package aculix.channelify.app.activity
import aculix.channelify.app.R
import aculix.channelify.app.fragment.VideoDetailsFragment
import aculix.channelify.app.utils.FullScreenHelper
import aculix.channelify.app.viewmodel.PausedModel
import aculix.channelify.app.viewmodel.VideoPlayerViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.android.synthetic.main.activity_video_player.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoPlayerActivity : AppCompatActivity(R.layout.activity_video_player) {



    companion object {
        const val VIDEO_ID = "video_id"
        lateinit var interstitialAd: InterstitialAd
        lateinit var adRequest: AdRequest


        fun startActivity(context: Context?, videoId: String) {

            MobileAds.initialize(context)
            adRequest = AdRequest.Builder().build()
            interstitialAd = InterstitialAd(context)
            interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
            interstitialAd.loadAd(adRequest)

            interstitialAd.setAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    if (interstitialAd.isLoaded) {
                        interstitialAd.show()
                    }
                }
            })
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra(VIDEO_ID, videoId)
            }
            context?.startActivity(intent)
        }
    }

    private val viewModel by viewModel<VideoPlayerViewModel>() // Lazy inject ViewModel

    lateinit var fullScreenHelper: FullScreenHelper
    lateinit var videoId: String
    private var videoElapsedTimeInSeconds = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fullScreenHelper = FullScreenHelper(this)
        videoId = intent.getStringExtra(VIDEO_ID)!!

        // Passing the videoId as argument to the start destination
        findNavController(R.id.navHostVideoPlayer).setGraph(
            R.navigation.video_player_graph,
            bundleOf(VideoDetailsFragment.VIDEO_ID to videoId)
        )

        initYouTubePlayer()
    }

    override fun onBackPressed() {
        if (ytVideoPlayerView.isFullScreen()) {
            ytVideoPlayerView.exitFullScreen()
        }
        else{

            PausedModel.setYourVariable(false)
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            super.onBackPressed()
        }
    }

    private fun initYouTubePlayer() {
        lifecycle.addObserver(ytVideoPlayerView)

        ytVideoPlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadOrCueVideo(lifecycle, videoId, 0f)
                addFullScreenListenerToPlayer()
                setupCustomActions(youTubePlayer)
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                videoElapsedTimeInSeconds = second
            }
        })


    }

    /**
     * Adds the forward and rewind action button to the Player
     */
    private fun setupCustomActions(youTubePlayer: YouTubePlayer) {
        ytVideoPlayerView.getPlayerUiController()
            .setCustomAction1(
                ContextCompat.getDrawable(this, R.drawable.ic_rewind)!!,
                View.OnClickListener {
                    videoElapsedTimeInSeconds -= 10
                    youTubePlayer.seekTo(videoElapsedTimeInSeconds)
                })
            .setCustomAction2(
                ContextCompat.getDrawable(this, R.drawable.ic_forward)!!,
                View.OnClickListener {
                    videoElapsedTimeInSeconds += 10
                    youTubePlayer.seekTo(videoElapsedTimeInSeconds)
                })
    }

    /**
     * Changes the orientation of the activity based on the
     * change of the player state (Full screen or not)
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun addFullScreenListenerToPlayer() {
        ytVideoPlayerView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                fullScreenHelper.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                fullScreenHelper.exitFullScreen()
            }
        })
    }
}
