package com.ahmed.english_pl.utils

import com.ahmed.english_pl.data.models.dto.*


object TestConstants {

    object Constants {
        const val MOCK_WEBSERVER_PORT = 8000
        const val BASE_NETWORK_URL = "/"
        const val CONNECTION_TRUE = true
        const val CONNECTION_FALSE = false
    }


    object JsonPaths {
        const val MATCHES_SUCCESS =
            "matches/matches_success.json"
        const val MATCHES_NO_INTERNET_CONNECTION =
            "matches/matches_no_internet_connection.json"
    }

    object Response {
        val MatchResponse = MatchResponse(
            count = 1,
            competition = Competition(
                id = 2021,
                area = Area(2021, "England"),
                name = "Premier League",
                code = "PL",
                plan = "TIER_ONE",
                lastUpdated = "2022-03-20T08:58:54Z"
            ),
            matches = arrayListOf(
                Match(
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED"
                )
            )
        )
    }

}