package com.example.myapplication.FirstApp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    var inputText by mutableStateOf("")
        private set
    var actionText by mutableStateOf("")
        private set

    fun runAction() {
        val result = when (actionText) {
            "count-letter-digit" -> countDigits(inputText)
            "remove-even" -> filterEvenNumbers(inputText)
            else -> ""
        }
        val newHistList = _uiState.value.histList + result
        updateUiState(newHistList, result)
    }

    private fun updateUiState(newHistList: List<String>, newOutput: String) {
        _uiState.value = AppUiState(newHistList, newOutput)
        Log.d("DebugUiState", "${_uiState.value.histList}")
    }

    fun updateInputText(newInputText: String) {
        inputText = newInputText
    }

    fun updateActionText(newActionText: String) {
        actionText = newActionText
    }

    private fun countDigits(str: String): String {
        Log.d("Debugging", "$str")
        try {
            var count = 0
            for (c in str) {
                if (c.isDigit()) {
                    count++
                }
            }
            return count.toString()

        } catch (e: Exception) {
            Log.d("Debugging", "${e.message}")
            throw e
        }
    }

    private fun filterEvenNumbers(numbers: String): String {
        val numbersList = numbers.split(",").map { it.toInt() }
        val oddNumbers = numbersList.filter { it % 2 == 1 }
        return oddNumbers.joinToString(",")
    }
    fun deleteOutput(outputIndex: Int) {
        Log.d("DebugRemoveOutput", "current state: ${_uiState.value.histList}, remove at index $outputIndex")
        val newList = _uiState.value.histList.toMutableList().apply { removeAt(outputIndex) }
        updateUiState(newList, newList.lastOrNull() ?: "")
        Log.d("DebugRemoveOutput", "after remove: ${_uiState.value.histList}")
    }
    fun deleteAllOutput() {
        val newList = emptyList<String>()
        updateUiState(newList, "")
    }

}