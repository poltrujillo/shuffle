<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Game;
use Illuminate\Support\Facades\DB;

class GameController extends Controller
{
    public function store(Request $request)
    {
        DB::beginTransaction();

        try {
            $game = Game::create([
                'game_modes_id' => $request->game_modes_id,
            ]);

            $game->users()->sync($request->user_id);

            DB::commit();

            return response()->json([
                'message' => 'Game created and linked to users successfully',
                'game' => $game
            ], 201);
        } catch (\Throwable $e) {
            DB::rollback();

            // Return an error response
            return response()->json([
                'message' => 'Failed to create the game',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}
