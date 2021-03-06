package coinmoa.app.automoney.browser.activity

import coinmoa.app.automoney.BrowserApp
import coinmoa.app.automoney.R
import coinmoa.app.automoney.preference.PreferenceManager
import coinmoa.app.automoney.utils.ThemeUtils
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

abstract class ThemableBrowserActivity : AppCompatActivity() {

    // TODO reduce protected visibility
    @Inject protected lateinit var preferences: PreferenceManager

    private var themeId: Int = 0
    private var showTabsInDrawer: Boolean = false
    private var shouldRunOnResumeActions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        BrowserApp.appComponent.inject(this)
        themeId = preferences.useTheme
        showTabsInDrawer = preferences.getShowTabsInDrawer(!isTablet)

        // set the theme
        if (themeId == 1) {
            setTheme(R.style.Theme_DarkTheme)
        } else if (themeId == 2) {
            setTheme(R.style.Theme_BlackTheme)
        }
        super.onCreate(savedInstanceState)

        resetPreferences()
    }

    private fun resetPreferences() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (preferences.useBlackStatusBar) {
                window.statusBarColor = Color.BLACK
            } else {
                window.statusBarColor = ThemeUtils.getStatusBarColor(this)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && shouldRunOnResumeActions) {
            shouldRunOnResumeActions = false
            onWindowVisibleToUserAfterResume()
        }
    }

    /**
     * Called after the activity is resumed
     * and the UI becomes visible to the user.
     * Called by onWindowFocusChanged only if
     * onResume has been called.
     */
    protected open fun onWindowVisibleToUserAfterResume() {

    }

    override fun onResume() {
        super.onResume()
        resetPreferences()
        shouldRunOnResumeActions = true
        val themePreference = preferences.useTheme
        val drawerTabs = preferences.getShowTabsInDrawer(!isTablet)
        if (themeId != themePreference || showTabsInDrawer != drawerTabs) {
            restart()
        }
    }

    protected val isTablet: Boolean
        get() = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE

    protected fun restart() {
        finish()
        startActivity(Intent(this, javaClass))
    }
}
