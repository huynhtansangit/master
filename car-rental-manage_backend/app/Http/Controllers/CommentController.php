<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\Post;
use App\Comment;
use Illuminate\Support\Facades\Auth;

class CommentController extends Controller
{
    public function store(Request $request, $id)
    {
    	$post = Post::findOrFail($id);
        
        Comment::create([
            'body' => $request->body,
            'user_id' => Auth::id(),
            'post_id' => $id
        ]);
   
        return response()->json(['message' => 'Successful'],200);
    }
}
