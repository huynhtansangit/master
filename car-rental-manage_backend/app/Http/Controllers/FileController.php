<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class FileController extends Controller
{
    public function user()
    {
    	return response()->download(public_path('upload/user.jpg'),'User Image');
    }

    public function upload(Request $request)
    {
    	$fileName = $request->file('photo')->getClientOriginalName();
    	$path = $request->file('photo')->move(public_path('upload/'),$fileName);
    	$photoURL = url('upload/'.$fileName);	
    	return response()->json(['url' => $photoURL],200);
    }
}
