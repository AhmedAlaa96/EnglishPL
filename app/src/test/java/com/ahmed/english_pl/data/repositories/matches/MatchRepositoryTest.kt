package com.ahmed.english_pl.data.repositories.matches

import com.ahmed.english_pl.base.BaseRepositoryTest
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.data.repositories.matches_list.IMatchesListRepository
import com.ahmed.english_pl.data.repositories.matches_list.MatchesListRepository
import com.ahmed.english_pl.utils.TestConstants
import com.ahmed.english_pl.utils.TestConstants.Constants.CONNECTION_FALSE
import com.ahmed.english_pl.utils.TestConstants.JsonPaths.MATCHES_NO_INTERNET_CONNECTION
import com.ahmed.english_pl.utils.TestConstants.JsonPaths.MATCHES_SUCCESS
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class MatchRepositoryTest : BaseRepositoryTest() {

    private lateinit var matchesListRepository: IMatchesListRepository

    @Before
    override fun setup() {
        super.setup()
        matchesListRepository = MatchesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            preferencesDataSource
        )
    }

    @Test
    fun `test match success flow and return success`() = runBlocking {
        mockNetworkResponseWithFileContent(
            MATCHES_SUCCESS,
            HttpURLConnection.HTTP_OK
        )

        val expectedStatus = Status.Success(TestConstants.Response.MatchResponse)

        var actualStatus: Status<MatchResponse> = Status.Idle()

        matchesListRepository.getMatchesList().collect {
            actualStatus = Status.CopyStatus(it, it.data)
        }

        Assert.assertEquals(expectedStatus.statusCode, actualStatus.statusCode)
        Assert.assertEquals(expectedStatus.data?.matches?.size, actualStatus.data?.matches?.size)
    }

    @Test
    fun `test match failed flow and return no internet`() = runBlocking {
        whenever(connectionUtils.isConnected).thenReturn(CONNECTION_FALSE)
        mockNetworkResponseWithFileContent(
            MATCHES_NO_INTERNET_CONNECTION,
            HttpURLConnection.HTTP_OK
        )

        val expectedStatus = Status.NoNetwork<MatchResponse>(error = "No network")

        var actualStatus: Status<MatchResponse> = Status.Idle()

        matchesListRepository.getMatchesList().collect {
            actualStatus = Status.CopyStatus(it, it.data)
        }

        Assert.assertEquals(expectedStatus.statusCode, actualStatus.statusCode)
    }
}