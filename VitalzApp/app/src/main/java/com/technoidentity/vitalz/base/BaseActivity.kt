package com.technoidentity.vitalz.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.utils.ConnectionType
import com.technoidentity.vitalz.utils.NetworkUtil
import com.technoidentity.vitalz.utils.isTablet
import com.technoidentity.vitalz.utils.showToast


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var networkMonitor: NetworkUtil

     var bleScanStatus: Boolean = false

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        setContentView(binding.root)

        if(isTablet(this)) showToast(this, "This is tablet") else showToast(this, "This is mobile")

        networkMonitor = NetworkUtil(this)

        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.WIFI -> {
                                //internet_status.text = "Wifi Connection"
                                //Snackbar.make(binding.root, "Connected", Snackbar.LENGTH_SHORT).show()
                            }
                            ConnectionType.CELLULAR -> {
                                //internet_status.text = "Cellular Connection"
                                //Snackbar.make(binding.root, "Connected", Snackbar.LENGTH_SHORT).show()
                            }
                            else -> {
                                //Snackbar.make(binding.root, "Connected", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                    false -> {
                        Snackbar.make(View(this), "No Connection", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        networkMonitor.register()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }

    override fun onStop() {
        super.onStop()
        //networkMonitor.unregister()
    }

    abstract fun getViewBinding(): VB

}