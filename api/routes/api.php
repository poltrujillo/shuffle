<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Middleware\ValidateRegister;
use App\Http\Middleware\ValidateLogin;
use App\Http\Controllers\UserController;
use App\Http\Controllers\GameController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::post('/register', [AuthController::class, 'register'])->middleware(ValidateRegister::class);
Route::post('/login', [AuthController::class, 'login'])->middleware(ValidateLogin::class);
Route::post('/logout', [AuthController::class, 'logout'])->middleware('auth:sanctum');
Route::get('/auth/user', [AuthController::class, 'user'])->middleware('auth:sanctum');

Route::get('/users', [UserController::class, 'index'])->middleware('auth:sanctum');
Route::get('/users/{id}', [UserController::class, 'show'])->middleware('auth:sanctum');
Route::post('/users', [UserController::class, 'store'])->middleware('auth:sanctum');
Route::put('/users/{id}', [UserController::class, 'update'])->middleware('auth:sanctum');
Route::delete('/users/{id}', [UserController::class, 'destroy'])->middleware('auth:sanctum');
Route::put('/users/password/{id}', [UserController::class, 'updatePassword'])->middleware('auth:sanctum');
Route::post('/users/image/{id}', [UserController::class, 'updateProfileImage'])->middleware('auth:sanctum');
Route::put('users/admin/{id}', [UserController::class, 'updateAdmin'])->middleware('auth:sanctum');

// Password reset and update:
Route::post('/password/forgot', [UserController::class, 'forgotPassword']);
Route::post('/password/reset', [UserController::class, 'resetPassword']);

Route::post('/games', [GameController::class, 'store'])->middleware('auth:sanctum');
