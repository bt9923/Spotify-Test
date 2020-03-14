package app.example.spotifytest.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.example.spotifytest.data.user.UserModel
import app.example.spotifytest.data.playlist.UserPlaylistModel
import app.example.spotifytest.data.repo.Repo

class MainViewModel : ViewModel() {

    fun fetchUserData(accessToken : String) : LiveData<UserModel>{
        val repo = Repo(accessToken)
        val mutableList = MutableLiveData<UserModel>()

        repo.getUserData().observeForever {
            mutableList.value = it
        }

        return mutableList
    }

    fun fetchGetPlayList(accessToken : String) : LiveData<UserPlaylistModel>{
        val repo = Repo(accessToken)
        val mutableList = MutableLiveData<UserPlaylistModel>()

        repo.getPlayList().observeForever {
            mutableList.value = it
        }

        return mutableList
    }

}