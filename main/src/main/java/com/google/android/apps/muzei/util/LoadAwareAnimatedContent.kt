/*
 * Copyright 2026 Google Inc.
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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> LoadAwareAnimatedContent(
    targetState: T,
    label: String,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<T>.() -> ContentTransform,
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (T) -> Any? = { it },
    content: @Composable ((T, onLoadComplete: (T) -> Unit) -> Unit)
) {
    val transitionState = remember {
        SeekableTransitionState(targetState)
    }
    val transition = rememberTransition(transitionState, label = label)
    var lastFullyLoadedState by remember { mutableStateOf<T?>(null) }
    LaunchedEffect(targetState, lastFullyLoadedState) {
        if (targetState != lastFullyLoadedState) {
            transitionState.seekTo(0f, targetState)
        } else {
            transitionState.animateTo(targetState)
        }
    }
    transition.AnimatedContent(
        modifier = modifier,
        transitionSpec = transitionSpec,
        contentAlignment = contentAlignment,
        contentKey = contentKey,
    ) { state ->
        content(state) { fullyLoadedState ->
            lastFullyLoadedState = fullyLoadedState
        }
    }
}
