package app.example.spotifytest.ui.Tracks.Player

import android.R.attr.bitmap
import android.R.attr.radius
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.example.spotifytest.BuildConfig
import app.example.spotifytest.R
import com.bumptech.glide.Glide
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    //<editor-fold desc="Vars">

    val viewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    private var mSpotifyAppRemote: SpotifyAppRemote? = null


    //</editor-fold>

    //<editor-fold desc="Life Cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        backButton.setOnClickListener { finish() }

        val getExtras = intent.extras

        connectionSpotify()

        observeData(getExtras?.getString("accessToken"), getExtras?.getString("trackID"))

        onClick()
    }

    //</editor-fold>

    //<editor-fold desc="Connected">

    private fun connectionSpotify() {
        val connectionParams = ConnectionParams.Builder(BuildConfig.CLIENT_ID)
            .setRedirectUri(BuildConfig.REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {

                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected!!" )

                    // Now you can start interacting with App Remote
                    val getExtras = intent.extras
                    mSpotifyAppRemote?.playerApi?.play("spotify:track:${getExtras?.getString("trackID")}")
                    imgPlayButton.setImageResource(R.drawable.pausa)
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here

                    Toast.makeText(applicationContext, "Be sure to have and log-in in Spotify", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun connected() {
        // Play a playlist
//        mSpotifyAppRemote?.playerApi?.play("spotify:playlist:37i9dQZF1DXcBWIGoYBM5M")
        mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback {
            if (it.playbackOptions!!.isShuffling){
                imgShuffleButton.setImageResource(R.drawable.shuffle)
            }else{
                imgShuffleButton.setImageResource(R.drawable.shuffle_gray)
            }

//            if (it.isPaused){
//                imgPlayButton.setImageResource(R.drawable.play_track)
//            }else{
//                imgPlayButton.setImageResource(R.drawable.pausa)
//            }
        }

        // Subscribe to PlayerState
        mSpotifyAppRemote?.playerApi?.
            subscribeToPlayerState()?.
            setEventCallback { playerState ->
                val track = playerState.track
                if (track != null) {
//                    Toast.makeText(applicationContext, "You are listening ${track.name}", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                }
            }
    }

    //</editor-fold>

    //<editor-fold desc="API">

    private fun observeData(accessToken: String?, trackID: String?) {
        viewModel.fetchGetTrack(accessToken!!, trackID!!).observe(this, Observer {
            nameTrack.text = it.name

            if (it.artists.isNotEmpty())
                durationTrack.text = it.artists[0].name

            imgTrack.clipToOutline = true

            if (it.album.images.isNotEmpty()) {
                Glide.with(applicationContext)
                    .load(it.album.images[0].url).error(R.drawable.image_broken).into(imgTrack)
            }
        })
    }

    //</editor-fold>

    //<editor-fold desc="OnClick">

    private fun onClick() {
        imgShuffleButton.setOnClickListener {
            mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback {
                if (it.playbackOptions!!.isShuffling){
                    imgShuffleButton.setImageResource(R.drawable.shuffle_gray)
                    mSpotifyAppRemote?.playerApi?.setShuffle(false)
                }else{
                    imgShuffleButton.setImageResource(R.drawable.shuffle)
                    mSpotifyAppRemote?.playerApi?.setShuffle(true)
                }
            }
        }

        playButton.setOnClickListener{
            mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback {
                if (it.isPaused){
                    mSpotifyAppRemote?.playerApi?.resume()
                    imgPlayButton.setImageResource(R.drawable.pausa)
                }else{
                    mSpotifyAppRemote?.playerApi?.pause()
                    imgPlayButton.setImageResource(R.drawable.play_track)
                }
            }
        }

        nextTrack.setOnClickListener {
            mSpotifyAppRemote?.playerApi?.skipNext()

            mSpotifyAppRemote?.playerApi?.playerState?.setResultCallback {
                Log.d("PLATERACTIVITY", "${it.track.duration}  DURATION ${it.track.name}" )
            }
        }

        backTrack.setOnClickListener {
            mSpotifyAppRemote?.playerApi?.skipPrevious()
        }
    }

    //</editor-fold>
}
