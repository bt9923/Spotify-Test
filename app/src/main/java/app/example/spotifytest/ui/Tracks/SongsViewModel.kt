package app.example.spotifytest.ui.Tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.example.spotifytest.data.playlist.UserTracksFromPlaylist
import app.example.spotifytest.data.repo.Repo

class SongsViewModel: ViewModel() {
    fun fetchUserData(accessToken : String, playlistID: String) : LiveData<UserTracksFromPlaylist> {
        val repo = Repo(accessToken)
        val mutableList = MutableLiveData<UserTracksFromPlaylist>()

        repo.getTracksFromPlayList(playlistID).observeForever {
            mutableList.value = it
        }

        return mutableList
    }
}