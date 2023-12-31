package aculix.channelify.app

//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.RequestConfiguration
import aculix.channelify.app.di.*
import aculix.channelify.app.sharedpref.AppPref
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import saschpe.android.customtabs.CustomTabsActivityLifecycleCallbacks
import timber.log.Timber

class Channelify : Application() {

    companion object {
         var isAdEnabled = true
    }

    override fun onCreate() {
        super.onCreate()
        isAdEnabled = resources.getBoolean(R.bool.enable_ads)

        initializeKotpref()
        setThemeMode()
        initializeTimber()
        initializeKoin()
        initializeCustomTabs()

//        if (resources.getBoolean(R.bool.enable_ads)) initializeAdmob()
    }

    private fun setThemeMode() {
        if (AppPref.isLightThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeKotpref() {
        Kotpref.init(this)
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger()
            androidContext(this@Channelify)
            modules(
                listOf(
                    appModule,
                    homeModule,
                    videoPlayerModule,
                    commentsModule,
                    videoDetailsModule,
                    commentRepliesModule,
                    playlistsModule,
                    playlistVideosModule,
                    favoritesModule,
                    searchModule,
                    aboutModule
                )
            )
        }
    }

    private fun initializeCustomTabs() {
        registerActivityLifecycleCallbacks(CustomTabsActivityLifecycleCallbacks())
    }
}