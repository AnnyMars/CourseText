package com.example.coursetext.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coursetext.Functions
import com.example.coursetext.db.Item
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkScreen(functions: Functions, viewModel: HistoryViewModel) {
    val A = remember {
        mutableStateOf(TextFieldValue())
    }
    val b = remember {
        mutableStateOf(TextFieldValue())
    }
    val maxIterations = remember {
        mutableStateOf(TextFieldValue())
    }
    val tolerance = remember {
        mutableStateOf(TextFieldValue())
    }
    val solution = remember {
        mutableStateOf("")
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    var edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val itemsList = viewModel.items.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)

    ) {
        TextField(
            value = A.value,
            onValueChange = { A.value = it },
            label = { Text("Матрица коэффициентов A") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = b.value,
            onValueChange = { b.value = it },
            label = { Text("Вектор свободных членов b") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = maxIterations.value,
            onValueChange = { maxIterations.value = it },
            label = { Text("Максимальное количество итераций") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = tolerance.value,
            onValueChange = { tolerance.value = it },
            label = { Text("Допустимая погрешность") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val matrixA = functions.parseMatrix(A.value.text)
                val vectorB = functions.parseVector(b.value.text)

                if (matrixA != null && vectorB != null) {
                    if (matrixA.size == vectorB.size) {
                        val iterations = maxIterations.value.text.toIntOrNull()
                        val tol = tolerance.value.text.toDoubleOrNull()

                        if (iterations != null && tol != null) {
                            val result = functions.solveLinearEquations(matrixA, vectorB, iterations, tol)

                            solution.value = if (result != null) {
                                viewModel.insertItem(Item(0, result.contentToString()))
                                "Решение: ${result.contentToString()}"
                            } else {
                                "Не удалось найти решение"
                            }
                        } else {
                            solution.value = "Ошибка: Пожалуйста, введите корректные значения для максимального количества итераций и допустимой погрешности"
                        }
                    } else {
                        solution.value = "Ошибка: Размерности матрицы коэффициентов A и вектора свободных членов b должны соответствовать"
                    }
                } else {
                    solution.value = "Ошибка: Пожалуйста, введите корректные значения для матрицы коэффициентов A и вектора свободных членов b"
                }
            }
        ) {
            Text("Решить")
        }
        
        Button(onClick = { openBottomSheet = !openBottomSheet }) {
            Text(text = "Посмотреть историю")
        }

        if (openBottomSheet) {
            val windowInsets = if (edgeToEdgeEnabled)
                WindowInsets(0) else BottomSheetDefaults.windowInsets

            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = windowInsets
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    openBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Text("Спрятать историю")
                    }
                }
                LazyColumn {
                    items(itemsList.value) {
                        ItemCard(text = it.value)
                    }
                }
            }
        }

        Text(
            text = solution.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

    }

}

@Composable
fun ItemCard(text: String){
    Card(
        border = BorderStroke(1.dp, color = Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}


@Preview
@Composable
fun PreviewCard(){
    ItemCard(text = "wwwwwwwwqdqwdqwdqwdqwdqwdqwdqdqwdqwdqd")
}


