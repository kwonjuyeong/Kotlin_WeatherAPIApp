package com.example.kotlin_weatherapiapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsyncTask().execute()
    }

    private inner class CallAPILoginAsyncTask() : AsyncTask<Any, Void, String>() {

        // A variable for Custom Progress Dialog
        private lateinit var customProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()

            showProgressDialog()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result: String


            var connection: HttpURLConnection? = null

            try {
                val url = URL("http://run.mocky.io/v3/ff9bc8e6-75cd-496d-9b9e-7c75006e41ed")
                connection = url.openConnection() as HttpURLConnection

                connection.doOutput = true
                connection.doInput = true

                val httpResult: Int = connection.responseCode

                if (httpResult == HttpURLConnection.HTTP_OK) {

                    val inputStream = connection.inputStream

                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            sb.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    result = sb.toString()
                } else {
                    result = connection.responseMessage
                }

            } catch (e: SocketTimeoutException) {
                result = "Connection Timeout"
            } catch (e: Exception) {
                result = "Error : " + e.message
            } finally {
                connection?.disconnect()
            }

            // You can notify with your result to onPostExecute.
            return result
        }



        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            cancelProgressDialog()
            Log.i("JSON RESPONCE RESULT", result)
            val jsonObject = JSONObject(result)

            //json 객체에 접근
            val message = jsonObject.optString("message")
            Log.i("Message", message)
            //Int형 객체이므로 optInt 사용하여 접근
            val userId = jsonObject.optInt("user_id")
            Log.i("User Id", "$userId")
            val name = jsonObject.optString("name")
            Log.i("Name", "$name")
            val email = jsonObject.optString("email")
            Log.i("Email", "$email")
            val mobileNumber = jsonObject.optLong("mobile")
            Log.i("Mobile", "$mobileNumber")

            //객체 안에 새로운 객체를 정의해준다.
            val profileDetailsObject = jsonObject.optJSONObject("profile_details")

            val isProfileCompleted = profileDetailsObject.optBoolean("is_profile_completed")
            Log.i("Is Profile Completed", "$isProfileCompleted")
            val rating = profileDetailsObject.optDouble("rating")
            Log.i("Rating", "$rating")


            //data_list라는 오브젝트 정의
            val dataListArray = jsonObject.optJSONArray("data_list")

            Log.i("Data List Size", "${dataListArray.length()}")

            //data_list 안에는 id와 value 값이 들어있고 총 3개의 객체가 존재한다.
            //따라서 for문을 이용해 길이만큼 계산해준다.
            for (item in 0 until dataListArray.length()) {
                Log.i("Value $item", "${dataListArray[item]}")

                val dataItemObject: JSONObject = dataListArray[item] as JSONObject

                val id = dataItemObject.optString("id")
                Log.i("ID", "$id")
                val value = dataItemObject.optString("value")
                Log.i("Value", "$value")
            }
        }


        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)

            customProgressDialog.show()
        }


        private fun cancelProgressDialog() {
            customProgressDialog.dismiss()
        }


    }
}