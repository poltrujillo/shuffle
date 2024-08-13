<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\Game;

class Playlist extends Model
{
    use HasFactory;

    protected $table = 'playlists';
    public $incrementing = true;
    public $timestamps = false;
    protected $keyType = 'int';
    protected $fillable = [
        'playlist_name',
        'times_played'
    ];

    public function game(): BelongsTo
    {
        return $this->belongsTo(Game::class);
    }
}
