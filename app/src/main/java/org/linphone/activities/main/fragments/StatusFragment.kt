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
package org.linphone.activities.main.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.linphone.LinphoneApplication.Companion.coreContext
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.main.LoginUserActivity
import org.linphone.activities.main.viewmodels.SharedMainViewModel
import org.linphone.activities.main.viewmodels.StatusViewModel
import org.linphone.core.RegistrationState
import org.linphone.core.tools.Log
import org.linphone.databinding.StatusFragmentBinding
import org.linphone.utils.Event

class StatusFragment : GenericFragment<StatusFragmentBinding>() {
    private lateinit var viewModel: StatusViewModel
    private lateinit var sharedViewModel: SharedMainViewModel

    override fun getLayoutId(): Int = R.layout.status_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        useMaterialSharedAxisXForwardAnimation = false

        viewModel = ViewModelProvider(this)[StatusViewModel::class.java]
        binding.viewModel = viewModel

        sharedViewModel = requireActivity().run {
            ViewModelProvider(this)[SharedMainViewModel::class.java]
        }

        sharedViewModel.accountRemoved.observe(
            viewLifecycleOwner
        ) {
            Log.i("[Status Fragment] An account was removed, update default account state")
            val defaultAccount = coreContext.core.defaultAccount
            if (defaultAccount != null) {
                viewModel.updateDefaultAccountRegistrationStatus(defaultAccount.state)
            }
        }

        binding.setMenuClickListener {
            sharedViewModel.toggleDrawerEvent.value = Event(true)
        }

        binding.setRefreshClickListener {
            viewModel.refreshRegister()
        }

        onBackPressedCallback.isEnabled = false
    }

    fun verifyStatusLogin(state: RegistrationState) {
        val nameFile = requireContext().packageName + "_preferences"
        val preferences = this.requireActivity().getSharedPreferences(nameFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()

        if (RegistrationState.Ok == state) {
            Toast.makeText(activity, "OKKKKKKKKKKK" + state, Toast.LENGTH_SHORT).show()
            editor.putString("isLoged", "yes")
            editor.apply()
            editor.commit()
        }

        if (RegistrationState.Failed == state) {
            Toast.makeText(activity, "FAILED" + state, Toast.LENGTH_SHORT).show()
            editor.putString("isLoged", "no")
            editor.apply()
            editor.commit()

            val intent = Intent(activity, LoginUserActivity::class.java)
            startActivity(intent)
        }
    }
}
