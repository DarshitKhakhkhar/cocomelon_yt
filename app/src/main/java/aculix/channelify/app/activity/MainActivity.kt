package aculix.channelify.app.activity
import aculix.channelify.app.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    lateinit var interstitialAd: InterstitialAd
    lateinit var adRequest: AdRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are
        // initializing our mobile ads.
        MobileAds.initialize(this)

        // on below line we are
        // initializing our ad request.
        adRequest = AdRequest.Builder().build()

        // on below line we are
        // initializing our interstitial ad.
        interstitialAd = InterstitialAd(this)

        // on below line we are setting ad
        // unit id for our interstitial ad.
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
//        interstitialAd.adUnitId = "ca-app-pub-9210368304205371/2753658138"

        // on below line we are loading
        // our ad with ad request
        interstitialAd.loadAd(adRequest)

        // on below line we are setting ad
        // listener for our interstitial ad.
        interstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                // on below line we are calling display
                // ad function to display interstitial ad.
                displayInterstitialAd(interstitialAd)
            }
        })

        val navController = findNavController(R.id.navHostFragment)
        bottomNavView.setupWithNavController(navController)

    }


    private fun displayInterstitialAd(interstitialAd: InterstitialAd) {
        // on below line we are
        // checking if the ad is loaded
        if (interstitialAd.isLoaded) {
            // if the ad is loaded we are displaying
            // interstitial ad by calling show method.
            interstitialAd.show()
        }
    }
}
