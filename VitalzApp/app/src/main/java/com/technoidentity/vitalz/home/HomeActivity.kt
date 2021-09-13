package com.technoidentity.vitalz.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView.*
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.base.BaseActivity
import com.technoidentity.vitalz.bluetooth.*
import com.technoidentity.vitalz.databinding.ActivityHomeBinding
import com.technoidentity.vitalz.utils.*
import com.technoidentity.vitalz.utils.Constants.CARETAKER
import com.technoidentity.vitalz.utils.Constants.DOCTOR
import com.technoidentity.vitalz.utils.Constants.NURSE
import com.technoidentity.vitalz.utils.Constants.REQUEST_LOCATION_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)

    val sharedViewModel: SharedViewModel by viewModels()
    private val bluetoothAdapter by lazy(LazyThreadSafetyMode.NONE) {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private var locationServiceEnabled: CompletableDeferred<Boolean>? = null
    var locationPermissionGranted: CompletableDeferred<Boolean>? = null
    var bluetoothEnabled: CompletableDeferred<Boolean>? = null

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.homeFragment,
                R.id.addDeviceFragment,
                R.id.bleScanResultFragment
            ) // Add fragments ID that should not have up button
            .build()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

         navController = navHostFragment.navController

        //this tablet check is for BLE Device Scan
        if (isTablet(this)) {
            lifecycleScope.launchWhenStarted {
                if (initializeBleScanner()) {
                    showToast(
                        this@HomeActivity,
                        "Scanner initialised successfully"
                    )
                    Timber.d(sharedViewModel.javaClass.toString())
                    sharedViewModel.startScan()
                } else {
                    showToast(
                        this@HomeActivity,
                        "Scanner initialisation failed"
                    )
                }
            }
        }
    }

    private suspend fun initializeBleScanner(): Boolean {
        locationServiceEnabled = CompletableDeferred()
        locationPermissionGranted = CompletableDeferred()
        bluetoothEnabled = CompletableDeferred()

        fun done(result: Boolean = false): Boolean {
            locationServiceEnabled = null
            locationPermissionGranted = null
            bluetoothEnabled = null
            return result
        }

        if (!hasPermission(PERMISSION_TYPE_FINE_LOCATION)) {
            val positiveButtonClick = { _: DialogInterface, _: Int ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            showAlert(
                title = "Location Permission Required",
                message = "The application requires location access in order to scan for BLE devices.",
                positiveBtnClickListener = positiveButtonClick
            )

            if (locationPermissionGranted?.await() == false) {
                return done()
            }
        }

        if (!isLocationServiceEnabled()) {
            promptEnableLocationServices(REQUEST_LOCATION_SERVICE)
            if (locationServiceEnabled?.await() == false) return done()
        }

        if (bluetoothAdapter.isDisabled) {
            promptEnableBluetooth()
            if (bluetoothEnabled?.await() == false) return done()
        }

        return done(true)
    }


    private fun promptEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startBleOnForResult.launch(enableBtIntent)
    }

    val startBleOnForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            when (result.resultCode) {
                RESULT_OK -> bluetoothEnabled?.run {
                    complete(result.resultCode == RESULT_OK)
                }
                else -> {
                    showAlert(
                        title = "Enable Bluetooth",
                        message = "Bluetooth should be enabled to use the app",
                        positiveBtnClickListener = { _, _ ->
                            promptEnableBluetooth()
                        },
                        negativeBtnClickListener = { _, _ ->
                            finishAffinity()
                        })
                }

            }
        }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

                locationPermissionGranted?.run {
                    complete(isGranted)
                }

            } else {
                showAlert(
                    title = "Permission Required",
                    message = "App requires location permission for bluetooth, app will be closed",
                    positiveBtnClickListener = { _, _ ->
                        finishAffinity()
                    })
            }
        }

    override fun onResume() {
        super.onResume()

        //bottomNavigation with Navigation Component
        setUpBottomNavigationBar(navController)

        //setting up bottom Nav with Nav Controller
        binding.bottomNavView.setupWithNavController(navController)


        binding.bottomNavView.setOnNavigationItemSelectedListener {

            lifecycleScope.launchWhenResumed {

                when (it.itemId) {
                    R.id.home_tab -> {
                        sharedViewModel.isSelected.observe(this@HomeActivity, { status ->
                            when (status) {
                                true -> {
                                    navController.navigate(R.id.singlePatientDashboardFragment)
                                }
                                false -> {
                                    if(!isTablet(this@HomeActivity)){
                                        navController.navigate(R.id.singlePatientDashboardFragment)
                                    }
                                    navController.navigate(R.id.multiPatientDashboardFragment)
                                }
                            }
                        })
                    }
                    R.id.notifications_tab -> {
                        sharedViewModel.assignedRole.collect {
                            when(it){
                                DOCTOR -> {
                                    navController.navigate(R.id.notificationsFragment, bundleOf("assignedRole" to DOCTOR))
                                }
                                NURSE -> {
                                    navController.navigate(R.id.notificationsFragment, bundleOf("assignedRole" to NURSE))
                                }
                                else -> {
                                    navController.navigate(R.id.notificationsFragment, bundleOf("assignedRole" to CARETAKER))
                                }
                            }
                        }
                    }
                    R.id.settings_tab -> {
                        navController.navigate(R.id.settingsFragment)
                    }
                }
            }
            true
        }

        //Adding badges to Notification
        val badge = binding.bottomNavView.getOrCreateBadge(R.id.notifications_tab)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set:
        lifecycleScope.launchWhenResumed {
            sharedViewModel.notificationCount.collect { badge.number = it
            }
        }

    }

    private fun setUpBottomNavigationBar(navController: NavController) {
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.userSelectionFragment2 ||
                destination.id == R.id.addDeviceFragment ||
                destination.id == R.id.bleScanResultFragment ||
                destination.id == R.id.deviceDetailsFragment ||
                destination.id == R.id.hospitalListFragment ||
                destination.id == R.id.patientListFragment ||
                destination.id == R.id.careTakerMobileOTPFragment ||
                destination.id == R.id.careTakerMobileLoginFragment ||
                destination.id == R.id.patientProfileFragment ||
                destination.id == R.id.doctorNurseLoginFragment ||
                destination.id == R.id.singlePatientDetailFragment
            ) {
                binding.bottomNavView.visibility = View.GONE
            } else {
                binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.stopScan()
    }
}
