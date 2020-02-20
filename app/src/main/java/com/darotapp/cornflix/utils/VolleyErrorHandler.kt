package com.darotapp.cornflix.utils

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class VolleyErrorHandler() {


    private object HOLDER {
        val INSTANCE = VolleyErrorHandler()
    }

    companion object {
        val instance: VolleyErrorHandler by lazy { HOLDER.INSTANCE }
    }

    fun erroHandler(error: VolleyError): String {
//        val error = com.android.volley.VolleyError()
        var errorString = ""
        var parseError: String = ""

        if (error.networkResponse != null) {


            try {
                val errorByte = error.networkResponse.data
                parseError = errorByte.toString(StandardCharsets.UTF_8)
//

//                            Log.e("Data", "Response ${error.networkResponse.data}")


                val errorObj = JSONObject(parseError)

                val errorMessage = errorObj.getString("message")

                Log.e("Datainside", "Response $errorObj")
                Log.e("parse", "Response $parseError")
                errorString += errorMessage

//                Toast.makeText(context.applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                val message = parseError

                errorString += parseError


            }


        } else {
            errorString += "Bad internet connection, please try again"
        }

        return errorString

    }
}