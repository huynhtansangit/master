<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Post;
use App\User;
use Illuminate\Support\Facades\Auth;

class PostController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return response()->json(Post::get(),200);
    }

    public function getPost()
    {
        $post = User::with('post')->find(Auth::id());  
        return response()->json($post,200);
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
        $post = new Post;
        $post->user_id = Auth::id();
        $post->title = $request->input('title');
        $post->name = $request->input('name');
        $post->cost = $request->input('cost');
        $post->producer = $request->input('producer');
        $post->seats = $request->input('seats');
        if($request->hasFile('photo')) {
            $fileName = $request->file('photo')->getClientOriginalName();
            $path = $request->file('photo')->move(public_path('upload/Post'),$fileName);
            $photoURL = url('upload/Post/'.$fileName); 
            $post->url_image = $photoURL;  
        }
        $post->save();
        return response()->json($post,201);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $post = Post::with('comments')->find($id);
        if(is_null($post))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        return response()->json($post,200);
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
        $post = Post::find($id);
        if(is_null($post)||Auth::id()!=$post->user_id)
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        $post->update($request->all());
        return response()->json($post,200);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $post = Post::find($id);
        if(is_null($post))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        $post->delete();
        return response()->json(['message' => 'Successful'],204);
    }

    public function search(Request $request)
    {
        $post = Post::where('name','like','%'.$request->name.'%')->get();
        if(is_null($post))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        return response()->json($post,200);
    }

    public function sortByProducer($producer)
    {
        $post = Post::where('producer',$producer)->get();
        if(is_null($post))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        return response()->json($post,200);
    }

    public function sortBySeats($seats)
    {
        $post = Post::where('seats',$seats)->get();
        if(is_null($post))
        {
            return response()->json(['message' => 'Not Found!'],404);
        }
        return response()->json($post,200);
    }
}
