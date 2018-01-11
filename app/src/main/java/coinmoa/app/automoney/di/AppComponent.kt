package coinmoa.app.automoney.di

import coinmoa.app.automoney.BrowserApp
import coinmoa.app.automoney.adblock.AssetsAdBlocker
import coinmoa.app.automoney.adblock.NoOpAdBlocker
import coinmoa.app.automoney.browser.BrowserPresenter
import coinmoa.app.automoney.browser.SearchBoxModel
import coinmoa.app.automoney.browser.TabsManager
import coinmoa.app.automoney.browser.activity.BrowserActivity
import coinmoa.app.automoney.browser.activity.ThemableBrowserActivity
import coinmoa.app.automoney.browser.fragment.BookmarksFragment
import coinmoa.app.automoney.browser.fragment.TabsFragment
import coinmoa.app.automoney.dialog.LightningDialogBuilder
import coinmoa.app.automoney.download.DownloadHandler
import coinmoa.app.automoney.download.LightningDownloadListener
import coinmoa.app.automoney.html.bookmark.BookmarkPage
import coinmoa.app.automoney.html.download.DownloadsPage
import coinmoa.app.automoney.html.history.HistoryPage
import coinmoa.app.automoney.html.homepage.StartPage
import coinmoa.app.automoney.network.NetworkObservable
import coinmoa.app.automoney.reading.activity.ReadingActivity
import coinmoa.app.automoney.search.SearchEngineProvider
import coinmoa.app.automoney.search.SuggestionsAdapter
import coinmoa.app.automoney.settings.activity.ThemableSettingsActivity
import coinmoa.app.automoney.settings.fragment.*
import coinmoa.app.automoney.utils.ProxyUtils
import coinmoa.app.automoney.view.LightningChromeClient
import coinmoa.app.automoney.view.LightningView
import coinmoa.app.automoney.view.LightningWebClient
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(activity: BrowserActivity)

    fun inject(fragment: BookmarksFragment)

    fun inject(fragment: BookmarkSettingsFragment)

    fun inject(builder: LightningDialogBuilder)

    fun inject(fragment: TabsFragment)

    fun inject(lightningView: LightningView)

    fun inject(activity: ThemableBrowserActivity)

    fun inject(fragment: LightningPreferenceFragment)

    fun inject(app: BrowserApp)

    fun inject(proxyUtils: ProxyUtils)

    fun inject(activity: ReadingActivity)

    fun inject(webClient: LightningWebClient)

    fun inject(activity: ThemableSettingsActivity)

    fun inject(listener: LightningDownloadListener)

    fun inject(fragment: PrivacySettingsFragment)

    fun inject(startPage: StartPage)

    fun inject(historyPage: HistoryPage)

    fun inject(bookmarkPage: BookmarkPage)

    fun inject(downloadsPage: DownloadsPage)

    fun inject(presenter: BrowserPresenter)

    fun inject(manager: TabsManager)

    fun inject(fragment: DebugSettingsFragment)

    fun inject(suggestionsAdapter: SuggestionsAdapter)

    fun inject(chromeClient: LightningChromeClient)

    fun inject(downloadHandler: DownloadHandler)

    fun inject(searchBoxModel: SearchBoxModel)

    fun inject(searchEngineProvider: SearchEngineProvider)

    fun inject(generalSettingsFragment: GeneralSettingsFragment)

    fun inject(networkObservable: NetworkObservable)

    fun provideAssetsAdBlocker(): AssetsAdBlocker

    fun provideNoOpAdBlocker(): NoOpAdBlocker

}
