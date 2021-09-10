package com.authme.sdk.sample.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authme.lib.AuthMe
import com.authme.library.liveness.LivenessResult
import com.authme.library.ocr.template.OCRResult
import com.authme.sdk.sample.APP
import com.authme.sdk.sample.manager.DemoManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

private const val clientIdProd = "a0eedab298f144119919373b2e468f65"
private const val secretProd = "ukZ26CBfMFdmo8TQRy9jJpPfgWqTAzwB"

private const val clientIdStaging = "d7e8cf57499b4c348fbfb4b3e92d0182"
private const val secretStaging = "oy9rDBCv5trf6BJe"

private const val demoId = "972ff0b06ffc427a825cbd0375ad6463"
private const val demoSecret = "Rf6SPdHL3pPvtHT5kEazfqkLLD3he9jG"

private const val clientId = clientIdStaging
private const val clientSecret = secretStaging

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null

    var livenessResult: LivenessResult? = null
    var ocrResult: OCRResult? = null

    val txtResult = MutableLiveData<String>()
    val showLoading = MutableLiveData<Boolean>()

    private val repository = MainRepository()

    private val _customerId = MutableLiveData("test${System.currentTimeMillis()}")
    val customerId: LiveData<String> = _customerId

    fun updateCustomerId() {
        _customerId.value = "test${System.currentTimeMillis()}"
        DemoManager.instance.reset()
        initSDK()
    }

    fun initSDK() {
        viewModelScope.launch {
            showLoading.postValue(true)
            var token: String = ""
            try {
                Timber.d("customerID: ${customerId.value}")
                token = repository.getToken(
                    clientID = clientId,
                    secret = clientSecret,
                    customerID = customerId.value!!
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            AuthMe.init(APP.instance, token) {
                Locale.TRADITIONAL_CHINESE
            }
            showLoading.postValue(false)
        }
    }

    fun idStatus(): Flow<String> {
        return DemoManager.instance.idConfirmed.map {
            return@map if (it) "✅ 已完成" else "❌ 未完成"
        }
    }

    fun ocrStatus(): Flow<String> {
        return DemoManager.instance.ocrConfirmed.map {
            return@map if (it) "✅ 已完成" else "❌ 未完成"
        }
    }

    fun livenessStatus(): Flow<String> {
        return DemoManager.instance.livenessResult.map {
            return@map if (it != null) "✅ 已完成" else "❌ 未完成"
        }
    }

    fun compareStatus(): Flow<String> {
        return DemoManager.instance.compareDone.map {
            return@map if (it) "✅ 已完成" else "❌ 未完成"
        }
    }

    fun compareEnable(): Flow<Boolean> {
        return DemoManager.instance.idConfirmed.combine(DemoManager.instance.ocrConfirmed) { a, b ->
            a && b
        }.combine(DemoManager.instance.livenessResult) { a, b ->
            a && b != null
        }
    }

    fun compareDone(): Flow<Boolean> {
        return DemoManager.instance.compareDone
    }

}
