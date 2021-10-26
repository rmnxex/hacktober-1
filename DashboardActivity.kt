package com.razzaq.bitaqwaapp.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razzaq.bitaqwaapp.R
import com.razzaq.bitaqwaapp.dashboard.adapter.InspirationListAdapter
import com.razzaq.bitaqwaapp.dashboard.data.InspirationData
import com.razzaq.bitaqwaapp.dashboard.model.InspirationModel
import com.razzaq.bitaqwaapp.menus.doa.MenuDoaActivity
import com.razzaq.bitaqwaapp.menus.dzikir.MenuDzikirActivity
import com.razzaq.bitaqwaapp.menus.jadwalsholat.MenuJadwalSholatActivity
import com.razzaq.bitaqwaapp.menus.videokajian.MenuVideoKajianActivity
import com.razzaq.bitaqwaapp.menus.zakat.MenuZakatActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardActivity : AppCompatActivity() {

    lateinit var ivIconMenuDoa: ImageView
    lateinit var ivIconMenuDzikir: ImageView 
    lateinit var ivIconMenuSholat: ImageView
    lateinit var ivIconMenuKajian: ImageView
    lateinit var ivIconMenuZakat: ImageView

    lateinit var ivIconHeader: ImageView
    lateinit var ivRvInspiration: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        ivIconMenuDoa = findViewById(R.id.ivMenuDoa)
        ivIconMenuDzikir = findViewById(R.id.ivMenuDzikir)
        ivIconMenuSholat = findViewById(R.id.ivJadwalSholat)
        ivIconMenuKajian = findViewById(R.id.ivVideoKajian)
        ivIconMenuZakat = findViewById(R.id.ivMenuZakat)
        ivIconHeader = findViewById(R.id.ivHeader)
        ivRvInspiration = findViewById(R.id.rvInspiration)

        initNavMenu()
        initHeader()
        initRecycleViewInspiration()
    }

    private fun initRecycleViewInspiration() {
        val list:ArrayList<InspirationModel> = arrayListOf()
        ivRvInspiration.setHasFixedSize(true)
        list.addAll(InspirationData.listData)
        ivRvInspiration.layoutManager=LinearLayoutManager(this)
        val listInspirationAdapter = InspirationListAdapter(list)
        ivRvInspiration.adapter = listInspirationAdapter
    }

    private fun initHeader() {
        val timeNow = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH")
        val time = timeFormat.format(timeNow.time)

        when {
            time.toInt() in 0..10 -> {
                ivIconHeader.setBackgroundResource(R.drawable.bg_header_dashboard_morning)
            }
            time.toInt() in 11..17 -> {
                ivIconHeader.setBackgroundResource(R.drawable.bg_header_dashboard_afternoon)
            }
            time.toInt() in 18..24 -> {
                ivIconHeader.setBackgroundResource(R.drawable.bg_header_dashboard_night)
            }
        }
    }

    private fun initNavMenu() {
        ivIconMenuDoa.setOnClickListener {
            startActivity(Intent(this, MenuDoaActivity::class.java))
        }

        ivIconMenuDzikir.setOnClickListener {
            startActivity(Intent(this, MenuDzikirActivity::class.java))
        }

        ivIconMenuSholat.setOnClickListener {
            startActivity(Intent(this, MenuJadwalSholatActivity::class.java))
        }

        ivIconMenuKajian.setOnClickListener {
            startActivity(Intent(this, MenuVideoKajianActivity::class.java))
        }

        ivIconMenuZakat.setOnClickListener {
            startActivity(Intent(this, MenuZakatActivity::class.java))
        }
    }
}