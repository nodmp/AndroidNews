package com.prsixe.anews.glance

import android.icu.text.SimpleDateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.util.Locale

object AndroidNesRepo {
    private var _currentInfo = MutableStateFlow<AndroidNewsInfo>(AndroidNewsInfo.Loading)

    val currentInfo: StateFlow<AndroidNewsInfo> get() = _currentInfo

    suspend fun updateArticles() {
        _currentInfo.value = AndroidNewsInfo.Loading
        try {
            var response = WanAndroidApi.wanAndroidService.getArticles()
            _currentInfo.value = AndroidNewsInfo.Success(
                response = response,
                updateTime = System.currentTimeMillis().toDate()

            )
        } catch (e: IOException) {
            _currentInfo.value = AndroidNewsInfo.Error
        }

    }
}

private fun Long.toDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    return format.format(this)
}
