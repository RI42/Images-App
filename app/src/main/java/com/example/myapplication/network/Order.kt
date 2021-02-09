package com.example.myapplication.network

import kotlinx.serialization.Serializable

@Serializable
enum class Order {
    RAND,
    DESC,
    ASC
}