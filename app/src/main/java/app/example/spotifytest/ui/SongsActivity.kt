package app.example.spotifytest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import app.example.spotifytest.BuildConfig.USER_ID
import app.example.spotifytest.R
import app.example.spotifytest.api.UserApi
import app.example.spotifytest.data.UserTracksFromPlaylist
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_songs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongsActivity : AppCompatActivity() {

    //<editor-fold desc="Life Cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        val intent = intent.extras
        Log.d("SongsActiivyt<<", "${intent?.get("playlistID")}")
       callTracksFromPlayList(intent?.get("playlistID").toString())
    }


    //</editor-fold>

    //<editor-fold desc="API">

    private fun callTracksFromPlayList(playlistID: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userApi = retrofit.create(UserApi::class.java)

        val callUserModel = userApi.getUserTracksFromPlaylist(USER_ID, playlistID!!)

        callUserModel.enqueue(object  : Callback<UserTracksFromPlaylist>{
            override fun onFailure(call: Call<UserTracksFromPlaylist>, t: Throwable) {
                Log.d("SongActivity", "$t")
                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<UserTracksFromPlaylist>,
                response: Response<UserTracksFromPlaylist>
            ) {
                if (response.body()!!.images.isNotEmpty()){
                    Glide.with(applicationContext).load(response.body()!!.images[0].url)
                        .error(R.drawable.image_broken).into(imagePlaylistFromTracks)
                }

                namePlaylist.text = response.body()!!.name
                descriptionPlaylist.text = "${response.body()!!.followers.total} Followers"
            }

        })
    }

    //</editor-fold>
}
