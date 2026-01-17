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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.google.android.apps.muzei.sync.ProviderManager
import com.google.android.apps.muzei.theme.AppTheme
import com.google.android.apps.muzei.util.toast
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import net.nurik.roman.muzei.R

class AutoAdvanceFragment : Fragment() {
    companion object {
        private const val TASKER_PACKAGE_NAME = "net.dinglisch.android.taskerm"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val context = LocalContext.current
        val providerManager = remember { ProviderManager.getInstance(context) }

        AppTheme(
            dynamicColor = false
        ) {
            AutoAdvance(
                providerManager = providerManager,
                contentPadding = WindowInsets.safeDrawing
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                    .asPaddingValues(),
                onOpenTasker = {
                    try {
                        val pm = context.packageManager
                        context.startActivity(
                            (pm.getLaunchIntentForPackage(TASKER_PACKAGE_NAME)
                                ?: Intent(
                                    Intent.ACTION_VIEW,
                                    ("https://play.google.com/store/apps/details?id=" +
                                            TASKER_PACKAGE_NAME +
                                            "&referrer=utm_source%3Dmuzei%26utm_medium%3Dapp%26utm_campaign%3Dauto_advance").toUri()
                                )
                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                        )
                        Firebase.analytics.logEvent("tasker_open", null)
                    } catch (_: ActivityNotFoundException) {
                        context.toast(R.string.play_store_not_found, Toast.LENGTH_LONG)
                    } catch (_: SecurityException) {
                        context.toast(R.string.play_store_not_found, Toast.LENGTH_LONG)
                    }
                }
            )
        }
    }
}