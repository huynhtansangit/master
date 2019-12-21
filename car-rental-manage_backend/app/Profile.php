<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Profile extends Model
{
    use SoftDeletes;
   
    protected $dates = ['deleted_at'];
   
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */

    protected $fillable = [
        'id',
        'user_id',
        'name',
        'birth_day',
        'phone',
        'address',
        'gender',
        'avatar',
        'wallpaper',
    ];

    public function user()
    {
       return $this->belongsTo('App\User');
    }
}
