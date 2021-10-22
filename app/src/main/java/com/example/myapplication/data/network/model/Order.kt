package com.example.myapplication.data.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class Order {
    RAND,
    DESC,
    ASC
}