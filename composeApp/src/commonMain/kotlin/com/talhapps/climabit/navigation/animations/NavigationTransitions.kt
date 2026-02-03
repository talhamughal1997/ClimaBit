package com.talhapps.climabit.navigation.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

private const val NAVIGATION_ANIMATION_DURATION = 300

fun forwardTransitionSpec() = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
)

fun backTransitionSpec() = slideInHorizontally(
    initialOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
)

fun predictiveBackTransitionSpec() = scaleIn(
    initialScale = 0.95f,
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) togetherWith scaleOut(
    targetScale = 0.95f,
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = FastOutSlowInEasing
    )
)

