package com.iris.gallery.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iris.gallery.BuildConfig
import com.iris.gallery.R
import com.iris.gallery.ui.glass.pressable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    padding: PaddingValues = PaddingValues(0.dp),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var eggClicks by remember { mutableIntStateOf(0) }
    var activeToast by remember { mutableStateOf<Toast?>(null) }
    var showEasterEggSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun openUrl(url: String) {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.section_about), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.action_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable {
                                eggClicks++
                                activeToast?.cancel()
                                if (eggClicks >= 5) {
                                    eggClicks = 0
                                    activeToast = Toast.makeText(context, context.getString(R.string.toast_optics_unlocked), Toast.LENGTH_SHORT).apply { show() }
                                    showEasterEggSheet = true
                                } else {
                                    val remaining = 5 - eggClicks
                                    val message = if (remaining == 1) context.getString(R.string.toast_easter_egg_hint_one)
                                    else context.getString(R.string.toast_easter_egg_hint, remaining)
                                    activeToast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { show() }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mikasa_logo),
                            contentDescription = "Mikasa Gallery Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        color = Color(0xFFFFD600).copy(alpha = 0.14f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFFFFD600).copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFFFFD600),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) • AMOLED Liquid Glass",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD600)
                            )
                        }
                    }
                    Text(
                        stringResource(R.string.app_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }

            item {
                Text(
                    "DEVELOPER & COMMUNITY",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0B0E)),
                    border = BorderStroke(1.dp, Color(0xFF24242C)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        AboutLinkRow(
                            icon = Icons.Outlined.Psychology,
                            title = "Developer",
                            subtitle = "Mayank (@xboyrex)"
                        ) { openUrl("https://github.com/xboyrex") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.Code,
                            title = "GitHub Repository",
                            subtitle = "github.com/xboyrex/Mikasa-Gallery"
                        ) { openUrl("https://github.com/xboyrex/Mikasa-Gallery") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.Forum,
                            title = "Telegram Updates & Community",
                            subtitle = "t.me/MayankDroid7 • t.me/OnyxHub7"
                        ) { openUrl("https://t.me/MayankDroid7") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.PlayCircle,
                            title = "YouTube Channels",
                            subtitle = "@mayankdroid • @onyxdroid89"
                        ) { openUrl("https://youtube.com/@mayankdroid?si=slX8DDU0dpLSIYyY") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.AlternateEmail,
                            title = "Instagram",
                            subtitle = "@mayank.zx7"
                        ) { openUrl("https://www.instagram.com/mayank.zx7") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.Email,
                            title = "Contact Email",
                            subtitle = "xboyrex496@gmail.com"
                        ) { openUrl("mailto:xboyrex496@gmail.com") }

                        HorizontalDivider(color = Color(0xFF24242C))

                        AboutLinkRow(
                            icon = Icons.Outlined.VerifiedUser,
                            title = stringResource(R.string.about_license_label),
                            subtitle = stringResource(R.string.about_license_name)
                        ) { openUrl("https://github.com/xboyrex/Mikasa-Gallery/blob/main/LICENSE") }
                    }
                }
            }

            item {
                Text(
                    stringResource(R.string.about_general_info).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp)
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0B0E)),
                    border = BorderStroke(1.dp, Color(0xFF24242C)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AppInfoItem(
                            stringResource(R.string.about_arch_title),
                            stringResource(R.string.about_arch_desc)
                        )
                        HorizontalDivider(color = Color(0xFF24242C))
                        AppInfoItem(
                            "Design System & Navigation",
                            "100% Pure AMOLED Pitch Black (#000000) + Onyx Yellow (#FFD600) with YTX 3-Layer Refractive Liquid Glass Navigation Bar"
                        )
                        HorizontalDivider(color = Color(0xFF24242C))
                        AppInfoItem(
                            stringResource(R.string.about_img_engine_title),
                            stringResource(R.string.about_img_engine_desc)
                        )
                        HorizontalDivider(color = Color(0xFF24242C))
                        AppInfoItem(
                            stringResource(R.string.about_vid_engine_title),
                            stringResource(R.string.about_vid_engine_desc)
                        )
                        HorizontalDivider(color = Color(0xFF24242C))
                        AppInfoItem(
                            stringResource(R.string.about_features_title),
                            stringResource(R.string.about_features_desc)
                        )
                    }
                }
            }
        }
    }

    if (showEasterEggSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEasterEggSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF0B0B0E)
        ) {
            MesopotamiaOpticsSheet(onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { showEasterEggSheet = false }
            })
        }
    }
}

@Composable
private fun AppInfoItem(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
        Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun AboutLinkRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD600).copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(20.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFF71717A),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun MesopotamiaOpticsSheet(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF141100),
                            Color(0xFF09090B),
                            Color(0xFF000000)
                        )
                    )
                )
                .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "𒀭 𒈹 𒌓 𒂗 𒆠",
                    fontSize = 24.sp,
                    color = Color(0xFFFFD600),
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "AL-HASAN IBN AL-HAYTHAM",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    "The Invention of the Camera & Optics",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFD600),
                    textAlign = TextAlign.Center
                )
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121216)),
            border = BorderStroke(1.dp, Color(0xFF24242C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        Icons.Outlined.Camera,
                        contentDescription = null,
                        tint = Color(0xFFFFD600),
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            "Kitāb al-Manāẓir (Book of Optics)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Iraq • 1011–1021 CE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD600)
                        )
                    }
                }
                Text(
                    "Born in Basra, Al-Hasan Ibn al-Haytham (Alhazen) proved that vision occurs when light rays reflect off objects and enter the eye's iris and pupil — laying the foundation for modern photography, lenses, and optical physics.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        FilledTonalButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Close")
        }
    }
}
