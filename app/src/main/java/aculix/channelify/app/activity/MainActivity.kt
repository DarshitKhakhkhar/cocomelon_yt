package aculix.channelify.app.activity
import aculix.channelify.app.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var interstitialAd2: InterstitialAd
    private lateinit var adRequest2: AdRequest
    var isPaused = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("dk", "onCreate: ")

        val navController = findNavController(R.id.navHostFragment)
        bottomNavView.setupWithNavController(navController)

    }

    override fun onResume() {

        Log.d("dk", "onResume: ")
        super.onResume()
        if (isPaused) {
            MobileAds.initialize(this)
            adRequest2 = AdRequest.Builder().build()
            interstitialAd2 = InterstitialAd(this)
            interstitialAd2.adUnitId = "ca-app-pub-3940256099942544/1033173712"

            interstitialAd2.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    if (interstitialAd2.isLoaded && isPaused) {
                        interstitialAd2.show()
                    }
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    isPaused=false
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Log.d("dk", "onAdFailedToLoad: $errorCode")
                }
            }

            interstitialAd2.loadAd(adRequest2)
        }
    }

    override fun onPause() {

        Log.d("dk", "onPause: ")
        super.onPause()
        isPaused = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isPaused = false
    }

}



