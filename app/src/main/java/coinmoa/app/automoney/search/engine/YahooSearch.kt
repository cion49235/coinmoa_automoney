package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.YAHOO_SEARCH

/**
 * The Yahoo search engine.
 *
 * See http://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Yahoo%21_logo.svg/799px-Yahoo%21_logo.svg.png
 * for the icon.
 */
class YahooSearch : BaseSearchEngine(
        "file:///android_asset/yahoo.png",
        YAHOO_SEARCH,
        R.string.search_engine_yahoo
)
