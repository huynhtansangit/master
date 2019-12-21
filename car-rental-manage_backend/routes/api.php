<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/



Route::post('login', 'UserController@login');
Route::post('register', 'UserController@register');
Route::get('activate/{token}', 'UserController@signupActivate');
Route::group(['middleware' => 'auth:api'], function() {
/*	Route::post('details', 'UserController@details');
	Route::post('update', 'UserController@update');*/
	Route::post('post/{id}', 'CommentController@store');
	Route::post('changepassword', 'UserController@change');
	Route::post('logout', 'UserController@logout');
	Route::resource('profile','ProfileController');
	Route::get('ypost','PostController@getPost');
	Route::resource('post','PostController');
	Route::get('search','PostController@search');
	Route::get('post/producer/{producer}','PostController@sortByProducer');
	Route::get('post/seats/{seats}','PostController@sortBySeats');
/*	Route::get('file','FileController@user');
	Route::post('file','FileController@upload');*/
});
Route::post('create', 'ResetPasswordController@sendMail');
/*Route::get('find/{token}', 'ResetPasswordController@find');*/
Route::post('reset', 'ResetPasswordController@reset');
