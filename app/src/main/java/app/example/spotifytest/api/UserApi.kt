package app.example.spotifytest.api

import app.example.spotifytest.data.UserModel
import app.example.spotifytest.data.UserPlaylistModel
import app.example.spotifytest.data.UserTracksFromPlaylist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserApi {

//    @Headers("Authorization: Bearer ${MainActivity.TOKEN_ID}")
    @GET("users/{user_id}/")
    fun getUserData(@Path("user_id") user : String) : Call<UserModel>


//    @Headers("Authorization: Bearer ${MainActivity.TOKEN_ID}")
    @GET("users/{user_id}/playlists/")
    fun getUserPlaylist(@Path("user_id") user : String) : Call<UserPlaylistModel>

//    @Headers("Authorization: Bearer ${MainActivity.TOKEN_ID}")
    @GET("users/{user_id}/playlists/{playlist_id}/")
    fun getUserTracksFromPlaylist(@Path("user_id") user : String, @Path("playlist_id") playlistID : String) : Call<UserTracksFromPlaylist>
}