package app.example.spotifytest.ui.Tracks.Player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.example.spotifytest.data.repo.Repo
import app.example.spotifytest.data.track.TrackModel

class PlayerViewModel: ViewModel() {
    fun fetchGetTrack(accessToken : String, trackID: String) : LiveData<TrackModel> {
        val repo = Repo(accessToken)
        val mutableList = MutableLiveData<TrackModel>()

        repo.getTrackByID(trackID).observeForever {
            mutableList.value = it
        }

        return mutableList
    }
}