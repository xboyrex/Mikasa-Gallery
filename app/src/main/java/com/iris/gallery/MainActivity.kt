@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.iris.gallery

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iris.gallery.data.SettingsPreferences
import com.iris.gallery.ui.GalleryApp
import com.iris.gallery.ui.glass.GlassQuality
import com.iris.gallery.ui.glass.GlassSettings
import com.iris.gallery.ui.glass.Motion
import com.iris.gallery.ui.setAppLanguage
import com.iris.gallery.ui.theme.IrisTheme

class MainActivity : ComponentActivity() {
    private val currentIntentState = mutableStateOf<Intent?>(null)
    private val intentGenerationState = mutableStateOf(0L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.app_open_enter,
                R.anim.app_open_exit,
            )
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.app_close_enter,
                R.anim.app_close_exit,
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.app_open_enter, R.anim.app_open_exit)
        }

        currentIntentState.value = intent
        GlassSettings.init(this)
        GlassQuality.init(this)
        IrisPhotoWidget.refreshAll(this)
        enableEdgeToEdge()

        val settingsPreferences = SettingsPreferences(this)
        val initialSettings = settingsPreferences.state.value

        // Always pure AMOLED Pitch Black (#000000)
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.BLACK))
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        if (initialSettings.language.isNotEmpty()) {
            setAppLanguage(this, initialSettings.language)
        }

        setContent {
            val activeIntent = currentIntentState.value ?: intent
            val pickerMode = activeIntent.action == Intent.ACTION_PICK || activeIntent.action == Intent.ACTION_GET_CONTENT
            val isCameraReviewAction = activeIntent.action == "com.android.camera.action.REVIEW" ||
                activeIntent.action == "android.provider.MediaStore.ACTION_REVIEW"
            val isViewAction = activeIntent.action == Intent.ACTION_VIEW ||
                activeIntent.action == Intent.ACTION_EDIT ||
                isCameraReviewAction
            val requestedType = activeIntent.type
            val rawViewUri = activeIntent.data ?: activeIntent.clipData?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.uri
            val viewUri = rawViewUri.takeIf { isViewAction }
            val isEditAction = activeIntent.action == Intent.ACTION_EDIT
            val shouldOpenLatest = isCameraReviewAction && rawViewUri == null
            val intentGeneration = intentGenerationState.value

            val settings by settingsPreferences.state.collectAsStateWithLifecycle()

            var appEntered by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { appEntered = true }
            val openScale by animateFloatAsState(
                targetValue = if (appEntered) 1f else 0.94f,
                animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
                label = "appOpenScale",
            )
            val openAlpha by animateFloatAsState(
                targetValue = if (appEntered) 1f else 0f,
                animationSpec = tween(durationMillis = 220, easing = Motion.Emphasized),
                label = "appOpenAlpha",
            )

            IrisTheme(
                themeMode = settings.themeMode,
                amoledBlack = true,
                accentColor = settings.accentColor,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .graphicsLayer {
                            scaleX = openScale
                            scaleY = openScale
                            alpha = openAlpha
                        },
                ) {
                    GalleryApp(
                        settings = settings,
                        settingsPreferences = settingsPreferences,
                        requestedType = requestedType.takeIf { pickerMode },
                        initialViewUri = viewUri,
                        initialEditMode = isEditAction,
                        initialMemories = activeIntent.getBooleanExtra("open_memories", false),
                        initialOpenLatest = shouldOpenLatest,
                        initialIntentGeneration = intentGeneration,
                        onPick = if (pickerMode) {
                            { media ->
                                val result = Intent().apply {
                                    data = media.uri
                                    clipData = ClipData.newUri(contentResolver, media.name, media.uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                setResult(Activity.RESULT_OK, result)
                                finish()
                            }
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT < 34) {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.app_close_enter, R.anim.app_close_exit)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntentState.value = intent
        intentGenerationState.value += 1
    }
}
