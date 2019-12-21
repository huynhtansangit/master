<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('manage/login',[
    'as'=>'login',
    'uses'=>'ManageController@getLogin'
]);
Route::post('manage/login',[
    'as'=>'login',
    'uses'=>'ManageController@postLogin'
]);

Route::group(['middleware' => 'auth'], function() {

    Route::get('manage/logout',[
        'as'=>'logout',
        'uses'=>'ManageController@logout'
    ]);
    Route::get('manage/change',[
        'as'=>'change',
        'uses'=>'ManageController@getChangePassword'
    ]);
    Route::post('manage/change',[
        'as'=>'change-password',
        'uses'=>'ManageController@postChangePassword'
    ]);

    Route::get('manage/home',[
        'as'=>'home',
        'uses'=>'ManageController@getDashboard'
    ]);

	Route::get('manage/chart',[
        'as'=>'chart',
        'uses'=>'ManageController@getChart'
    ]);

	Route::get('manage/tables',[
        'as'=>'tables',
        'uses'=>'ManageController@getTables'
    ]);
});

Route::get('/', function () {
    return redirect('/manage/login');
});


