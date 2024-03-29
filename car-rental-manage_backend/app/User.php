<?php
namespace App;
use Laravel\Passport\HasApiTokens;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
class User extends Authenticatable 
{
  use HasApiTokens, Notifiable, SoftDeletes;

  	protected $dates = ['deleted_at'];
	/**
	* The attributes that are mass assignable.
	*
	* @var array
	*/
	protected $fillable = [
		'name', 'email', 'password', 'active', 'activation_token'
	];
	/**
	* The attributes that should be hidden for arrays.
	*
	* @var array
	*/
	protected $hidden = [
		'password', 'remember_token', 'activation_token'
	];

	public function userProfile()
    {
       return $this->hasOne('App\Profiles');
    }

    public function post()
    {
       return $this->hasMany('App\Post');
    }
}