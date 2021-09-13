package com.technoidentity.vitalz.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.databinding.ActivitySplashScreenBinding
import com.technoidentity.vitalz.utils.showSnackbar

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()

        val countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    binding.root.showSnackbar(R.string.ble_not_suppported, Snackbar.LENGTH_INDEFINITE, R.string.ok) {
                        finish()
                    }
                } else {
                    Intent(this@SplashActivity, HomeActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }

        }
        countDownTimer.start()


    }
}



