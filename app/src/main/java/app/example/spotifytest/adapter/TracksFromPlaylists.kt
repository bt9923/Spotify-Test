package app.example.spotifytest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.example.spotifytest.R
import app.example.spotifytest.data.Item
import app.example.spotifytest.ui.SongsActivity
import com.bumptech.glide.Glide
import java.io.FileNotFoundException

class TracksFromPlaylists (val items: List<Item>, val context: Context)
    : RecyclerView.Adapter<TracksFromPlaylists.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.playlist, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
//        holder.imagePlaylist.setOnClickListener{
//            Toast.makeText(context, "ONCLICKITEM", Toast.LENGTH_SHORT).show()
//            val intent = Intent(context, SongsActivity::class.java)
//            intent.putExtra("playlistID", items[position].id)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MyViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener{
        override fun onClick(v: View?) {
            Toast.makeText(context, "ONCLICKITEM", Toast.LENGTH_SHORT).show()
        }

        val tvNamePlaylist = view.findViewById<TextView>(R.id.namePlayList)
        val tvOwnerPlaylist = view.findViewById<TextView>(R.id.ownerPlayList)
        val imagePlaylist = view.findViewById<ImageView>(R.id.imagePlaylist)

        fun bind(item: Item) {
            imagePlaylist.setOnClickListener(this)
            tvNamePlaylist.text = item.name
            tvOwnerPlaylist.text = "By ${item.owner.display_name}"
            if (item.images.isNotEmpty())
                imagePlaylist.loadUrl(item.images[0].url)
        }

        private fun ImageView.loadUrl(url: String) {
            try {
                Glide.with(context).load(url).error(R.drawable.image_broken).into(this)
            } catch (e: FileNotFoundException) {
                Toast.makeText(context, "The image is not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}