package app.example.spotifytest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.example.spotifytest.BuildConfig
import app.example.spotifytest.R
import app.example.spotifytest.adapter.PlaylistAdapter
import app.example.spotifytest.api.UserApi
import app.example.spotifytest.data.UserModel
import app.example.spotifytest.data.UserPlaylistModel
import com.bumptech.glide.Glide
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    //<editor-fold desc="Vars">

    private val REQUEST_CODE = 1337
    private val TAG = "MainActivity"
//    private val USER_ID = "wizzler"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    //</editor-fold>

    //<editor-fold desc="Life Cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        textView.setOnClickListener {
//            val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
//            AuthenticationClient.openLoginActivity(
//                this,
//                REQUEST_CODE,
//                request
//            )
//        }

        connectedToUser()
    }

    override fun onStart() {
        super.onStart()

//        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
//            .setRedirectUri(REDIRECT_URI)
//            .showAuthView(true)
//            .build()
//
//        SpotifyAppRemote.connect(this, connectionParams,
//            object : ConnectionListener {
//
//                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
//                    mSpotifyAppRemote = spotifyAppRemote
//                    Log.d("MainActivity", "Connected! Yay!" )
//
//                    // Now you can start interacting with App Remote
//                    connected()
//
//                }
//
//                override fun onFailure(throwable: Throwable) {
//                    Log.e("MainActivity", throwable.message, throwable)
//                    // Something went wrong when attempting to connect! Handle errors here
//
//                    Toast.makeText(applicationContext, "Be sure to have and log-in in Spotify", Toast.LENGTH_SHORT).show()
//                }
//            })


//        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN,
//            REDIRECT_URI)


//        builder.setScopes(arrayOf("streaming"))
//        val request = builder.build()
//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    private fun connectedToUser() {

//        val spotifyApi = SpotifyApi()
//
//        spotifyApi.setAccessToken(accessToken)
//
//        val spotify: SpotifyService = spotifyApi.service
//
////        spotify.me
////        val restAdapter = RestAdapter.Builder()
////            .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
////            .setRequestInterceptor { request -> request!!.addHeader("Authorization",
////                "Bearer $accessToken"
////            )}
////            .build()
////
////        val spotifyService = restAdapter.create(SpotifyService::class.java)
//
//        val request = Request.Builder()
//            .url("https://api.spotify.com/v1/me")
//            .addHeader("Authorization", "Bearer $accessToken")
//            .build()
//
//
//        val mOkHttpClient = OkHttpClient()
//        val call = mOkHttpClient.newCall(request)
//
//        call.enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                Log.d(TAG, "onFailure JSONSTRIGNIGNGINGINGIGN")
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                try {
//                    val jsonObject = JSONObject(response.body!!.string())
//
//                    Log.d(TAG, "${jsonObject.get("display_name")} JSONSTRIGNIGNGINGINGIGN")
//
////                    textView.text = jsonObject.get("display_name").toString()
////                    followers.text = jsonObject.get("followers").toString()
//
//                    Log.d("Status: ", "Success get all JSON ${jsonObject.toString(3)}")
//                } catch (e: JSONException) {
//                    Log.d("Status: ", "Failed to parse data: $e")
//                }
//            }
//
//        })

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userApi = retrofit.create(UserApi::class.java)

        getUserProfileData(userApi)

        getUserPlayList(userApi)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            val response = AuthenticationClient.getResponse(resultCode, data)

            when(response.type){
                AuthenticationResponse.Type.TOKEN ->{
                    Log.d(TAG, response.accessToken)
//                    connectedToUser()
                }
                AuthenticationResponse.Type.ERROR ->{
                    Log.d(TAG, resultCode.toString())
                    Log.d(TAG, response.toString())
                    somethingWentWrong.visibility = VISIBLE
                    recyclerViewPlaylist.visibility = GONE
                    Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.d(TAG, "$response")
                    somethingWentWrong.visibility = VISIBLE
                    recyclerViewPlaylist.visibility = GONE
                    Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    //</editor-fold>

    //<editor-fold desc="Connected">

    private fun connected() {
        // Play a playlist
//        mSpotifyAppRemote?.playerApi?.play("spotify:playlist:37i9dQZF1DX4sWSpwq3LiO")

//        mSpotifyAppRemote?.playerApi?.pause()
        // Subscribe to PlayerState
        mSpotifyAppRemote?.playerApi?.
            subscribeToPlayerState()?.
            setEventCallback { playerState ->
                val track = playerState.track
                if (track != null) {
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                }
            }
    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(
            BuildConfig.CLIENT_ID,
            type,
            BuildConfig.REDIRECT_URI
        )
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email"))
            .build()
    }

    //</editor-fold>

    //<editor-fold desc="API">

    private fun getUserProfileData(userApi: UserApi) {
        val callUserModel = userApi.getUserData(BuildConfig.USER_ID)
        callUserModel.enqueue( object : Callback<UserModel>{
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG, "<<>>> $t")
                somethingWentWrong.visibility = VISIBLE
                recyclerViewPlaylist.visibility = GONE
                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    when (response.code()) {
                        200 ->{
                            Log.d(TAG, "Successful")
                            textViewName.text = response.body()?.display_name
                            textViewFollowers.text = response.body()?.followers?.total.toString()

                            if (response.body()?.images?.isNotEmpty()!!)
                                Glide.with(applicationContext)
                                    .load(response.body()?.images!![0].url)
                                    .into(imgUploadPhoto)
//                            Log.d("MAINACTIVITY.COM XDDDDD", response.body()?.)
                        }
                        401 -> {
                            Log.d(TAG, "Unauthorized")
                            somethingWentWrong.visibility = VISIBLE
                            recyclerViewPlaylist.visibility = GONE
                            Toast.makeText(applicationContext, "The request requires user authentication", Toast.LENGTH_SHORT).show()
                        }
                        403 ->{
                            Log.d(TAG, "Forbidden")
                            somethingWentWrong.visibility = VISIBLE
                            recyclerViewPlaylist.visibility = GONE
                            Toast.makeText(applicationContext, resources.getString(
                                R.string.failed_in_the_server
                            ), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    somethingWentWrong.visibility = VISIBLE
                    recyclerViewPlaylist.visibility = GONE
                    Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun getUserPlayList(userApi: UserApi) {
        val callUserModel = userApi.getUserPlaylist(BuildConfig.USER_ID)
        callUserModel.enqueue( object : Callback<UserPlaylistModel>{
            override fun onFailure(call: Call<UserPlaylistModel>, t: Throwable) {
                somethingWentWrong.visibility = VISIBLE
                recyclerViewPlaylist.visibility = GONE
                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserPlaylistModel>, response: Response<UserPlaylistModel>) {
                somethingWentWrong.visibility = GONE
                recyclerViewPlaylist.visibility = VISIBLE
                recyclerViewPlaylist.layoutManager = LinearLayoutManager(applicationContext)
                recyclerViewPlaylist.setHasFixedSize(true)
                val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
                itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.item_decorator
                )!!)

                recyclerViewPlaylist.addItemDecoration(itemDecoration)

                if (response.isSuccessful) {
                    when (response.code()) {
                        200 ->{
                            recyclerViewPlaylist.adapter = PlaylistAdapter(response.body()!!.items, applicationContext)
                            Log.d(TAG, "Successful")
//                            textView.text = response.body()?.items!![0].name
//                            followers.text = response.body()?.followers?.total.toString()
                        }
                        401 -> {
                            Log.d(TAG, "Unauthorized")
                            somethingWentWrong.visibility = VISIBLE
                            recyclerViewPlaylist.visibility = GONE
                            Toast.makeText(applicationContext, "The request requires user authentication", Toast.LENGTH_SHORT).show()
                        }
                        403 ->{
                            Log.d(TAG, "Forbidden")
                            somethingWentWrong.visibility = VISIBLE
                            recyclerViewPlaylist.visibility = GONE
                            Toast.makeText(applicationContext, resources.getString(
                                R.string.failed_in_the_server
                            ), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Log.d(TAG, "$response")
                    somethingWentWrong.visibility = VISIBLE
                    recyclerViewPlaylist.visibility = GONE
                    Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    //</editor-fold>

    //<editor-fold desc="Constructor">

    companion object {
        const val TOKEN_ID = "BQCTc_lV-ErHl_CJh2Se9Y86QksOQz0Q3QKCTv0bLw5veKS0VlQjd7cIHOs__B9pt6yGmSqySjHkbaC-AvsNF4MlHAkrZ4taif_4Amsce2Qv4fUurXqXRcjg3LIEsIxk5ZegNwl2A2m91erf8mtqp8fFhELffoSinnP0NMQjduLDKyefhxs65KG2X4fa3Tx5BDpdOAOGMau3de0R6Z5h2exSzEPoUDKLUPgrkbrv2-UrMy8F8Q83T9oYu_9DgLCgNo0ovyOAsOnteBRnpTpTR3_0bQoLKfK10A"
    }

    //</editor-fold>
}
