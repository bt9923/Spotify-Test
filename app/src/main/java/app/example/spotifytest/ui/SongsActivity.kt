package app.example.spotifytest.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.example.spotifytest.BuildConfig.USER_ID
import app.example.spotifytest.R
import app.example.spotifytest.adapter.TracksFromPlaylists
import app.example.spotifytest.api.UserApi
import app.example.spotifytest.data.UserTracksFromPlaylist
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_songs.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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

        imageViewToolbar.setOnClickListener {
            finish()
        }
        // Quitamos elevaciòn para que estè acorde con los tabs
        if (supportActionBar != null) {
            supportActionBar!!.elevation = 0f
        }
        val intent = intent.extras
        Log.d("SongsActiivyt<<", "${intent?.get("playlistID")}")
        callTracksFromPlayList(intent?.get("playlistID").toString(),
            intent?.get("TOKEN_ID").toString())
    }

    //</editor-fold>

    //<editor-fold desc="API">

    private fun callTracksFromPlayList(playlistID: String?, accessToken: String) {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original = chain.request()

                val request = original.newBuilder()
                    .header( "Authorization", "Bearer $accessToken")
                    .method(original.method, original.body)
                    .build()

                return chain.proceed(request)
            }
        })
        val  client = httpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val userApi = retrofit.create(UserApi::class.java)

        val callUserModel = userApi.getUserTracksFromPlaylist(USER_ID, playlistID!!)

        recyclerViewTracks.layoutManager  = LinearLayoutManager(applicationContext)
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


                recyclerViewTracks.adapter = TracksFromPlaylists(response.body()!!, applicationContext)
            }
        })
    }

    //</editor-fold>
}
