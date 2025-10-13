/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.apps.muzei

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.apps.muzei.util.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.nurik.roman.muzei.R
import net.nurik.roman.muzei.androidclientcommon.R as CommonR

class MissingResourcesDialogFragment : DialogFragment() {
    companion object {
        fun showDialogIfNeeded(activity: FragmentActivity) : Boolean {
            val missingResources = try {
                ContextCompat.getDrawable(activity, CommonR.drawable.ic_stat_muzei)
                ContextCompat.getDrawable(activity, R.drawable.about_android_experiment)
                false
            } catch (_ : Resources.NotFoundException) {
                true
            }
            if (missingResources) {
                MissingResourcesDialogFragment().show(
                        activity.supportFragmentManager,"missingResources")
            }
            return missingResources
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.missing_resources_title)
                .setMessage(R.string.missing_resources_message)
                .setPositiveButton(R.string.missing_resources_open) { _: DialogInterface, _: Int ->
                    try {
                        val playStoreIntent = Intent(Intent.ACTION_VIEW,
                            ("https://play.google.com/store/apps/details?id=" +
                                    requireContext().packageName).toUri())
                        startActivity(playStoreIntent)
                    } catch (_: ActivityNotFoundException) {
                        requireContext().toast(R.string.play_store_not_found, Toast.LENGTH_LONG)
                    } catch (_: SecurityException) {
                        requireContext().toast(R.string.play_store_not_found, Toast.LENGTH_LONG)
                    }
                    requireActivity().finish()
                }
                .setNegativeButton(R.string.missing_resources_quit) { _: DialogInterface, _: Int ->
                    requireActivity().finish()
                }
                .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().finish()
    }
}