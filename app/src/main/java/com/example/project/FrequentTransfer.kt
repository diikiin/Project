package com.example.project

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class FrequentTransfer(
    val imageId: Int = -1,
    var title: String = "",
    var name: String = "",
    val phoneNumber: String = ""
) : Serializable
