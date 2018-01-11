package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.GOOGLE_SEARCH

/**
 * The Google search engine.
 *
 * See https://www.google.com/images/srpr/logo11w.png for the icon.
 */
class GoogleSearch : BaseSearchEngine(
        "file:///android_asset/google.png",
        GOOGLE_SEARCH,
        R.string.search_engine_google
)
