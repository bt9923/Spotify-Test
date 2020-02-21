package app.example.spotifytest.api

import app.example.spotifytest.data.user.UserModel
import app.example.spotifytest.data.playlist.UserPlaylistModel
import app.example.spotifytest.data.playlist.UserTracksFromPlaylist
import app.example.spotifytest.data.track.TrackModel
import retrofit2.Call
import retrofit2.http.GET
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

    @GET("tracks/{id}")
    fun getTrackByID(@Path("id") trackID : String) : Call<TrackModel>
}