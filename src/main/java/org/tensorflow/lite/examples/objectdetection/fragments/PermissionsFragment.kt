/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed under an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.objectdetection.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import org.tensorflow.lite.examples.objectdetection.R

/**
 * The sole purpose of this fragment is to request permissions and, once granted, display the
 * camera fragment to the user.
 */
class PermissionsFragment : Fragment() {

    // All required permissions for the app
    private val PERMISSIONS_REQUIRED = mutableListOf(
        Manifest.permission.CAMERA
    ).apply {
        // Add Bluetooth permissions for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_CONNECT)
            add(Manifest.permission.BLUETOOTH_SCAN)
        } else {
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        // Add location permissions for Bluetooth scanning
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }.toTypedArray()

    private val requestMultiplePermissionsLauncher = 
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) 
        { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                Toast.makeText(context, "All permissions granted", Toast.LENGTH_LONG).show()
                navigateToCamera()
            } else {
                Toast.makeText(context, "Some permissions denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val allPermissionsGranted = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        
        if (allPermissionsGranted) {
            navigateToCamera()
        } else {
            requestMultiplePermissionsLauncher.launch(PERMISSIONS_REQUIRED)
        }
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                PermissionsFragmentDirections.actionPermissionsToCamera())
        }
    }

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
