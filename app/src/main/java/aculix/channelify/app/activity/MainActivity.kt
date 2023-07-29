package aculix.channelify.app.activity
import aculix.channelify.app.R
import aculix.channelify.app.viewmodel.PausedModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navController = findNavController(R.id.navHostFragment)
        bottomNavView.setupWithNavController(navController)

    }

    override fun onPause() {
        super.onPause()
        PausedModel.setYourVariable(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    PausedModel.setYourVariable(false)
    }



}



