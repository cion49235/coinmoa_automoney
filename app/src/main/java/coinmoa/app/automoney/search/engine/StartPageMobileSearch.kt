package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.STARTPAGE_MOBILE_SEARCH

/**
 * The StartPage mobile search engine.
 */
class StartPageMobileSearch : BaseSearchEngine(
        "file:///android_asset/startpage.png",
        STARTPAGE_MOBILE_SEARCH,
        R.string.search_engine_startpage_mobile
)
