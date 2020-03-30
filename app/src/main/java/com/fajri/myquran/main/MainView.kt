package com.fajri.myquran.main
import com.fajri.myquran.model.Surat

interface MainView {
    fun showLoading()
    fun onRespone(hasil: ArrayList<Surat>?)
    fun onError(message: String?)
    fun hideLoading()
}