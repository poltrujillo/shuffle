<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\User;
use App\Models\Playlist;
use App\Models\GameMode;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Game extends Model
{
    use HasFactory;

    protected $table = 'games';
    public $incrementing = true;
    public $timestamps = false;
    protected $keyType = 'int';
    protected $fillable = [
        'game_modes_id',
        'rounds_count',
        'game_time'
    ];

    public function users(): BelongsToMany
    {
        return $this->belongsToMany(User::class, 'user_game', 'game_id', 'user_id')
                    ->withPivot('score', 'ranking');
    }

    public function playlist(): HasMany
    {
        return $this->hasMany(Playlist::class);
    }

    public function gameMode(): HasMany
    {
        return $this->hasMany(GameMode::class);
    }
}
