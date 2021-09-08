package com.authme.sdk.sample.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.authme.library.ocr.template.ScanType
import com.authme.library.ocr.ui.OCRActivity
import com.authme.library.liveness.ui.LivenessActivity
import com.authme.sdkdemoall.R
import com.authme.sdk.sample.compare.CompareActivity
import com.authme.sdk.sample.manager.DemoManager
import com.authme.sdk.sample.ocr.OCRResultActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.initSDK()

        viewModel.idStatus().onEach {
            txt_id_status.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.ocrStatus().onEach {
            txt_ocr_status.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.livenessStatus().onEach {
            txt_liveness_status.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.compareEnable().onEach {
            btn_face_compare.isEnabled = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.compareStatus().onEach {
            txt_compare_status.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.compareDone().onEach {
            txt_done.isVisible = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        viewModel.customerId.observe(this) { id ->
            txt_user.text = "User: $id"
        }

        viewModel.showLoading.observe(this) {
            progress.isVisible = it
        }

        btn_customer_id_update.setOnClickListener {
            viewModel.updateCustomerId()
        }

        btn_id.setOnClickListener {
            OCRActivity.start(this, ScanType.TWID, null, REQUEST_CODE_ID)
        }

        btn_ocr.setOnClickListener {
            MaterialDialog(this).show {
                title(text = "選擇證件")
                listItemsSingleChoice(items = listOf("Driver License", "Passport", "Health ID")) { dialog, index, text ->
                    when (index) {
                        0 -> OCRActivity.start(this@MainActivity, ScanType.TWLicense, null, REQUEST_CODE_DRIVER_LICENSE)
                        1 -> OCRActivity.start(this@MainActivity, ScanType.Passport, null, REQUEST_CODE_PASSPORT)
                        2 -> OCRActivity.start(this@MainActivity, ScanType.TWHealth, null, REQUEST_CODE_TW_HEALTH)
                    }
                }
                positiveButton(text = "Confirm")
            }
        }

        btn_liveness.setOnClickListener {
            LivenessActivity.start(this, true, requestCode = REQUEST_CODE_LIVENESS)
        }

        btn_face_compare.setOnClickListener {
            CompareActivity.start(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_CODE_LIVENESS) {
            val livenessResult = LivenessActivity.getLivenessResult(data)
            if (livenessResult != null) {
                DemoManager.instance.livenessResult.value = livenessResult
                return
            }
        }

        val ocrResult = OCRActivity.castResult(data)
        if (ocrResult != null) {
            OCRResultActivity.start(this, ocrResult)
        }

    }

    companion object {
        private const val REQUEST_CODE_ID = 5390
        private const val REQUEST_CODE_DRIVER_LICENSE = 5391
        private const val REQUEST_CODE_LIVENESS = 5392
        private const val REQUEST_CODE_TW_HEALTH = 5393
        private const val REQUEST_CODE_PASSPORT = 5394
    }
}
