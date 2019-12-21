<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User; 
use App\Profile;
use Illuminate\Support\Facades\Auth;

class ProfileController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $profile = Profile::where('user_id',Auth::id())->first(); 
        return response()->json($profile,200);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $profile = Profile::where('user_id',Auth::id())->first();       
        $profile->update($request->all());
        
        $this->validate($request,
            [
                'avatar'=>'image|mimes:jpeg,jpg,png,bmp,gif,svg|max:2048',
                'wallpaper'=>'image|mimes:jpeg,jpg,png,bmp,gif,svg|max:2048'
            ]
        );
        if($request->hasFile('avatarImg')){
            $fileName = $request->file('avatarImg')->getClientOriginalName();
            $path = $request->file('avatarImg')->move(public_path('upload/'),$fileName);
            $photoURL = url('upload/'.$fileName); 
            $profile->avatar = $photoURL;  
        }
        if($request->hasFile('wallpaperImg')){
            $fileName = $request->file('wallpaperImg')->getClientOriginalName();
            $path = $request->file('wallpaperImg')->move(public_path('upload/'),$fileName);
            $photoURL = url('upload/'.$fileName); 
            $profile->wallpaper = $photoURL;  
        }
        $profile->save();
        return response()->json($profile,201);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $profile = Profile::find($id);
        if(is_null($profile))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        return response()->json($profile,200);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
