package com.example.myapplication.FirstApp

data class AppUiState (
    val histList: List<String> = emptyList(),
    val currentOutput: String = ""
)
