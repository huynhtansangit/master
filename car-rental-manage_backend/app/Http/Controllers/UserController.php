<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request; 
use App\Http\Controllers\Controller; 
use Illuminate\Support\Str;
use App\User; 
use App\Profile;
use Illuminate\Support\Facades\Auth; 
use Illuminate\Support\Facades\Route;
use Laravel\Passport\Client;
use Illuminate\Support\Facades\Hash;
use Validator;
use App\Notifications\SignupActivate;

class UserController extends Controller 
{
	public $successStatus = 200;
/** 
     * login api 
     * 
     * @return \Illuminate\Http\Response 
     */ 
    public function login(Request $request) 
    {
        $validator = Validator::make($request->all(), [ 
            'email' => 'required|string|email', 
            'password' => 'required|string', 
        ]);
        if ($validator->fails()) { 
            return response()->json(['error'=>$validator->errors()], 401);            
        }
        $credentials = request(['email', 'password']);
        $credentials['active'] = 1;
        $credentials['deleted_at'] = null;
        if(!Auth::attempt($credentials)){ 
            return response()->json(['error'=>'Unauthorised'], 401);
        } 
        else{ 
            $user = Auth::user(); 
            $success['token'] =  $user->createToken('MyApp')-> accessToken; 
            return response()->json($success, $this-> successStatus); 
        } 
    }
/** 
     * Register api 
     * 
     * @return \Illuminate\Http\Response 
     */ 
    public function register(Request $request) 
    { 
        $validator = Validator::make($request->all(), [ 
            'name' => 'required', 
            'email' => 'required|unique:users', 
            'password' => 'required|min:6|confirmed', 
        ]);
		if ($validator->fails()) { 
            return response()->json(['error'=>$validator->errors()], 401);            
        }

        $user = new User([
            'name' => $request->name,
            'email' => $request->email,
            'password' => bcrypt($request->password),
            'activation_token' => Str::random(60)
        ]);

        $user->save();
        $user->notify(new SignupActivate($user));

        $profile = Profile::create([
            'user_id' => $user->id,
            'name' => $request->name,
            'email' => $request->email,
        ]);
        $profile->save();

		return response()->json(['message' => 'Successful'], $this-> successStatus); 
    }
/** 
     * details api 
     * 
     * @return \Illuminate\Http\Response 
     */ 
/*    public function details() 
    { 
        $user = Auth::user(); 
        return response()->json($user, $this-> successStatus); 
    } */

/*    public function update(Request $request)
    {
        $user = Auth::user();       
        $user->update($request->except('password'));

        return response()->json(['message' => 'Update Successful'], $this-> successStatus); 
    }*/

    public function change(Request $request)
    {
        $user = Auth::user();
        $data = $this->validate($request, [
            'current_password' => ['required', function($attribute, $value, $fail) {
                $user = Auth::user();
                if (!Hash::check($value, $user->password)) {
                    return $fail($attribute.' is invalid.');
                }
        }],
            'new_password' => 'required|string|min:6|different:current_password|confirmed',
        ]);
        $user->password = bcrypt($data['new_password']);
        $user->save();

        return response()->json(['message' => 'Update Successful'], $this-> successStatus); 
    }
/**
     * Log the user out (Invalidate the token).
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout()
    {
        Auth::user()->token()->delete();

        return response()->json(['message' => 'Logout Successful'], $this-> successStatus);
    }

    public function signupActivate($token)
    {
        $user = User::where('activation_token', $token)->first();
        if (!$user) {
            return response()->json(['message' => 'This activation token is invalid.'], 404);
        }
        $user->active = true;
        $user->activation_token = '';
        $user->save();
        return $user;
    }
}