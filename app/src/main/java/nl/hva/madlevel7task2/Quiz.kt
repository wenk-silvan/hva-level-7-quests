package nl.hva.madlevel7task2

import com.google.firebase.storage.StorageReference

data class Quiz(
    val question: String,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val result: String,
    val imgStorageReference: StorageReference,
)