<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\GameMode;
use App\Models\Api;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // \App\Models\User::factory(10)->create();

        // \App\Models\User::factory()->create([
        //     'name' => 'Test User',
        //     'email' => 'test@example.com',
        // ]);
        $this->createGameModes();
        $this->createApi();
    }

    private function createGameModes()
    {
        $modes = [
            ['game_mode' => 'Genre',
             'description' => 'Este modo de juego consiste en jugar con generos predefinidos de Spotify.'],
            ['game_mode' => 'Artist',
             'description' => 'Este modo de juego consiste en jugar con artistas predefinidos de Spotify.'],
            ['game_mode' => 'Playlist',
             'description' => 'Este modo de juego consiste en jugar con playlist privadas o publicas de los usuarios de Spotify.']
        ];

        foreach ($modes as $mode) {
            GameMode::updateOrCreate(
                ['game_mode' => $mode['game_mode']],
                ['description' => $mode['description']]
            );
        }
    }

    private function createApi()
    {
        $api = [
            ['api_name' => 'Spotify Web', 'api_key' => '', 'is_active' => 1, 'url' => 'https://developer.spotify.com/documentation/web-api', 'platform' => 'Web'],
                ['api_name' => 'Spotify Android', 'api_key' => '', 'is_active' => 1, 'url' => 'https://developer.spotify.com/documentation/android', 'platform' => 'Android'],
                    ['api_name' => 'Spotify Playback', 'api_key' => '', 'is_active' => 1, 'url' => 'https://developer.spotify.com/documentation/web-playback-sdk', 'platform' => 'Web / Android']
        ];

        foreach ($api as $api) {
            Api::updateOrCreate(
                ['api_name' => $api['api_name']],
                ['api_key' => $api['api_key'], 'is_active' => $api['is_active'], 'url' => $api['url'], 'platform' => $api['platform']]
            );
        }
    }
}
