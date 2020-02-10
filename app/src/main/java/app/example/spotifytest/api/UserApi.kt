package app.example.spotifytest.api

import app.example.spotifytest.MainActivity
import app.example.spotifytest.data.UserModel
import app.example.spotifytest.data.UserPlaylistModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserApi {

    @Headers("Authorization: Bearer ${MainActivity.TOKEN_ID}")
    @GET("users/{user_id}/")
    fun getUserData(@Path("user_id") user : String) : Call<UserModel>


    @Headers("Authorization: Bearer ${MainActivity.TOKEN_ID}")
    @GET("users/{user_id}/playlists/")
    fun getUserPlaylist(@Path("user_id") user : String) : Call<UserPlaylistModel>
}