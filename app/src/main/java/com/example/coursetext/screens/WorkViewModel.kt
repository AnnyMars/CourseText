package com.example.coursetext.screens

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursetext.db.HistoryDao
import com.example.coursetext.db.HistoryDatabase
import com.example.coursetext.db.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyDao: HistoryDao
) : ViewModel() {
    val items: StateFlow<List<Item>> = historyDao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insertItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            historyDao.insert(item)
        }
    }
}