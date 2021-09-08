package com.authme.sdk.sample.compare

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.authme.sdkdemoall.R
import kotlinx.android.synthetic.main.activity_compare.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CompareActivity : AppCompatActivity() {

    private val viewModel: CompareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        viewModel.status1().onEach {
            txt_compare1.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.status2().onEach {
            txt_compare2.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.status3().onEach {
            txt_compare3.text = it
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.idResult().onEach {
            image1.setImageBitmap(it.frontImage)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.ocrResult().onEach {
            image2.setImageBitmap(it.frontImage)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.livenessResult().onEach {
            image3.setImageBitmap(it.originalImage)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        btn_done.setOnClickListener {
            viewModel.comfirm()
            finish()
        }
    }

    companion object {
        private const val DATA_RESULT = "DATA_RESULT"
        fun start(context: Activity) {
            context.startActivity(Intent(context, CompareActivity::class.java))
        }
    }
}
