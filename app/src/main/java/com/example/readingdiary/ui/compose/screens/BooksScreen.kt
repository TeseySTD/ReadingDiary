package com.example.readingdiary.ui.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.Book
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.BookRepository
import com.example.readingdiary.repo.ReadingPlanRepository
import com.example.readingdiary.ui.compose.components.ComposeBookItem
import kotlinx.coroutines.launch

@Composable
fun BooksScreen() {
    val bookRepository = BookRepository.getInstance()
    val readingPlanRepository = ReadingPlanRepository.getInstance()
    val books = bookRepository.getBooksLiveData().observeAsState(initial = emptyList())

    var selectedBooks by remember { mutableStateOf(setOf<Book>()) }
    val showMakeReadingPlanButton = selectedBooks.isNotEmpty()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
                    selectedBooks = if (isSelected) {
                        selectedBooks + book
                    } else {
                        selectedBooks - book
                    }
                },
                onDeleteClick = { book ->
                    bookRepository.removeBook(book)
                    if (selectedBooks.contains(book)) {
                        selectedBooks = selectedBooks - book
                    }
                },
                onRatingChanged = { book, rating ->
                    bookRepository.changeBookRating(book, rating)
                }
            )

            if (showMakeReadingPlanButton) {
                MakeBookPlanButton(
                    onClick = {
                        if (selectedBooks.isNotEmpty()) {
                            val readingPlan = ReadingPlan(selectedBooks.toList())
                            readingPlanRepository.addReadingPlan(readingPlan)

                            selectedBooks.forEach {
                                bookRepository.changeBookStatus(it, ReadingStatus.IN_PROGRESS)
                            }

                            // Clear selection
                            selectedBooks = emptySet()

                            scope.launch {
                                snackbarHostState.showSnackbar("Reading plan created")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("No books selected")
                            }
                        }
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
    LazyColumn {
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