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
import app.example.spotifytest.data.UserModel
import app.example.spotifytest.ui.Tracks.SongsActivity
import com.bumptech.glide.Glide
import java.io.FileNotFoundException

class PlaylistAdapter(val TOKEN_ID: String, private val context: Context )
    : RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {

    private var dataList = mutableListOf<Item>()

    fun setListData(data: MutableList<Item>){
        dataList = data
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.playlist, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener{
            val intent = Intent(context, SongsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("playlistID", dataList[position].id)
            intent.putExtra("TOKEN_ID", TOKEN_ID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val tvNamePlaylist = view.findViewById<TextView>(R.id.namePlayList)
        private val tvOwnerPlaylist = view.findViewById<TextView>(R.id.ownerPlayList)
        private val imagePlaylist = view.findViewById<ImageView>(R.id.imagePlaylist)

        fun bind(item: Item) {
            tvNamePlaylist.text = item.name
            tvOwnerPlaylist.text = "By ${item.owner.display_name}"
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

