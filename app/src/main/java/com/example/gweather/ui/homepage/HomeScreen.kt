package com.example.gweather.ui.homepage

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.gweather.data.local.Weather
import com.google.android.gms.location.LocationServices
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.gweather.BuildConfig
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    uid: String,
    windowSizeClass: WindowSizeClass,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val apiKey = getApiKeyOrNull()


    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    var selectedTab by remember { mutableIntStateOf(0) }


    if (apiKey == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No API KEY", color = Color.Red, textAlign = TextAlign.Center)
        }
        return
    }


    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }


    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val loc = getLastKnownLocation(context)
            if (loc != null) {
                viewModel.fetchWeather(uid, loc.latitude, loc.longitude, apiKey)
            }
        }
    }


    LaunchedEffect(selectedTab) {
        if (selectedTab == 1 && viewModel.history.isEmpty()) {
            viewModel.loadHistory(uid)
        }
    }

    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    if (isExpanded) {

        Row(Modifier.fillMaxSize()) {
            Column(Modifier.weight(1f).padding(16.dp)) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(text = { Text("Current") }, selected = selectedTab == 0, onClick = { selectedTab = 0 })
                    Tab(text = { Text("History") }, selected = selectedTab == 1, onClick = { selectedTab = 1 })
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp), thickness = DividerDefaults.Thickness, color = DividerDefaults.color
            )

            Box(modifier = Modifier.weight(2f).padding(16.dp)) {
                when (selectedTab) {
                    0 -> WeatherTab(viewModel.current)
                    1 -> WeatherHistory(viewModel.history)
                }
            }
        }
    } else {

        Column(Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(text = { Text("Current") }, selected = selectedTab == 0, onClick = { selectedTab = 0 })
                Tab(text = { Text("History") }, selected = selectedTab == 1, onClick = { selectedTab = 1 })
            }

            when (selectedTab) {
                0 -> WeatherTab(viewModel.current)
                1 -> WeatherHistory(viewModel.history)
            }
        }
    }

    if (!locationPermissionState.status.isGranted) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Location permission is required to fetch weather data.",
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun WeatherTab(weather: Weather?) {
    if (weather == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val hour = LocalTime.now().hour
    val icon = when {
        weather.condition.contains("Rain", true) -> Icons.Default.WbCloudy
        weather.condition.equals("Clear", true) && hour >= 18 -> Icons.Default.NightlightRound
        else -> Icons.Default.WbSunny
    }


    Column(Modifier.padding(16.dp)) {
        Icon(icon, contentDescription = weather.condition)
        Text("${weather.city}, ${weather.country}", style = MaterialTheme.typography.titleMedium)
        Text("${weather.tempCelsius}°C", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Sunrise: ${formatTime(weather.sunriseTime)}")
        Text("Sunset: ${formatTime(weather.sunsetTime)}")
    }
}

@Composable
fun WeatherHistory(history: List<Weather>) {
    if (history.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No history available.")
        }
        return
    }

    LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
        items(history) { entry ->
            Card(Modifier.fillMaxWidth().padding(4.dp)) {
                Column(Modifier.padding(8.dp)) {
                    Text("${entry.city}, ${entry.country} - ${entry.tempCelsius}°C")
                    Text("Condition: ${entry.condition}")
                    Text("Fetched: ${formatTime(entry.timeFetched)}")
                }
            }
        }
    }
}


fun getApiKeyOrNull(): String? {
    return BuildConfig.apikey.takeIf { it.isNotBlank() }
}

suspend fun getLastKnownLocation(context: Context): Location? =
    suspendCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        try {
            client.lastLocation
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resume(null) }
        } catch (e: SecurityException) {
            cont.resume(null)
        }
    }

fun formatTime(unix: Long): String {
    val timeincurrentzone = Instant.ofEpochSecond(unix).atZone(ZoneId.systemDefault())
    return DateTimeFormatter.ofPattern("hh:mm a").format(timeincurrentzone)
}

fun formatDegrees(k: Double): String {
    return "%.1f°C".format(k - 273.15)
}

