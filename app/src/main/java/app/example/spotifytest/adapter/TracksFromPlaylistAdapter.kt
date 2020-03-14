package app.example.spotifytest.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.example.spotifytest.R
import app.example.spotifytest.data.playlist.ItemTrack
import app.example.spotifytest.data.playlist.UserTracksFromPlaylist
import app.example.spotifytest.ui.Tracks.Player.PlayerActivity
import com.bumptech.glide.Glide
import java.io.FileNotFoundException

class TracksFromPlaylistAdapter(val context: Context)
    : RecyclerView.Adapter<TracksFromPlaylistAdapter.MyViewHolder>() {

    private var dataList : UserTracksFromPlaylist? =
        UserTracksFromPlaylist()

    private lateinit var accesToken : String

    fun setListData(
        tracks: UserTracksFromPlaylist,
        accessToken: String
    ){
        dataList = tracks
        accesToken = accessToken
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.tracks, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList!!.tracks!!.items[position])

        holder.play.setOnClickListener{
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("accessToken", accesToken)
            intent.putExtra("trackID", dataList!!.tracks!!.items[position].track.id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (dataList!!.tracks == null){
            0
        }else{
            dataList!!.tracks!!.total
        }
    }

    class MyViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view){
//        override fun onClick(v: View?) {
//            val intent = Intent(context, PlayerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)
//            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
//        }

        private val tvNameTrack = view.findViewById<TextView>(R.id.nameTrack)
        private val tvSinger = view.findViewById<TextView>(R.id.ownerTrack)
        private val imageTrack = view.findViewById<ImageView>(R.id.imageTrack)
        internal val play = view.findViewById<ImageView>(R.id.play)

        fun bind(item: ItemTrack) {
//            play.setOnClickListener(this)
            tvNameTrack.text = item.track.name
            tvSinger.text = item.track.artists[0].name
            if (item.track.album.images[0].url.isNotEmpty())
                imageTrack.loadUrl(item.track.album.images[0].url)
        }

        private fun ImageView.loadUrl(url: String) {
            try {
                Glide.with(context).load(url).error(R.drawable.musica).into(this)
            } catch (e: FileNotFoundException) {
                Toast.makeText(context, "The image is not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}