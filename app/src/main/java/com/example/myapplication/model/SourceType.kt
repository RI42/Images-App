package com.example.myapplication.model

enum class SourceType(val id: Int) {
    CAT(1),
    DOG(2);

    companion object {
        val map = values().associateBy { it.id }
    }
}
