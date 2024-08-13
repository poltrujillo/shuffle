<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\Game;

class GameMode extends Model
{
    use HasFactory;

    protected $table = 'game_modes';
    public $incrementing = true;
    public $timestamps = false;
    protected $keyType = 'int';
    protected $fillable = [
        'game_mode',
        'description'
    ];

    public function game(): BelongsTo
    {
        return $this->belongsTo(Game::class);
    }
}
