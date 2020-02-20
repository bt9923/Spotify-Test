package app.example.spotifytest.ui.Tracks.Player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.example.spotifytest.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        backButton.setOnClickListener { finish() }

        imgTrack.setImageResource(R.drawable.ad_astra)
        nameTrack.text = "AD - ASTRA"
    }
}
