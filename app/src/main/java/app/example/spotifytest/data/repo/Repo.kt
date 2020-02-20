package app.example.spotifytest.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.example.spotifytest.BuildConfig.USER_ID
import app.example.spotifytest.api.UserApi
import app.example.spotifytest.data.UserModel
import app.example.spotifytest.data.UserPlaylistModel
import app.example.spotifytest.data.UserTracksFromPlaylist
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repo(accessToken: String) {
    private val TAG = "MainActivity"
    private var userApi: UserApi
    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original = chain.request()

                val request = original.newBuilder()
                    .header( "Authorization", "Bearer $accessToken")
                    .method(original.method, original.body)
                    .build()

                return chain.proceed(request)
            }
        })
        val  client = httpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        userApi = retrofit.create(UserApi::class.java)
    }

    fun getUserData(): LiveData<UserModel>{
        val mutableData = MutableLiveData<UserModel>()

        val callUserModel = userApi.getUserData(USER_ID)
        callUserModel.enqueue( object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d(TAG, "OnFailure $t")
//                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    when (response.code()) {
                        200 -> {
                            mutableData.value = response.body()
                            Log.d(TAG, "Successful")
//                            textViewName.text = response.body()?.display_name
//                            textViewFollowers.text = response.body()?.followers?.total.toString()

//                            if (response.body()?.images?.isNotEmpty()!!)
//                                Glide.with(applicationContext)
//                                    .load(response.body()?.images!![0].url)
//                                    .into(imgUploadPhoto)
//                            Log.d("MAINACTIVITY.COM XDDDDD", response.body()?.)
                        }
                        401 -> {
                            Log.d(TAG, "Unauthorized")
//                            somethingWentWrong.visibility = View.VISIBLE
//                            recyclerViewPlaylist.visibility = View.GONE
//                            Toast.makeText(
//                                applicationContext,
//                                "The request requires user authentication",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        403 -> {
                            Log.d(TAG, "Forbidden")
//                            somethingWentWrong.visibility = View.VISIBLE
//                            recyclerViewPlaylist.visibility = View.GONE
//                            Toast.makeText(
//                                applicationContext, resources.getString(
//                                    R.string.failed_in_the_server
//                                ), Toast.LENGTH_LONG
//                            ).show()
                        }
                    }
                } else {
//                    somethingWentWrong.visibility = View.VISIBLE
//                    recyclerViewPlaylist.visibility = View.GONE
//                    Toast.makeText(
//                        applicationContext,
//                        resources.getString(R.string.failed_in_the_server),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }

        })

        return mutableData
    }

    fun getPlayList(): LiveData<UserPlaylistModel> {
        val mutableData = MutableLiveData<UserPlaylistModel>()

        val callPlaylist = userApi.getUserPlaylist(USER_ID)
        callPlaylist.enqueue( object : Callback<UserPlaylistModel>{
            override fun onFailure(call: Call<UserPlaylistModel>, t: Throwable) {
                Log.d(TAG, t.toString())
//                somethingWentWrong.visibility = View.VISIBLE
//                recyclerViewPlaylist.visibility = View.GONE
//                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserPlaylistModel>, response: Response<UserPlaylistModel>) {
                Log.d(TAG, "Successful OnResponse GetUserPlayList")
//                somethingWentWrong.visibility = View.GONE
//                recyclerViewPlaylist.visibility = View.VISIBLE
//                recyclerViewPlaylist.layoutManager = LinearLayoutManager(applicationContext)
//                recyclerViewPlaylist.setHasFixedSize(true)
//                val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
//                itemDecoration.setDrawable(
//                    ContextCompat.getDrawable(applicationContext,
//                    R.drawable.item_decorator
//                )!!)

//                recyclerViewPlaylist.addItemDecoration(itemDecoration)

                if (response.isSuccessful) {
                    when (response.code()) {
                        200 -> {
                            mutableData.value = response.body()
//                            recyclerViewPlaylist.adapter = PlaylistAdapter(response.body()!!.items, TOKEN_ID, applicationContext)
                            Log.d(TAG, "Successful")
//                            textView.text = response.body()?.items!![0].name
//                            followers.text = response.body()?.followers?.total.toString()
                        }
//                        401 -> {
//                            Log.d(TAG, "Unauthorized")
//                            somethingWentWrong.visibility = View.VISIBLE
//                            recyclerViewPlaylist.visibility = View.GONE
//                            Toast.makeText(applicationContext, "The request requires user authentication", Toast.LENGTH_SHORT).show()
//                        }
//                        403 ->{
//                            Log.d(TAG, "Forbidden")
//                            somethingWentWrong.visibility = View.VISIBLE
//                            recyclerViewPlaylist.visibility = View.GONE
//                            Toast.makeText(applicationContext, resources.getString(
//                                R.string.failed_in_the_server
//                            ), Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }else{
//                    Log.d(TAG, "$response")
//                    somethingWentWrong.visibility = View.VISIBLE
//                    recyclerViewPlaylist.visibility = View.GONE
//                    Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
//                }
                    }
                }
            }
        })

        return mutableData
    }

    fun getTracksFromPlayList(playlistID: String?): LiveData<UserTracksFromPlaylist> {
        val mutableData = MutableLiveData<UserTracksFromPlaylist>()

        val callUserModel = userApi.getUserTracksFromPlaylist(USER_ID, playlistID!!)

        callUserModel.enqueue(object  : Callback<UserTracksFromPlaylist>{
            override fun onFailure(call: Call<UserTracksFromPlaylist>, t: Throwable) {
                Log.d("SongActivity", "$t")
//                Toast.makeText(applicationContext, resources.getString(R.string.failed_in_the_server), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<UserTracksFromPlaylist>,
                response: Response<UserTracksFromPlaylist>) {
                mutableData.value = response.body()
            }
        })

        return mutableData
    }
}