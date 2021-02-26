/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ericktijerou.jetpuppy.ui.onboarding

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.ericktijerou.jetpuppy.R
import com.ericktijerou.jetpuppy.ui.theme.OrangeOnboarding
import com.ericktijerou.jetpuppy.ui.theme.PurpleOnboarding
import com.ericktijerou.jetpuppy.ui.theme.TealOnboarding

sealed class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @RawRes val animation: Int,
    val color: Color
) {
    object Page1 : OnboardingPage(R.string.label_title_onboarding1, R.string.label_subtitle_onboarding1, R.raw.doggy, PurpleOnboarding)
    object Page2 : OnboardingPage(R.string.label_title_onboarding2, R.string.label_subtitle_onboarding2, R.raw.dog, TealOnboarding)
    object Page3 : OnboardingPage(R.string.label_title_onboarding3, R.string.label_subtitle_onboarding3, R.raw.happy_dog, OrangeOnboarding)
}
