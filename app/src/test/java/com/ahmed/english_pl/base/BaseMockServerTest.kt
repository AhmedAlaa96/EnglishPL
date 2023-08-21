package com.ahmed.english_pl.base

import com.ahmed.english_pl.data.local.ILocalDataSource
import com.ahmed.english_pl.data.remote.IRemoteDataSource
import com.ahmed.english_pl.data.remote.RemoteDataSource
import com.ahmed.english_pl.data.shared_prefrences.IPreferencesDataSource
import com.ahmed.english_pl.retrofit.ApiInterface
import com.ahmed.english_pl.utils.connection_utils.IConnectionUtils
import com.ahmed.english_pl.utils.MockResponseFileReader
import com.ahmed.english_pl.utils.TestConstants
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.kotlin.whenever
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


abstract class BaseMockServerTest {

    private val server: MockWebServer = MockWebServer()

    protected lateinit var remoteDataSource: IRemoteDataSource

    @Mock
    protected lateinit var connectionUtils: IConnectionUtils

    @Mock
    protected lateinit var preferencesDataSource: IPreferencesDataSource

    @Mock
    protected lateinit var localDataSource: ILocalDataSource


    @Before
    open fun setup() {
        server.start(TestConstants.Constants.MOCK_WEBSERVER_PORT)
        remoteDataSource = RemoteDataSource(
            Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        )
        whenever(connectionUtils.isConnected).thenReturn(TestConstants.Constants.CONNECTION_TRUE)
    }

    private fun getBaseUrl(): HttpUrl = server.url(TestConstants.Constants.BASE_NETWORK_URL)

    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) = server.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(MockResponseFileReader(fileName).content)
    )


    @After
    open fun tearDown() {
        server.shutdown()
    }
}