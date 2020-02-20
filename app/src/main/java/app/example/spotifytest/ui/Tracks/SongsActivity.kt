package app.example.spotifytest.ui.Tracks

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.example.spotifytest.R
import app.example.spotifytest.adapter.TracksFromPlaylistAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_songs.*
import kotlinx.android.synthetic.main.toolbar.*

class SongsActivity : AppCompatActivity() {

    //<editor-fold desc="Life Cycle">

    private lateinit var adapter: TracksFromPlaylistAdapter

    private val viewModel by lazy {
        ViewModelProvider(this).get(SongsViewModel::class.java)
    }

    //</editor-fold>

    //<editor-fold desc="Life Cycle">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        imageViewToolbar.setOnClickListener {
            finish()
        }

        recyclerViewTracks.layoutManager  = LinearLayoutManager(applicationContext)
        adapter = TracksFromPlaylistAdapter(applicationContext)
        recyclerViewTracks.adapter = adapter

        if (supportActionBar != null) {
            supportActionBar!!.elevation = 0f
        }
        val intent = intent.extras
        observeData(intent?.get("TOKEN_ID").toString(), intent?.get("playlistID").toString())
//        callTracksFromPlayList(intent?.get("playlistID").toString(),
//            intent?.get("TOKEN_ID").toString())
    }

    //</editor-fold>

    //<editor-fold desc="API">

    private fun observeData(accessToken: String, playlistID: String) {

        viewModel.fetchUserData(accessToken, playlistID).observe(this, Observer {
            Glide.with(applicationContext).load(it.images!![0].url)
                .error(R.drawable.image_broken).into(imagePlaylistFromTracks)

            namePlaylist.text = it.name
            descriptionPlaylist.text = "${it.followers?.total} Followers"

            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    //</editor-fold>
}
