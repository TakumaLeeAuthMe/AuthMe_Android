package com.authme.sdk.sample.ocr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.authme.library.ocr.template.OCRResult
import com.authme.library.ocr.template.ScanType
import com.authme.sdkdemoall.R
import com.authme.sdk.sample.manager.DemoManager
import kotlinx.android.synthetic.main.activity_ocrresult.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class OCRResultActivity : AppCompatActivity() {

    private var adapter: OCRResultAdapter? = null
    private val ocrResult: OCRResult by lazy {
        intent.getParcelableExtra(DATA_RESULT)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocrresult)
        adapter = OCRResultAdapter(ocrResult)
        result_list_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        result_list_view.adapter = adapter
        image_view.setImageBitmap(ocrResult.frontImage)
    }

    override fun onStart() {
        super.onStart()
        btn_done.setOnClickListener {
            btn_done.visibility = View.INVISIBLE
            lifecycleScope.launch(Dispatchers.IO) {
                ocrResult.confirm()
                if (ocrResult.type == ScanType.TWID) {
                    DemoManager.instance.idResult.value = ocrResult
                    DemoManager.instance.idConfirmed.value = true
                } else {
                    DemoManager.instance.ocrResult.value = ocrResult
                    DemoManager.instance.ocrConfirmed.value = true
                }
                runOnUiThread {
                    finish()
                }
            }
        }
    }

    companion object {
        private const val DATA_RESULT = "DATA_RESULT"
        fun start(context: Activity, ocrResult: com.authme.library.ocr.template.OCRResult) {
            context.startActivity(Intent(context, OCRResultActivity::class.java).also {
                it.putExtra(DATA_RESULT, ocrResult)
            })
        }
    }
}
