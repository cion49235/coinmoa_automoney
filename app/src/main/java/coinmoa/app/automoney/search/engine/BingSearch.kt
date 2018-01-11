package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.BING_SEARCH

/**
 * The Bing search engine.
 *
 * See http://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Bing_logo_%282013%29.svg/500px-Bing_logo_%282013%29.svg.png
 * for the icon.
 */
class BingSearch : BaseSearchEngine(
        "file:///android_asset/bing.png",
        BING_SEARCH,
        R.string.search_engine_bing
)
