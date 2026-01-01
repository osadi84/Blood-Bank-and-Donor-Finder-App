package com.example.blooddonor.ui.screens



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blooddonor.ui.components.DonorCard
import com.example.blooddonor.ui.viewmodels.DonorUiState
import com.example.blooddonor.ui.viewmodels.DonorViewModel
import com.example.blooddonor.utils.openDialer
import com.example.blooddonor.utils.sendSMS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: DonorViewModel = hiltViewModel()) {
    var bloodType by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isBloodTypeExpanded by remember { mutableStateOf(false) }
    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val cities = listOf("Colombo", "Gampaha", "Kandy", "Galle", "Jaffna", "Kurunegala")

    val donors = viewModel.donors.value
    val uiState = viewModel.uiState.value

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.searchDonors()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Blood Donors") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Search Donors",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Blood Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isBloodTypeExpanded,
                        onExpandedChange = { isBloodTypeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = bloodType,
                            onValueChange = {},
                            label = { Text("Blood Type") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = isBloodTypeExpanded
                                )
                            },
                            placeholder = { Text("Select blood type") }
                        )

                        ExposedDropdownMenu(
                            expanded = isBloodTypeExpanded,
                            onDismissRequest = { isBloodTypeExpanded = false }
                        ) {
                            bloodTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        bloodType = type
                                        isBloodTypeExpanded = false
                                        viewModel.updateSearchQuery(bloodType = type, city = city)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // City Field
                    OutlinedTextField(
                        value = city,
                        onValueChange = {
                            city = it
                            viewModel.updateSearchQuery(bloodType = bloodType, city = it)
                        },
                        label = { Text("City (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = "City")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter city name") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.updateSearchQuery(bloodType = bloodType, city = city)
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${donors.size} donors found",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Results
            when (uiState) {
                is DonorUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is DonorUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as DonorUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    if (donors.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No donors found",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Try a different search criteria.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                            items(donors) { donor ->
                                DonorCard(
                                    donor = donor,
                                    onCallClick = { openDialer(context, donor.phone) },
                                    onMessageClick = { sendSMS(context, donor.phone) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}