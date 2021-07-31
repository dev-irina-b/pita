package ru.devcold.pita

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @DocumentId
    val id: String = "",
    val photos: List<String> = listOf(""),
    val previewPhotoIndex: Int = 0,
    val title: String = "",
): Parcelable