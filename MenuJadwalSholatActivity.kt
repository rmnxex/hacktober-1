package com.blogsetyaaji.dashboardislami.menus.jadwalsholat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.blogsetyaaji.dashboardislami.databinding.ActivityMenuJadwalSholatBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MenuJadwalSholatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuJadwalSholatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuJadwalSholatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        initView()
    }

    private fun initView() {
        val calendar = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        val formatedDate: String = dateFormat.format(calendar)

        binding.tvDatePray.text = formatedDate

        initJadwalSholat(calendar, "Jakarta")
    }

    private fun initJadwalSholat(date: Date, city: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatedDate = dateFormat.format(date)

        val client = AsyncHttpClient()
        val url = "https://api.pray.zone/v2/times/day.json?city=$city&date=$formatedDate"

        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.pbJadwalSholat.visibility = View.INVISIBLE

                val response = responseBody?.let { String(it) }

                try {
                    val responObject = JSONObject(response)
                    val dataResults = responObject.getJSONObject("results")
                    val dataDateTimeArray = dataResults.getJSONArray("datetime")
                    val dataDateTime = dataDateTimeArray.getJSONObject(0)
                    val dataTimes = dataDateTime.getJSONObject("times")

                    binding.tvPrayTimeImsak.text = dataTimes.getString("Imsak")
                    binding.tvPrayTimeSunrise.text = dataTimes.getString("Sunrise")
                    binding.tvPrayTimeDzuhur.text = dataTimes.getString("Dhuhr")
                    binding.tvPrayTimeSubuh.text = dataTimes.getString("Fajr")
                    binding.tvPrayTimeAshar.text = dataTimes.getString("Asr")
                    binding.tvPrayTimeMaghrib.text = dataTimes.getString("Maghrib")
                    binding.tvPrayTimeIsya.text = dataTimes.getString("Isha")

                    val dataObjectLocation = dataResults.getJSONObject("location")
                    binding.tvLocation.text = dataObjectLocation.getString("city")
                        .plus(", ")
                        .plus(dataObjectLocation.getString("country"))

                } catch (e: Exception){
                    Toast.makeText(this@MenuJadwalSholatActivity, e.message, Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.pbJadwalSholat.visibility = View.INVISIBLE

                val errorMessage = when(statusCode){
                    400 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(this@MenuJadwalSholatActivity, errorMessage,
                Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}