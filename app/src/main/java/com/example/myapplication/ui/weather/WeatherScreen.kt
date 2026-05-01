package com.example.myapplication.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.di.NetworkModule
import com.example.myapplication.model.*

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(
            weatherRepository = WeatherRepository(apiService = NetworkModule.weatherApiService)
        )
    )

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onStateSelected = { stateName ->
                navController.navigate("detail/$stateName")
            })
        }
        composable(
            "detail/{stateName}",
            arguments = listOf(navArgument("stateName") { type = NavType.StringType })
        ) { backStackEntry ->
            val stateName = backStackEntry.arguments?.getString("stateName")
            val stateInfo = indianStates.find { it.name == stateName }
            if (stateInfo != null) {
                DetailScreen(
                    stateInfo = stateInfo,
                    viewModel = weatherViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onStateSelected: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather AI") })
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(indianStates) { state ->
                StateCard(state = state, onClick = { onStateSelected(state.name) })
            }
        }
    }
}

@Composable
fun StateCard(state: StateInfo, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() }
    ) {
        Box {
            AsyncImage(
                model = state.imageUrl,
                placeholder = painterResource(id = R.drawable.state_placeholder),
                error = painterResource(id = R.drawable.state_placeholder),
                contentDescription = state.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Surface(
                color = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxSize()
            ) {}
            Text(
                text = state.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(stateInfo: StateInfo, viewModel: WeatherViewModel, onBack: () -> Unit) {
    val weatherState by viewModel.weatherState.collectAsState()

    LaunchedEffect(stateInfo) {
        viewModel.fetchWeather(stateInfo.latitude, stateInfo.longitude)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stateInfo.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = weatherState) {
                is WeatherViewModel.WeatherState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is WeatherViewModel.WeatherState.Success -> {
                    WeatherDetailContent(stateInfo, state.weatherData)
                }
                is WeatherViewModel.WeatherState.Error -> {
                    Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun WeatherDetailContent(stateInfo: StateInfo, weatherData: WeatherResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(24.dp)) {
                Box(modifier = Modifier.height(250.dp)) {
                    AsyncImage(
                        model = stateInfo.imageUrl,
                        placeholder = painterResource(id = R.drawable.state_placeholder),
                        error = painterResource(id = R.drawable.state_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxSize()
                    ) {}
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${weatherData.current?.temperature ?: "--"}°C",
                            color = Color.White,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = weatherData.current?.weatherCode?.toWeatherDescription() ?: "Unknown",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                InfoTile("Humidity", "${weatherData.current?.humidity ?: "--"}%")
                InfoTile("Wind", "${weatherData.current?.windSpeed ?: "--"} km/h")
            }
        }

        item {
            Text("Hourly Forecast", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val hourly = weatherData.hourly
                if (hourly != null) {
                    val itemCount = if (hourly.time.size > 24) 24 else hourly.time.size
                    items(itemCount) { index ->
                        HourlyItem(
                            time = hourly.time[index].substringAfter("T"),
                            temp = hourly.temperatures[index],
                            code = hourly.weatherCodes[index]
                        )
                    }
                }
            }
        }

        item {
            Text("7-Day Forecast", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        val daily = weatherData.daily
        if (daily != null) {
            items(daily.time.size) { index ->
                DailyItem(
                    date = daily.time[index],
                    maxTemp = daily.maxTemps[index],
                    minTemp = daily.minTemps[index],
                    code = daily.weatherCodes[index]
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun InfoTile(label: String, value: String) {
    Card(modifier = Modifier.width(150.dp)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 14.sp, color = Color.Gray)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HourlyItem(time: String, temp: Double, code: Int) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(time, fontSize = 12.sp)
            Text("${temp}°", fontWeight = FontWeight.Bold)
            Text(code.toWeatherDescription(), fontSize = 10.sp)
        }
    }
}

@Composable
fun DailyItem(date: String, maxTemp: Double, minTemp: Double, code: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(date, modifier = Modifier.weight(1f))
            Text(code.toWeatherDescription(), modifier = Modifier.weight(1.5f), fontSize = 14.sp)
            Text("${maxTemp.toInt()}° / ${minTemp.toInt()}°", fontWeight = FontWeight.Bold)
        }
    }
}
