package com.example.woof.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.woof.R

data class Dog(
    val id: Int,
    @StringRes val name: Int,
    val age: Int,
    @DrawableRes val imageResourceId: Int,
    @StringRes val description: Int,
    val mood: Mood
)

enum class Mood(val displayName: String) {
    HAPPY("Happy"),
    SLEEPY("Sleepy"),
    PLAYFUL("Playful")
}

val dogs = listOf(
    Dog(1, R.string.dog_name_1, 2, R.drawable.koda, R.string.dog_description_1, Mood.HAPPY),
    Dog(2, R.string.dog_name_2, 16, R.drawable.lola, R.string.dog_description_2, Mood.SLEEPY),
    Dog(3, R.string.dog_name_3, 2, R.drawable.frankie, R.string.dog_description_3, Mood.PLAYFUL),
    Dog(4, R.string.dog_name_4, 8, R.drawable.nox, R.string.dog_description_4, Mood.HAPPY),
    Dog(5, R.string.dog_name_5, 8, R.drawable.faye, R.string.dog_description_5, Mood.SLEEPY),
    Dog(6, R.string.dog_name_6, 14, R.drawable.bella, R.string.dog_description_6, Mood.HAPPY),
    Dog(7, R.string.dog_name_7, 2, R.drawable.moana, R.string.dog_description_7, Mood.PLAYFUL),
    Dog(8, R.string.dog_name_8, 7, R.drawable.tzeitel, R.string.dog_description_8, Mood.SLEEPY),
    Dog(9, R.string.dog_name_9, 4, R.drawable.leroy, R.string.dog_description_9, Mood.HAPPY)
)
