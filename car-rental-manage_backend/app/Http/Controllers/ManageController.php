<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Hash;
use App;
use Session;
use App\Charts\MyChart;
use App\User; 
use App\Profile;
use App\Post;

class ManageController extends Controller
{
    public function getLogin()
    {
        if(Auth::check()) {
            $user = Auth::user();
            if($user->role == 'admin')
                return redirect('manage/home');
        }
        return view('auth.login');
    }
    public function postLogin(Request $req)
    {
        $v = Validator::make($req->all(), [
            'email'    => 'required|email',
            'password' => 'required|alphaNum|min:3'
        ]);
        $data=[
            'email'=>$req->email,
            'password'=>$req->password
        ];
        if(Auth::attempt($data)){
            $user = Auth::user();
            if($user->role == 'admin')
                return redirect('manage/home');
            else
                return redirect('manage/login');
            /*return redirect('manage/home');*/
        }else{
            return redirect()->back()->withErrors($v->errors());
        }
    }
    public function logout()
    {
        Auth::logout();
        return redirect('manage/login');
    }

    public function getChangePassword()
    {
        if(Auth::check()) {
            return view('auth.password.change');
        }
        return view('auth.login');
    }
    public function postChangePassword(Request $req)
    {
        if (!(Hash::check($req->get('current-password'), Auth::user()->password))) {
        // The passwords matches
            return redirect()->back()->with("error","Your current password does not matches with the password you provided. Please try again.");
        }
        if(strcmp($req->get('current-password'), $req->get('new-password')) == 0){
        //Current password and new password are same
            return redirect()->back()->with("error","New Password cannot be same as your current password. Please choose a different password.");
        }
//        $validatedData = $req->validate([
//            'current-password' => 'required',
//            'new-password' => 'required|min:6',
//        ]);
        //Change Password
        $user = Auth::user();
        $user->password = bcrypt($req->get('new-password'));
        $user->save();
        return redirect()->back()->with("success","Password changed successfully !");
    }
    public function getDashboard()
    {
        $today_users = User::whereDate('created_at', today())->count();
        $yesterday_users = User::whereDate('created_at', today()->subDays(1))->count();
        $users_2_days_ago = User::whereDate('created_at', today()->subDays(2))->count();
        $users_3_days_ago = User::whereDate('created_at', today()->subDays(3))->count();
        $users_4_days_ago = User::whereDate('created_at', today()->subDays(4))->count();
        $users_5_days_ago = User::whereDate('created_at', today()->subDays(5))->count();
        $users_6_days_ago = User::whereDate('created_at', today()->subDays(6))->count();
        $users_7_days_ago = User::whereDate('created_at', today()->subDays(7))->count();
        $chart = new MyChart;
        $chart->labels(['7 days ago','6 days ago','5 days ago','4 days ago','3 days ago','2 days ago', 'Yesterday', 'Today']);
        $chart->displaylegend(false);
        $chart->dataset('', 'line', [$users_7_days_ago,$users_6_days_ago,$users_5_days_ago,$users_4_days_ago,$users_3_days_ago,$users_2_days_ago, $yesterday_users, $today_users])
            ->color("rgba(2,117,216,1)")
            ->backgroundcolor('rgba(2,117,216,0.2)')
            ->linetension(0.3);

        $tables = Profile::all();
        return view('home',compact('chart','tables'));
        /*return view('home');*/
    }
    public function getChart()
    {
        $data = collect([]); 
        for ($day = 6; $day >= 0; $day--) {
            $data->push(User::whereDate('created_at', today()->subDays($day))->count());
        }

        $chart = new MyChart;
        $chart->labels(['6 days ago','5 days ago','4 days ago','3 days ago','2 days ago', 'Yesterday', 'Today']);
        $chart->displaylegend(false);
        $chart->dataset('', 'line', $data)
            ->color("rgba(2,117,216,1)")
            ->backgroundcolor('rgba(2,117,216,0.2)')
            ->linetension(0.3);

        $new_post = collect([]);
        for ($month = 11; $month >= 0; $month--)  {
            $new_post->push(Post::whereMonth('created_at', today()->subMonths($month))->count());
        }

        $bar_chart = new MyChart;
        $bar_chart->labels(['January','February','March','April','May','June', 'July', 'August', 'September', 'October', 'November', 'December']);
        $bar_chart->displaylegend(false);
        $bar_chart->dataset('', 'bar', $new_post)
            ->color('rgba(2,117,216,1)')
            ->backgroundcolor("rgba(2,117,216,1)");

        $pie_chart = new MyChart;

        return view('manage.chart',compact('chart','bar_chart','pie_chart'));
    }
    public function getTables()
    {
        $tables = Profile::all();
        return view('manage.tables',compact('tables'));
    }
}
