package com.example.myapplication.FirstApp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun FirstApp(
    appViewModel: AppViewModel = viewModel(), navController: NavHostController = rememberNavController()
) {
    val appUiState by appViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "HomeScreen",
    ) {
        composable("HomeScreen") {
            HomeScreen(inputText = appViewModel.inputText,
                actionText = appViewModel.actionText,
                onInputTextChange = { appViewModel.updateInputText(it) },
                onActionTextChange = { appViewModel.updateActionText(it) },
                onActionButtonClicked = { appViewModel.runAction() },
                output = appUiState.currentOutput,
                onHistoryButtonClicked = {
                    navController.navigate("HistoryScreen")
                })
        }
        composable("HistoryScreen") {
            HistoryScreen(
                outputs = appUiState.histList,
                onDeleteButtonClicked = { appViewModel.deleteOutput(it) },
                onBackButtonClicked = { navController.navigateUp() },
                onDeleteAllButtonClicked = { appViewModel.deleteAllOutput() }
            )
        }
    }

}

@Composable
fun HomeScreen(
    inputText: String,
    actionText: String,
    onInputTextChange: (String) -> Unit = {},
    onActionTextChange: (String) -> Unit = {},
    onActionButtonClicked: () -> Unit = {},
    output: String? = null,
    onHistoryButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        AppLayout(
            inputText = inputText,
            actionText = actionText,
            onInputTextChange = onInputTextChange,
            onActionTextChange = onActionTextChange
        )
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onActionButtonClicked, elevation = ButtonDefaults.elevation(4.dp)
            ) {
                Text("Submit")
            }
        }
        Spacer(Modifier.height(10.dp))
        OutputLayout(output, onHistoryButtonClicked)
    }
}

@Composable
private fun OutputLayout(
    output: String?, onHistoryButtonClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
    ) {
        Text("Output", fontSize = 17.sp)
    }
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
    ) {
        if (output != null) {
            Text(output, style = TextStyle(color = Color.Red, fontSize = 22.sp))
        }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier.size(30.dp)
            ) {
                FloatingActionButton(
                    onClick = onHistoryButtonClicked, backgroundColor = Color.Gray, contentColor = Color.Black
                ) {
                    Icon(
                        painterResource(R.drawable.baseline_bookmark_24), "History"
                    )
                }
            }
        }
    }
}

@Composable
private fun AppLayout(
    inputText: String,
    actionText: String,
    onInputTextChange: (String) -> Unit,
    onActionTextChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f).height(50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Trần Đình Minh Khoa")
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Input:")
        TextField(value = inputText, onValueChange = onInputTextChange)

    }
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Action:")
        TextField(value = actionText, onValueChange = onActionTextChange)
    }
}

@Composable
private fun HistoryScreen(
    outputs: List<String>,
    onDeleteButtonClicked: (Int) -> Unit = {},
    onBackButtonClicked: () -> Unit = {},
    onDeleteAllButtonClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = { Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            IconButton(onClick = onBackButtonClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            IconButton(onClick = onDeleteAllButtonClicked) {
                Icon(painterResource(R.drawable.baseline_delete_forever_24), "Delete all")
            }
        } }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            Log.d("DebugRemoveOutput", "Check again: currently having ${outputs.size} output")
            Log.d("DebugRemoveOutput", "Check again: current output is $outputs")
            itemsIndexed(outputs.toList()) { index, output ->
                Card(
                    border = BorderStroke(2.dp, Color.Red),
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(output, style = TextStyle(fontSize = 20.sp, color = Color.Red))
                        IconButton(onClick = {
                            onDeleteButtonClicked(index)
                        }) {
                            Icon(painterResource(R.drawable.baseline_delete_forever_24), "Delete")
                        }
                    }

                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistory() {
    MyApplicationTheme {
        HistoryScreen(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
    }
}