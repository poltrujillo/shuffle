<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Api extends Model
{
    use HasFactory;

    protected $table = 'apis';
    public $incrementing = true;
    public $timestamps = false;
    protected $keyType = 'int';
    protected $fillable = [
        'api_name',
        'is_active',
        'url',
        'platform',
        'api_key'
    ];
}
