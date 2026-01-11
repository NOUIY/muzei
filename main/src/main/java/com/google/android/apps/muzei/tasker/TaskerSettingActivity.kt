/*
 * Copyright 2014 Google Inc.
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

package com.google.android.apps.muzei.tasker

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.apps.muzei.util.rememberDrawablePainter
import com.joaomgcd.taskerpluginlibrary.TaskerPluginConstants.EXTRA_BUNDLE
import com.joaomgcd.taskerpluginlibrary.TaskerPluginConstants.EXTRA_STRING_BLURB
import net.nurik.roman.muzei.R

/**
 * The EDIT_SETTINGS activity for a Tasker Plugin allowing users to select whether they
 * want the Tasker action to move to the next artwork or select a particular provider
 */
class TaskerSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<TaskerSettingViewModel>()
            AlertDialog(
                onDismissRequest = {
                    setResult(RESULT_CANCELED, null)
                    finish()
                },
                confirmButton = {
                    // Users confirm by selecting an option from the list
                },
                title = {
                    Text(stringResource(R.string.tasker_setting_dialog_title))
                },
                text = {
                    val actions by viewModel.getActions().collectAsState(emptyList())
                    LazyColumn {
                        items(
                            actions,
                            key = { it.action.toBundle() }
                        ) { action ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable(onClickLabel = action.text) {
                                        val intent = Intent().apply {
                                            putExtra(EXTRA_STRING_BLURB, action.text)
                                            putExtra(EXTRA_BUNDLE, action.action.toBundle())
                                        }
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val icon = rememberDrawablePainter(action.icon)
                                Image(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    contentScale = ContentScale.Crop,
                                )
                                Spacer(Modifier.size(16.dp))
                                Text(
                                    text = action.text,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
