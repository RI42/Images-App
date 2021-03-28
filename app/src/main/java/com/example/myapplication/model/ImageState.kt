package com.example.myapplication.model

enum class ImageState(val id: String) {
    NOT_SHOWN("not_shown"),
    LIKE("liked"),
    DISLIKE("disliked");

    companion object {
        val map = values().associateBy { it.id }
    }
}