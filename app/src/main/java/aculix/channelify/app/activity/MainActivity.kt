package aculix.channelify.app.activity
import aculix.channelify.app.R
import aculix.channelify.app.viewmodel.PausedModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var startAppAd: StartAppAd
    private var flag =true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);

        StartAppSDK.enableReturnAds(false);

        val navController = findNavController(R.id.navHostFragment)
        bottomNavView.setupWithNavController(navController)

    }


    override fun onResume() {

        Log.d("dk", "onResume: ")
        super.onResume()
        if (PausedModel.getYourVariable()) {

            flag = false
            PausedModel.setYourVariable(false)
            startAppAd = StartAppAd(this)

            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, object : AdEventListener {
                override fun onReceiveAd(p0: Ad) {
                    // The ad has been received, now show it
                    startAppAd.showAd()

                }

                override fun onFailedToReceiveAd(p0: Ad?) {

                }


            })
        }
    }


    override fun onPause() {
        Log.d("dk", "onPause: "+PausedModel.getYourVariable())
        super.onPause()
        if(flag){
            PausedModel.setYourVariable(true)
        }else{
            flag = true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PausedModel.setYourVariable(false)
    }



}



