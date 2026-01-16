/*
 * Copyright 2025 Google Inc.
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.apps.muzei.theme.AppTheme
import com.google.android.apps.muzei.util.RadioButtonGroup
import com.google.android.apps.muzei.util.RadioButtonSectionHeader
import net.nurik.roman.muzei.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoAdvance(
    advanceOnWifi: Boolean,
    onAdvanceOnWifiChanged: (Boolean) -> Unit,
    orderingSelectedOption: String,
    onOrderingSelectedOptionChange: (String) -> Unit,
    intervalSelectedOption: String,
    onIntervalSelectedOptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onOpenTasker: () -> Unit = {},
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(contentPadding),
        ) {
            Text(
                text = stringResource(R.string.auto_advance_title),
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = stringResource(R.string.auto_advance_description),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .selectable(
                        selected = advanceOnWifi,
                        onClick = { onAdvanceOnWifiChanged(!advanceOnWifi) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = advanceOnWifi,
                    onCheckedChange = null,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.auto_advance_wifi),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f).padding(start = 8.dp, end = 16.dp)
                )
            }
            RadioButtonSectionHeader(
                title = stringResource(R.string.auto_advance_ordering),
            )
            val orderingOptions = listOf(
                stringResource(R.string.auto_advance_ordering_in_order),
                stringResource(R.string.auto_advance_ordering_new_in_order),
                stringResource(R.string.auto_advance_ordering_random),
            )
            RadioButtonGroup(
                options = orderingOptions,
                selectedOption = orderingSelectedOption,
                onOptionSelected = onOrderingSelectedOptionChange,
            )
            RadioButtonSectionHeader(
                title = stringResource(R.string.auto_advance_interval),
            )
            val intervalOptions = listOf(
                stringResource(R.string.auto_advance_interval_15m),
                stringResource(R.string.auto_advance_interval_30m),
                stringResource(R.string.auto_advance_interval_1h),
                stringResource(R.string.auto_advance_interval_3h),
                stringResource(R.string.auto_advance_interval_6h),
                stringResource(R.string.auto_advance_interval_24h),
                stringResource(R.string.auto_advance_interval_72h),
                stringResource(R.string.auto_advance_interval_never),
            )
            RadioButtonGroup(
                options = intervalOptions,
                selectedOption = intervalSelectedOption,
                onOptionSelected = onIntervalSelectedOptionChange,
            )
            val taskerDescription = stringResource(R.string.auto_advance_tasker)
            val taskerName = stringResource(R.string.auto_advance_tasker_name)
            val taskerText = remember(taskerDescription, taskerName) {
                buildAnnotatedString {
                    append(taskerDescription.substringBefore(taskerName))
                    withStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            fontStyle = FontStyle.Italic,
                        )
                    ) {
                        append(taskerName)
                    }
                    append(taskerDescription.substringAfter(taskerName))
                }
            }
            Text(
                text = taskerText,
                modifier = Modifier
                    .clickable(onClick = onOpenTasker)
                    .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp),
            )
        }
    }
}

@Preview(name = "Portrait", device = PHONE)
@Composable
fun AutoAdvancePreview() {
    AppTheme(
        dynamicColor = false
    ) {
        val defaultAdvanceOnWifi = false
        var advanceOnWifi by remember { mutableStateOf(defaultAdvanceOnWifi) }
        val defaultOrderingOption = stringResource(R.string.auto_advance_ordering_new_in_order)
        var orderingOption by remember { mutableStateOf(defaultOrderingOption) }
        val defaultIntervalOption = stringResource(R.string.auto_advance_interval_1h)
        var intervalSelectedOption by remember { mutableStateOf(defaultIntervalOption) }

        AutoAdvance(
            advanceOnWifi = advanceOnWifi,
            onAdvanceOnWifiChanged = { advanceOnWifi = it },
            orderingSelectedOption = orderingOption,
            onOrderingSelectedOptionChange = { orderingOption = it },
            intervalSelectedOption = intervalSelectedOption,
            onIntervalSelectedOptionChange = { intervalSelectedOption = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}