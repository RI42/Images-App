package com.example.myapplication.model

enum class ImageState(val id: Int) {
    NOT_SHOWN(0),
    LIKE(1),
    DISLIKE(2);

    companion object {
        val map = values().associateBy { it.id }
    }
}