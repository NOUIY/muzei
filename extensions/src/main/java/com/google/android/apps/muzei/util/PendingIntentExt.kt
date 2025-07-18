/*
* Copyright 2024 Google Inc.
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

package com.google.android.apps.muzei.util

import android.app.ActivityOptions
import android.app.PendingIntent
import android.os.Build

/**
 * Ensure that the proper flags are sent along with the PendingIntent so that
 * background activity launches are allowed.
 */
fun PendingIntent.sendFromBackground() {
    if (Build.VERSION.SDK_INT >= 34) {
        val options = ActivityOptions.makeBasic().apply {
            @Suppress("DEPRECATION") /* Doesn't work unless this is also used ¯\_(ツ)_/¯ */
            isPendingIntentBackgroundActivityLaunchAllowed = true
            if (Build.VERSION.SDK_INT >= 36) {
                setPendingIntentCreatorBackgroundActivityStartMode(ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOW_ALWAYS)
            } else {
                @Suppress("DEPRECATION") /* Needed for API 34-35 */
                setPendingIntentCreatorBackgroundActivityStartMode(ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED)
            }
        }.toBundle()
        send(options)
    } else {
        send()
    }
}