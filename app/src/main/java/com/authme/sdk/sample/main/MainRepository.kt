package com.authme.sdk.sample.main

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.authme.sdk.sample.APP
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private class TokenRequest(url: String?,
                           private val clientID: String,
                           private val secret: String,
                           private val customerID: String,
                           listener: Response.Listener<String>?,
                           errorListener: Response.ErrorListener?) : StringRequest(Request.Method.POST, url, listener, errorListener) {
    override fun getBodyContentType(): String {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    override fun getParams(): MutableMap<String, String> {
        return mutableMapOf("client_id" to clientID,
                "client_secret" to secret,
                "grant_type" to "client_credentials",
                "scope" to "BIOMS customer_id:${customerID}")
    }
}

class MainRepository(private val context: Context = APP.instance) {

    private val queue = Volley.newRequestQueue(context)

    private val hostProd = "https://api.authme.com"
    private val hostStaging = "https://staging.api.authme.com"
    private val demoURL = "https://sc-poc.api.authme.com"

    private val host = hostStaging

    suspend fun getToken(
            clientID: String,
            secret: String,
            customerID: String) = suspendCoroutine<String> { continuation ->
        val url = "$host/connect/token"
        val stringRequest = TokenRequest(
                url,
                clientID,
                secret,
                customerID, { response ->
            continuation.resume(parseToken(JSONObject(response)))
        }, {
            it.printStackTrace()
            continuation.resumeWithException(it)
        })
        queue.add(stringRequest)
    }

    private fun parseToken(json: JSONObject): String {
        return json.getString("access_token")
    }
}

