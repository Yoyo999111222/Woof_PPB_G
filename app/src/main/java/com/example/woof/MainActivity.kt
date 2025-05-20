package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.woof.data.Dog
import com.example.woof.data.Mood
import com.example.woof.data.dogs
import com.example.woof.ui.theme.WoofTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var selectedMood by remember { mutableStateOf<Mood?>(null) }

            WoofTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WoofApp(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme },
                        selectedMood = selectedMood,
                        onMoodSelected = { selectedMood = it }
                    )
                }
            }
        }
    }
}

@Composable
fun WoofApp(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    selectedMood: Mood?,
    onMoodSelected: (Mood?) -> Unit
) {
    var selectedDog by remember { mutableStateOf<Dog?>(null) } // state untuk dog yang diklik

    Scaffold(
        topBar = {
            WoofTopAppBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                selectedMood = selectedMood,
                onMoodSelected = onMoodSelected
            )
        }
    ) { paddingValues ->
        val filteredDogs = dogs.filter { selectedMood == null || it.mood == selectedMood }

        LazyColumn(contentPadding = paddingValues) {
            items(filteredDogs) { dog ->
                DogItem(
                    dog = dog,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    onClick = { selectedDog = dog }  // handle klik
                )
            }
        }

        // Tampilkan dialog jika ada dog yang dipilih
        if (selectedDog != null) {
            DogDetailDialog(dog = selectedDog!!, onDismiss = { selectedDog = null })
        }
    }
}

@Composable
fun WoofTopAppBar(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    selectedMood: Mood?,
    onMoodSelected: (Mood?) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.image_size))
                        .padding(dimensionResource(R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_woof_logo),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.weight(1f)
                )
                // Mood filter dropdown (posisi kiri)
                MoodDropdown(selectedMood, onMoodSelected)
                // Spacer agar toggle ada di paling kanan
                Spacer(modifier = Modifier.width(16.dp))
                // Toggle Dark Mode dengan label
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isDarkTheme) "Dark" else "Light")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onToggleTheme() },
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun MoodDropdown(selectedMood: Mood?, onMoodSelected: (Mood?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedMood?.displayName ?: "All Moods")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("All Moods") },
                onClick = {
                    onMoodSelected(null)
                    expanded = false
                }
            )
            Mood.values().forEach { mood ->
                DropdownMenuItem(
                    text = { Text(mood.displayName) },
                    onClick = {
                        onMoodSelected(mood)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DogDetailDialog(dog: Dog, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text(text = stringResource(dog.name))
        },
        text = {
            Column {
                Image(
                    painter = painterResource(dog.imageResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Age: ${dog.age} years")
                Text(text = "Mood: ${dog.mood.displayName}")
                Text(text = stringResource(dog.description))
            }
        }
    )
}

@Composable
fun DogItem(dog: Dog, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clickable { onClick() }  // panggil onClick dari parameter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            DogIcon(dog.imageResourceId)
            DogInformation(dog.name, dog.age)
        }
    }
}

@Composable
fun DogIcon(@DrawableRes dogIcon: Int, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        painter = painterResource(dogIcon),
        contentDescription = null
    )
}

@Composable
fun DogInformation(@StringRes dogName: Int, dogAge: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(dogName),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp(false, {}, null, {})
    }
}

@Preview
@Composable
fun WoofDarkPreview() {
    WoofTheme(darkTheme = true) {
        WoofApp(true, {}, null, {})
    }
}
