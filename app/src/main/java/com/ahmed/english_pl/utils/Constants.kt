package com.ahmed.english_pl.utils

object Constants {
    object SharedPreference {
        const val SHARED_PREF_NAME = "my_shared_pref"
        const val MATCHES_LIST = "MATCHES_LIST"
    }

    object General {
        const val EMPTY_TEXT = ""
        const val DASH_TEXT = "-"
        const val FROM_DATE_AFTER_TO_DATE = "FROM_DATE_AFTER_TO_DATE"
    }

    object FragmentListeners {
        const val FILTER_KEY = "FILTER_KEY"
        const val FILTER_BUNDLE = "FILTER_BUNDLE"
        const val STATUS_KEY = "STATUS_KEY"
        const val STATUS_BUNDLE = "STATUS_BUNDLE"

    }


    object RecycleType {
        const val LAYOUT_MANAGER_LINEAR_HORIZONTAL = 1
        const val LAYOUT_MANAGER_LINEAR_VERTICAL = 2
    }

    object Network {
        const val CONNECT_TIMEOUT = 5L
        const val READ_TIMEOUT = 5L
        const val WRITE_TIMEOUT = 5L
    }

    object URL {
        const val BASE_URL = "http://api.football-data.org/v2/competitions/2021/"
        const val GET_MATCHES = "matches"
    }

    object Headers {
        const val X_AUTH_TOKEN = "X-Auth-Token"
        const val X_AUTH_TOKEN_VALUE = "4aabf4ea7664444c9794b68f5af117cb"
    }

    object ViewsTags {
        const val RECYCLER_VIEW_MATCHES = "RECYCLER_VIEW_MATCHES"
    }
}