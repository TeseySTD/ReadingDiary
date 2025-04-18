package com.example.readingdiary.ui.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.models.Book
import com.example.readingdiary.ui.compose.components.ComposeBookItem
import com.example.readingdiary.view_models.BooksScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BooksScreen(
    viewModel: BooksScreenViewModel = viewModel()
) {
    val books = viewModel.books.observeAsState(initial = emptyList())
    val selectedBooks by viewModel.selectedBooks.observeAsState(initial = emptySet())
    val showMakeReadingPlanButton = selectedBooks.isNotEmpty()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is BooksScreenViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BooksList(
                books = books.value,
                onBookSelected = { book, isSelected ->
                    viewModel.toggleBookSelection(book, isSelected)
                },
                onDeleteClick = { book ->
                    viewModel.deleteBook(book)
                },
                onRatingChanged = { book, rating ->
                    viewModel.changeBookRating(book, rating)
                }
            )

            if (showMakeReadingPlanButton) {
                MakeBookPlanButton(
                    onClick = {
                        viewModel.createReadingPlan()
                    }
                )
            }
        }
    }
}

@Composable
fun BooksList(
    books: List<Book>,
    onBookSelected: (Book, Boolean) -> Unit,
    onDeleteClick: (Book) -> Unit,
    onRatingChanged: (Book, BookRating) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState
    ) {
        items(books) { book ->
            ComposeBookItem(
                book = book,
                onCheckboxChanged = { book, isChecked ->
                    onBookSelected(book, isChecked)
                },
                onDeleteClick = { onDeleteClick(book) },
                onRatingChanged = { book, rating -> onRatingChanged(book, rating) }
            )
        }
    }
}

@Composable
fun MakeBookPlanButton(
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            content = { Text("Make reading plan") }
        )
    }
}