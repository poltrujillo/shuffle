<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Password;
use Illuminate\Support\Facades\RateLimiter;
use Illuminate\Support\Facades\Log;
use Illuminate\Validation\ValidationException;
use Illuminate\Auth\Events\PasswordReset;
use Illuminate\Support\Str;

class UserController extends Controller
{
    public function index()
    {
        return response()->json(User::all());
    }

    public function store(Request $request)
    {
        $user = User::create($request->all());
        return response()->json([
            'message' => 'User created successfully',
            'user' => $user
        ], 201);
    }

    public function show(string $id)
    {
        return response()->json(User::find($id));
    }

    public function update(Request $request, string $id)
    {
        $validatedData = $request->validate([
            'username' => 'required|string|max:255',
            'email' => 'required|string|email|max:255',
        ]);

        $user = User::find($id);
        if (!$user) {
            return response()->json([
                'message' => 'User not found'
            ], 404);
        }

        $user->update($validatedData);

        return response()->json([
            'message' => 'User updated successfully',
            'user' => $user
        ]);
    }

    public function destroy(string $id)
    {
        User::destroy($id);
        return response()->json([
            'message' => 'User deleted successfully'
        ]);
    }

    public function updateAdmin(string $id)
    {
        $user = User::find($id);
        if (!$user) {
            return response()->json([
                'message' => 'User not found'
            ], 404);
        }

        $user->update(['is_admin' => !$user->is_admin]);

        return response()->json([
            'message' => 'User updated successfully',
            'user' => $user
        ]);
    }

    public function updateProfileImage(Request $request, string $id)
    {
        Log::info('Received request to update profile image for user id: ' . $id);

        try {
            $request->validate([
                'image' => 'required|image|mimes:jpeg,png,jpg,gif,svg|max:2048',
            ]);

            $user = User::find($id);
            if (!$user) {
                Log::error('User not found with id: ' . $id);
                return response()->json(['message' => 'User not found'], 404);
            }

            if ($request->hasFile('image')) {
                $imageName = time() . '.' . $request->image->extension();
                $request->image->move(public_path('images'), $imageName);
                $user->update(['image' => 'images/' . $imageName]);
            } else {
                Log::error('No image file detected in request for user id: ' . $id);
                return response()->json(['message' => 'No image file provided'], 400);
            }

            Log::info('Profile image updated successfully for user id: ' . $id);
            return response()->json([
                'message' => 'Profile image updated successfully',
                'user' => $user
            ]);
        } catch (\Exception $e) {
            Log::error("Error updating profile image for user id: {$id}: {$e->getMessage()}");
            return response()->json(['message' => 'Server Error'], 500);
        }
    }

    public function updatePassword(Request $request, string $id)
    {
        $request->validate([
            'old_password' => 'required',
            'password' => 'required|min:8',
        ]);

        $user = User::find($id);
        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }

        if (!Hash::check($request->old_password, $user->password)) {
            return response()->json(['message' => 'Old password is incorrect'], 401);
        }

        $user->update([
            'password' => bcrypt($request->password)
        ]);

        return response()->json([
            'message' => 'Password updated successfully',
            'user' => $user
        ]);
    }

    public function forgotPassword(Request $request)
    {
        $request->validate(['email' => 'required|email']);

        $status = Password::sendResetLink(
            $request->only('email')
        );

        return $status === Password::RESET_LINK_SENT
                    ? response()->json(['status' => __($status)], 200)
                    : response()->json(['email' => __($status)], 400);
    }

    public function resetPassword(Request $request)
    {
        $request->validate([
            'token' => 'required',
            'email' => 'required|email',
            'password' => 'required|min:8|confirmed',
        ]);

        $status = Password::reset(
            $request->only('email', 'password', 'password_confirmation', 'token'),
            function (User $user, string $password) {
                $user->forceFill([
                    'password' => Hash::make($password)
                ])->setRememberToken(Str::random(60));

                $user->save();

                event(new PasswordReset($user));
            }
        );

        return $status === Password::PASSWORD_RESET
                    ? response()->json(['status' => __($status)], 200)
                    : response()->json(['email' => __($status)], 400);
    }
}
