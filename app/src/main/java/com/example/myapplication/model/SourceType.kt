package com.example.myapplication.model

enum class SourceType(val id: String) {
    CAT("cat"),
    DOG("dog");

    companion object {
        val map = values().associateBy { it.id }
    }
}
