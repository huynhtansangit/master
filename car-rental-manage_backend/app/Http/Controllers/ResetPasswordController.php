<?php

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Support\Str;
use App\User; 
use Illuminate\Http\Request;
use App\ResetPassword;
use App\Notifications\ResetPasswordRequest;
use App\Notifications\PasswordResetSuccess;

class ResetPasswordController extends Controller
{
    /**
     * Create token password reset
     *
     * @param  [string] email
     * @return [string] message
     */
    public function sendMail(Request $request)
    {
        $request->validate([
            'email' => 'required|string|email',
        ]);
        $user = User::where('email', $request->email)->first();
        if (!$user)
            return response()->json(['message' => 'We cant find a user with that e-mail address.'], 404);
        $passwordReset = ResetPassword::updateOrCreate(
            ['email' => $user->email],
            [
                'email' => $user->email,
                'token' => Str::random(6),
            ]
        );
        if ($user && $passwordReset)
            $user->notify(
                new ResetPasswordRequest($passwordReset->token)
            );
        return response()->json(['message' => 'We have e-mailed your password reset link!']);
    }
    /**
     * Find token password reset
     *
     * @param  [string] $token
     * @return [string] message
     * @return [json] passwordReset object
     */
    public function find($token)
    {
        $passwordReset = ResetPassword::where('token', $token)->first();
        if (!$passwordReset)
            return response()->json(['message' => 'This password reset token is invalid.'], 404);
        if (Carbon::parse($passwordReset->updated_at)->addMinutes(720)->isPast()) {
            $passwordReset->delete();
            return response()->json(['message' => 'This password reset token is invalid.'], 404);
        }
        return response()->json($passwordReset);
    }
     /**
     * Reset password
     *
     * @param  [string] email
     * @param  [string] password
     * @param  [string] password_confirmation
     * @param  [string] token
     * @return [string] message
     * @return [json] user object
     */
    public function reset(Request $request)
    {
        $request->validate([
            'email' => 'required|string|email',
            'password' => 'required|string|confirmed',
            'token' => 'required|string'
        ]);
        $passwordReset = ResetPassword::where([
            ['token', $request->token],
            ['email', $request->email]
        ])->first();
        if (!$passwordReset)
            return response()->json(['message' => 'This password reset token is invalid.'], 404);
        $user = User::where('email', $passwordReset->email)->first();
        if (!$user)
            return response()->json(['message' => 'We cant find a user with that e-mail address.'], 404);
        $user->password = bcrypt($request->password);
        $user->save();
        $passwordReset->delete();
        $user->notify(new PasswordResetSuccess($passwordReset));
        return response()->json($user);
    }
}