package coinmoa.app.automoney.view

import android.os.Handler
import android.os.Looper

/**
 * Common [Handler] used by the application.
 */
object Handlers {

    init {
        if (Looper.getMainLooper() == null) {
            Looper.prepareMainLooper()
        }
    }

    @JvmField
    val MAIN: Handler = Handler(Looper.getMainLooper())

}