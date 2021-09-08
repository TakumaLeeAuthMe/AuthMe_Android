/**
 * Created by Leon Lee on 2021/7/27.
 */

package com.authme.sdk.sample.manager

import com.authme.library.liveness.LivenessResult
import com.authme.library.ocr.template.OCRResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class DemoManager {

    val idResult = MutableStateFlow<OCRResult?>(null)
    val idConfirmed = MutableStateFlow(false)

    val ocrResult = MutableStateFlow<OCRResult?>(null)
    val ocrConfirmed = MutableStateFlow<Boolean>(false)

    val livenessResult = MutableStateFlow<LivenessResult?>(null)

    val compareDone = MutableStateFlow<Boolean>(false)

    companion object {
        val instance = DemoManager()
    }

    fun reset() {
        idResult.value = null
        idConfirmed.value = false

        ocrResult.value = null
        ocrConfirmed.value = false

        livenessResult.value = null
        compareDone.value = false
    }
}
