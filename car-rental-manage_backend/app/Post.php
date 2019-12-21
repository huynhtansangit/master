<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Post extends Model
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
    	'title',
        'name',
    	'cost',
        'producer',
        'seats',
        'url_image',
    ];

    public function Post()
    {
       return $this->belongsTo('App\User');
    }

    public function comments()
    {
       return $this->hasMany('App\Comment');
    }
}
