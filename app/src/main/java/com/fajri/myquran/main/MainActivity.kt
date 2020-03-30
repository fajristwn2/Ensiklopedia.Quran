package com.fajri.myquran.main
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import android.util.Log
import android.widget.Toast
import com.fajri.myquran.R
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.fajri.myquran.detail.DetailActivity
import com.fajri.myquran.adapter.AdapterSurat
import com.fajri.myquran.model.Surat
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private lateinit var mainPresenter: MainPresenter
    private var list: MutableList<Surat> = mutableListOf()
    private lateinit var adapterSurat: AdapterSurat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //custom color swipe refresh
        swipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

        mainPresenter = MainPresenter(this)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        AndroidNetworking.initialize(this, httpClient)

        swipeRefresh.post {
            loadSurat()
        }

        swipeRefresh.setOnRefreshListener {
            loadSurat()
        }
    }

    private fun loadSurat() {
        mainPresenter.getSurat()

        adapterSurat = AdapterSurat(this, list) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity().NOMOR, it.nomor)
            intent.putExtra(DetailActivity().NAMASURAT, it.nama)
            intent.putExtra(DetailActivity().ASMA, it.asma)
            intent.putExtra(DetailActivity().ARTI, it.arti)
            intent.putExtra(DetailActivity().JUMLAHAYAT, it.ayat)
            intent.putExtra(DetailActivity().TURUNSURAT, it.type)
            intent.putExtra(DetailActivity().TURUNKE, it.urut)
            intent.putExtra(DetailActivity().KETERANGAN, it.keterangan)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = adapterSurat
    }

    fun showLoading() {

        swipeRefresh.isRefreshing = true
    }

    fun onRespone(hasil: ArrayList<Surat>?) {
        list.clear()
        hasil?.let {
            list.addAll(it)
        }
        adapterSurat.notifyDataSetChanged()
    }

    fun onError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("onError", message)
    }

    fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }
}