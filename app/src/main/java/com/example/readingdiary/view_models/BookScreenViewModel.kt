package com.example.readingdiary.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.Book
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.BookRepository
import com.example.readingdiary.repo.ReadingPlanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class BooksScreenViewModel : ViewModel() {
    private val bookRepository = BookRepository.getInstance()
    private val readingPlanRepository = ReadingPlanRepository.getInstance()

    val books: LiveData<List<Book>> = bookRepository.getBooksLiveData()

    private val _selectedBooks = MutableLiveData<Set<Book>>(emptySet())
    val selectedBooks: LiveData<Set<Book>> = _selectedBooks

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    fun toggleBookSelection(book: Book, isSelected: Boolean) {
        val currentSelection = _selectedBooks.value ?: emptySet()
        _selectedBooks.value = if (isSelected) {
            currentSelection + book
        } else {
            currentSelection - book
        }
    }

    fun deleteBook(book: Book) {
        bookRepository.removeBook(book)

        val currentSelection = _selectedBooks.value ?: emptySet()
        if (currentSelection.contains(book)) {
            _selectedBooks.value = currentSelection - book
        }
    }

    fun changeBookRating(book: Book, rating: BookRating) {
        bookRepository.changeBookRating(book, rating)
    }

    fun createReadingPlan() {
        val booksToAdd = _selectedBooks.value ?: emptySet()

        if (booksToAdd.isNotEmpty()) {
            val readingPlan = ReadingPlan(booksToAdd.toList())
            readingPlanRepository.addReadingPlan(readingPlan)

            booksToAdd.forEach {
                bookRepository.changeBookStatus(it, ReadingStatus.IN_PROGRESS)
            }

            _selectedBooks.value = emptySet()

            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("Reading plan created"))
            }
        } else {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("No books selected"))
            }
        }
    }

    fun clearSelections() {
        _selectedBooks.value = emptySet()
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}