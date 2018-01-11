package coinmoa.app.automoney

import coinmoa.app.automoney.browser.activity.BrowserActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import com.anthonycr.bonsai.Completable
import kr.co.inno.autocash.service.AutoLoginServiceActivity
import kr.co.inno.autocash.service.AutoServiceActivity
import android.widget.RelativeLayout
import com.admixer.*
import gun0912.tedadhelper.TedAdHelper
import gun0912.tedadhelper.banner.OnBannerAdListener
import gun0912.tedadhelper.banner.TedAdBanner


class MainActivity : BrowserActivity() , AdViewListener {

    @Suppress("DEPRECATION")
    public override fun updateCookiePreference(): Completable {
        return Completable.create { subscriber ->
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.createInstance(this@MainActivity)
            }
            cookieManager.setAcceptCookie(preferences.cookiesEnabled)
            subscriber.onComplete()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "kh19o528")
        AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4092414235173954/3785554198")
        AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4092414235173954/9668436264")
        auto_service()
        addBannerView()
    }
    var ad_layout: RelativeLayout? = null
    fun addBannerView() {
        val adInfo = AdInfo("kh19o528")
        adInfo.isTestMode = false
        val adView = com.admixer.AdView(this)
        adView.setAdInfo(adInfo, this)
        adView.setAdViewListener(this)
        adView.setAdAnimation(AdView.AdAnimation.TopSlide)
        ad_layout = findViewById<View>(R.id.ad_layout) as RelativeLayout
        if (ad_layout != null) {
            val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            ad_layout!!.addView(adView, params)
        }
    }


    /*internal var facebookBanner: com.facebook.ads.AdView? = null
    fun addBannerView() {
        ad_layout = findViewById<View>(R.id.ad_layout) as RelativeLayout
        TedAdBanner.showBanner(ad_layout, getString(R.string.ADMOB_KEY_BACKPRESS), getString(R.string.ADMOB_KEY_BACKPRESS), TedAdHelper.AD_FACEBOOK, object : OnBannerAdListener {
            override fun onError(errorMessage: String) {

            }

            override fun onLoaded(adType: Int) {

            }

            override fun onAdClicked(adType: Int) {

            }

            override fun onFacebookAdCreated(facebookBanner: com.facebook.ads.AdView) {
                this@MainActivity.facebookBanner = facebookBanner
            }

        })
    }*/

    private fun auto_service() {
        val intent = Intent(this, AutoServiceActivity::class.java)
        stopService(intent)
        startService(intent)
    }

    private fun auto_login_service() {
        val intent = Intent(this, AutoLoginServiceActivity::class.java)
        stopService(intent)
        startService(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNewIntent(intent: Intent) {
        if (isPanicTrigger(intent)) {
            panicClean()
        } else {
            handleNewIntent(intent)
            super.onNewIntent(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        saveOpenTabs()
    }

    override fun updateHistory(title: String?, url: String) {
        addItemToHistory(title, url)
    }

    override val isIncognito = false

    override fun closeActivity() {
        closeDrawers {
            performExitCleanUp()
            moveTaskToBack(true)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.isCtrlPressed) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_P ->
                    // Open a new private window
                    if (event.isShiftPressed) {
                        startActivity(Intent(this, IncognitoActivity::class.java))
                        overridePendingTransition(R.anim.slide_up_in, R.anim.fade_out_scale)
                        return true
                    }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onClickedAd(arg0: String, arg1: com.admixer.AdView) {}

    override fun onFailedToReceiveAd(arg0: Int, arg1: String,
                                     arg2: com.admixer.AdView) {
    }

    override fun onReceivedAd(arg0: String, arg1: com.admixer.AdView) {}
}
