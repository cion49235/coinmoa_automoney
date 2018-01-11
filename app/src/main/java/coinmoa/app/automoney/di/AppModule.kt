package coinmoa.app.automoney.di

import coinmoa.app.automoney.BrowserApp
import coinmoa.app.automoney.database.bookmark.BookmarkDatabase
import coinmoa.app.automoney.database.bookmark.BookmarkModel
import coinmoa.app.automoney.database.downloads.DownloadsDatabase
import coinmoa.app.automoney.database.downloads.DownloadsModel
import coinmoa.app.automoney.database.history.HistoryDatabase
import coinmoa.app.automoney.database.history.HistoryModel
import coinmoa.app.automoney.download.DownloadHandler
import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import net.i2p.android.ui.I2PAndroidHelper
import javax.inject.Singleton

@Module
class AppModule(private val app: BrowserApp) {

    @Provides
    fun provideApplication(): Application = app

    @Provides
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideBookmarkModel(): BookmarkModel = BookmarkDatabase(app)

    @Provides
    @Singleton
    fun provideDownloadsModel(): DownloadsModel = DownloadsDatabase(app)

    @Provides
    @Singleton
    fun providesHistoryModel(): HistoryModel = HistoryDatabase(app)

    @Provides
    @Singleton
    fun provideDownloadHandler(): DownloadHandler = DownloadHandler()

    @Provides
    @Singleton
    fun provideI2PAndroidHelper(): I2PAndroidHelper = I2PAndroidHelper(app.applicationContext)

}
