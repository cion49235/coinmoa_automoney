package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.STARTPAGE_SEARCH

/**
 * The StartPage search engine.
 */
class StartPageSearch : BaseSearchEngine(
        "file:///android_asset/startpage.png",
        STARTPAGE_SEARCH,
        R.string.search_engine_startpage
)
