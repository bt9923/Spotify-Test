package app.example.spotifytest.ui.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.example.spotifytest.BuildConfig.CLIENT_ID
import app.example.spotifytest.BuildConfig.REDIRECT_URI
import app.example.spotifytest.R
import app.example.spotifytest.adapter.PlaylistAdapter
import com.bumptech.glide.Glide
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //<editor-fold desc="Vars">

    lateinit var TOKEN_ID : String
    private val REQUEST_CODE = 1337
    private val TAG = "MainActivity"
//    private val USER_ID = "wizzler"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var adapter: PlaylistAdapter

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    //</editor-fold>

    //<editor-fold desc="Life Cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        imgUploadPhoto.setOnClickListener{
            val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
            AuthenticationClient.openLoginActivity(
                this,
                REQUEST_CODE,
                request
            )
        }

        recyclerViewPlaylist.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewPlaylist.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext,
            R.drawable.item_decorator
        )!!)
        recyclerViewPlaylist.addItemDecoration(itemDecoration)
//        connectedToUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            val response = AuthenticationClient.getResponse(resultCode, data)

            when(response.type){
                AuthenticationResponse.Type.TOKEN ->{
                    Log.d(TAG, response.accessToken)
//                    connectedToUser()
                    TOKEN_ID = response.accessToken

                    adapter = PlaylistAdapter(TOKEN_ID, applicationContext)
                    recyclerViewPlaylist.adapter = adapter
                    observeData()
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

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(
            CLIENT_ID,
            type,
            REDIRECT_URI
        )
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email"))
            .build()
    }

    //</editor-fold>

    //<editor-fold desc="API">

    fun observeData(){
        viewModel.fetchUserData(TOKEN_ID).observe(this, Observer {
            textViewName.text = it.display_name
            textViewFollowers.text = it.followers.total.toString()

            if (it.images.isNotEmpty())
                Glide.with(applicationContext)
                    .load(it.images[0].url)
                    .into(imgUploadPhoto)
        })

        viewModel.fetchGetPlayList(TOKEN_ID).observe(this, Observer {
            somethingWentWrong.visibility = GONE
            recyclerViewPlaylist.visibility = VISIBLE
            adapter.setListData(it.items)
            adapter.notifyDataSetChanged()
        })
    }

    //</editor-fold>

    //<editor-fold desc="Constructor">

    //</editor-fold>
}
