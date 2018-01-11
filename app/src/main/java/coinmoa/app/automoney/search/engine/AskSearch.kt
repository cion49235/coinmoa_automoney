package coinmoa.app.automoney.search.engine

import coinmoa.app.automoney.R
import coinmoa.app.automoney.constant.ASK_SEARCH

/**
 * The Ask search engine.
 */
class AskSearch : BaseSearchEngine(
        "file:///android_asset/ask.png",
        ASK_SEARCH,
        R.string.search_engine_ask
)
