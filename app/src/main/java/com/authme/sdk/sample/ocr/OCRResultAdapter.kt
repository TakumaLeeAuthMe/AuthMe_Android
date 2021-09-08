/**
 * Created by Leon Lee on 2021/7/27.
 */

package com.authme.sdk.sample.ocr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.utils.MDUtil.textChanged
import com.authme.library.ocr.template.OCRResult
import com.authme.sdkdemoall.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_ocr_result.view.*


class OCRResultAdapter(private val ocrResult: OCRResult) : RecyclerView.Adapter<OCRResultAdapter.ViewHolder>() {

    private val list = ocrResult.confirmMap.toList().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ocr_result, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = list[position].first
        val value = list[position].second
        holder.textChanged = null
        holder.textField.hint = key
        holder.textField.editText?.setText(value)
        holder.textChanged = {
            list[position] = Pair(key, it)
            ocrResult.set(key, it)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textField: TextInputLayout = itemView.textField
        var textChanged: ((String) -> Unit)? = null

        init {
            textField.editText?.textChanged {
                textChanged?.invoke(it.toString())
            }
        }
    }
}
