package app.example.spotifytest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.example.spotifytest.R
import app.example.spotifytest.data.ItemTrack
import app.example.spotifytest.data.UserTracksFromPlaylist
import com.bumptech.glide.Glide
import java.io.FileNotFoundException

class TracksFromPlaylists(val items: UserTracksFromPlaylist?, val context: Context)
    : RecyclerView.Adapter<TracksFromPlaylists.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.tracks, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items!!.tracks.items[position])
//        holder.imagePlaylist.setOnClickListener{
//            Toast.makeText(context, "ONCLICKITEM", Toast.LENGTH_SHORT).show()
//            val intent = Intent(context, SongsActivity::class.java)
//            intent.putExtra("playlistID", items[position].id)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return items!!.tracks.total
    }

    class MyViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener{
        override fun onClick(v: View?) {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        private val tvNameTrack = view.findViewById<TextView>(R.id.nameTrack)
        private val tvSinger = view.findViewById<TextView>(R.id.ownerTrack)
        private val imageTrack = view.findViewById<ImageView>(R.id.imageTrack)
        private val play = view.findViewById<ImageView>(R.id.play)

        fun bind(item: ItemTrack) {
            play.setOnClickListener(this)
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