/*
 * Copyright (c) 2010-2020 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.activities.assistant

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.linphone.LinphoneApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.GenericActivity
import org.linphone.activities.SnackBarActivity
import org.linphone.activities.assistant.fragments.EchoCancellerCalibrationFragment
import org.linphone.activities.assistant.viewmodels.SharedAssistantViewModel

class AssistantActivity : GenericActivity(), SnackBarActivity {
    private lateinit var sharedViewModel: SharedAssistantViewModel
    private lateinit var coordinator: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.assistant_activity)

        sharedViewModel = ViewModelProvider(this)[SharedAssistantViewModel::class.java]

        coordinator = findViewById(R.id.coordinator)

        corePreferences.firstStart = false

        Toast.makeText(this@AssistantActivity, "" + EchoCancellerCalibrationFragment.RECORD_AUDIO_PERMISSION_REQUEST_CODE, Toast.LENGTH_SHORT).show()
        requestPermissions(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
            ),
            EchoCancellerCalibrationFragment.RECORD_AUDIO_PERMISSION_REQUEST_CODE
        )
    }

    /*

     arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.MANAGE_OWN_CALLS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_CONTACTS,

            )

     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
    }

    override fun showSnackBar(resourceId: Int) {
        Snackbar.make(coordinator, resourceId, Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackBar(resourceId: Int, action: Int, listener: () -> Unit) {
        Snackbar
            .make(findViewById(R.id.coordinator), resourceId, Snackbar.LENGTH_LONG)
            .setAction(action) {
                listener()
            }
            .show()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show()
    }
}
