package com.calibraint.picoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.calibraint.picoftheday.databinding.ActivityMainBinding
import com.calibraint.picoftheday.utils.Constants
import com.calibraint.picoftheday.utils.HelperFunctions
import com.calibraint.picoftheday.utils.HelperFunctions.showToast
import com.calibraint.picoftheday.utils.MediaType
import com.calibraint.picoftheday.utils.SharedPreferences
import com.calibraint.picoftheday.viewmodels.MainViewModel
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var listener: AbstractYouTubePlayerListener
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainViewModel
    private var playerView: YouTubePlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)
        initView()
    }

    private fun initPlayer(media: String?) {
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(0)
            .autoplay(0)
            .rel(1)
            .modestBranding(1)
            .fullscreen(0)
            .build()
        listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val defaultPlayerUiController =
                    DefaultPlayerUiController(playerView!!, youTubePlayer)
                defaultPlayerUiController.apply {
                    showFullscreenButton(false)
                    showSeekBar(false)
                    showVideoTitle(false)
                    showYouTubeButton(false)
                    showCurrentTime(false)
                    showDuration(false)
                    showMenuButton(false)
                }
                playerView?.setCustomPlayerUi(defaultPlayerUiController.rootView)
                media?.let {
                    youTubePlayer.loadOrCueVideo(
                        lifecycle,
                        HelperFunctions.extractVideoId(media),
                        0f
                    )
                }
            }
        }
        playerView = binding.videoView
        lifecycle.addObserver(playerView!!)
        playerView?.initialize(listener, options)
    }

    private fun initView() {
        viewmodel.getCachedData(this)
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewmodel.getData(
                    onCompletion = {
                        swipeRefresh.isRefreshing = false
                        showToast(root, getString(R.string.refresh_success))
                    },
                    onError = { error -> showToast(root, error) }
                )
            }
            viewmodel.presentData.observe(this@MainActivity) { data ->
                data.title?.let {
                    tvNoData.isVisible = false
                    clRoot.isVisible = true
                    data.apply {
                        val headerText = getString(R.string.image_of_the_day, mediaType)
                        tvHeader.text = HelperFunctions.capitalize(headerText)
                        val json = Gson().toJson(data)
                        SharedPreferences.updatePrefs(this@MainActivity, Constants.photoData, json)
                        tvTitle.text = title
                        mediaType?.let { setView(it, media) }
                        date?.let { tvDate.text = getString(R.string.date_posted, it) }
                        tvDescription.text = description
                    }
                } ?: run {
                    tvNoData.isVisible = true
                }
            }
        }
    }

    /** Function to update view based on photo data */
    private fun setView(mediaType: String, media: String?) {
        binding.apply {
            ivImage.isVisible = (mediaType == MediaType.IMAGE.type)
            videoView.isVisible = (mediaType != MediaType.IMAGE.type)
            when (mediaType) {
                MediaType.IMAGE.type -> {
                    Picasso.get()
                        .load(media)
                        .into(ivImage)
                }

                else -> {
                    if (playerView == null) initPlayer(media)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getData(
            { /*Do nothing*/ },
            { error -> showToast(binding.root, error) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView?.release()
        lifecycle.removeObserver(playerView!!)
        playerView = null
    }
}
