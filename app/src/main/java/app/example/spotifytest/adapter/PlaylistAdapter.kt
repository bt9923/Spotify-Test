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
import app.example.spotifytest.data.Item
import com.bumptech.glide.Glide
import java.io.FileNotFoundException

class PlaylistAdapter(val items: List<Item>, val context: Context)
    : RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.playist, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
       return items.size
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tvNamePlaylist = view.findViewById<TextView>(R.id.namePlayList)
        val imagePlaylist = view.findViewById<ImageView>(R.id.imagePlaylist)

        fun bind(item: Item) {
            tvNamePlaylist.text = item.name
            if (item.images.isNotEmpty())
                imagePlaylist.loadUrl(item.images[0].url)
        }
        private fun ImageView.loadUrl(url: String) {
            try {
                Glide.with(context).load(url).error(R.drawable.image_broken).into(this)
            }catch (e : FileNotFoundException){
                Toast.makeText(context, "The image is not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

