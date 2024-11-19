package com.example.examplenavigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.examplenavigation.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation Component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Hubungkan BottomNavigationView dengan NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Tambahkan listener untuk menampilkan indikator
        setupNavigationIndicator(navController)
    }

    @SuppressLint("RestrictedApi")
    private fun setupNavigationIndicator(navController: NavController) {
        // Parent BottomNavigationView
        val menuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView

        // Listener untuk memperbarui indikator pada item yang diklik
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val index = getMenuItemIndex(menuItem.itemId)
            updateIndicator(index, menuView)
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            true
        }

        // Listener untuk menyinkronkan indikator saat fragment berubah
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val index = getMenuItemIndex(destination.id)
            updateIndicator(index, menuView)
        }

        // Menetapkan indikator default (item pertama)
        val defaultIndex = getMenuItemIndex(navController.currentDestination?.id ?: -1)
        updateIndicator(defaultIndex, menuView)
    }

    @SuppressLint("RestrictedApi")
    private fun updateIndicator(index: Int, menuView: BottomNavigationMenuView) {
        // Menghapus indikator dari semua item
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
            val indicator = itemView.findViewById<View>(R.id.nav_indicator)
            indicator?.let { itemView.removeView(it) }
        }

        // Menambahkan indikator pada item yang dipilih
        if (index in 0 until menuView.childCount) {
            val selectedItemView = menuView.getChildAt(index) as BottomNavigationItemView
            val indicatorView = View(this).apply {
                id = R.id.nav_indicator
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, // Lebar indikator sesuai item
                    10.dp // Total tinggi (garis + bayangan)
                ).apply {
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // Muncul di atas ikon menu
                    topMargin = -6.dp // Atur jarak agar sedikit di atas ikon
                }
                setBackgroundResource(R.drawable.nav_indikator_gojek) // Mengatur background ripple
            }
            selectedItemView.addView(indicatorView)
        }
    }

    // Fungsi untuk mendapatkan indeks item berdasarkan ID
    private fun getMenuItemIndex(itemId: Int): Int {
        for (i in 0 until binding.bottomNavigationView.menu.size()) {
            if (binding.bottomNavigationView.menu.getItem(i).itemId == itemId) {
                return i
            }
        }
        return -1 // Jika item tidak ditemukan
    }

    // Ekstensi untuk konversi dp ke px
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
//    @SuppressLint("RestrictedApi")
//    private fun updateIndicator(index: Int, menuView: BottomNavigationMenuView) {
//        // Hapus indikator dari semua item
//        for (i in 0 until menuView.childCount) {
//            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
//            val indicator = itemView.findViewById<View>(R.id.nav_indicator)
//            indicator?.let { itemView.removeView(it) }
//        }
//
//        // Tambahkan indikator pada item yang dipilih
//        if (index in 0 until menuView.childCount) {
//            val selectedItemView = menuView.getChildAt(index) as BottomNavigationItemView
//            val indicatorView = View(this).apply {
//                id = R.id.nav_indicator
//                layoutParams = FrameLayout.LayoutParams(16.dp, 16.dp).apply {
//                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//                    topMargin = (-8).dp // Penyesuaian posisi bulatan
//                }
//                setBackgroundResource(R.drawable.nav_indikator) // Drawable bulatan
//            }
//            selectedItemView.addView(indicatorView)
//        }
//    }


