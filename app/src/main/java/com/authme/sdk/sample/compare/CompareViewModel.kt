/**
 * Created by Leon Lee on 2021/7/28.
 */

package com.authme.sdk.sample.compare

import androidx.lifecycle.ViewModel
import com.authme.library.liveness.LivenessResult
import com.authme.library.liveness.LivenessUtils
import com.authme.library.ocr.template.OCRResult
import com.authme.sdk.sample.manager.DemoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CompareViewModel : ViewModel() {

    fun idResult(): Flow<OCRResult> {
        return DemoManager.instance.idResult.filterNotNull()
    }

    fun ocrResult(): Flow<OCRResult> {
        return DemoManager.instance.ocrResult.filterNotNull()
    }

    fun livenessResult(): Flow<LivenessResult> {
        return DemoManager.instance.livenessResult.filterNotNull()
    }

    fun status1(): Flow<String> {
        return idResult().combine(livenessResult()) { a, b ->
            return@combine LivenessUtils.faceCompare(a, b)
        }.flowOn(Dispatchers.IO).map {
            "card 1 & face: ${it.score}"
        }
    }

    fun status2(): Flow<String> {
        return ocrResult().combine(livenessResult()) { a, b ->
            return@combine LivenessUtils.faceCompare(a, b)
        }.flowOn(Dispatchers.IO).map {
            "card 2 & face: ${it.score}"
        }
    }

    fun status3(): Flow<String> {
        return idResult().combine(ocrResult()) { a, b ->
            return@combine LivenessUtils.faceCompare(a, b)
        }.flowOn(Dispatchers.IO).map {
            "card 1 & card 2: ${it.score}"
        }
    }

    fun comfirm(){
        DemoManager.instance.compareDone.value = true
    }
}