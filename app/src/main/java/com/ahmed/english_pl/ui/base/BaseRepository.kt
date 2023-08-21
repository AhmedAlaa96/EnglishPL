package com.ahmed.english_pl.ui.base

import android.util.Log
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.remote.IRemoteDataSource
import com.ahmed.english_pl.data.shared_prefrences.IPreferencesDataSource
import com.ahmed.english_pl.utils.Utils
import com.ahmed.english_pl.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.net.SocketException

abstract class BaseRepository(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    private val mIPreferencesDataSource: IPreferencesDataSource
) : IBaseRepository {


    protected val isConnected: Boolean
        get() {
            return connectionUtils.isConnected
        }


    fun <T> safeApiCalls(apiCall: suspend () -> T): Flow<Status<T>> {
        return flow {
            if (isConnected) {
                try {
                    emit(Status.Success(apiCall.invoke()))
                } catch (throwable: Throwable) {
                    Utils.printStackTrace(throwable)
                    when (throwable) {
                        is HttpException -> {
                            emit(Status.Error(error = throwable.message)) as Unit
                        }
                        is SocketException -> {
                            emit(Status.Error(error = throwable.message)) as Unit
                        }
                        else -> {
                            emit(Status.Error(error = throwable.message)) as Unit
                        }
                    }
                }
            } else {
                emit(Status.NoNetwork(error = "No Network")) as Unit
            }

        }.flowOn((Dispatchers.IO)) as Flow<Status<T>>
    }
}
