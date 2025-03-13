package com.example.readingdiary.ui.compose

import android.widget.RatingBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.readingdiary.enums.BookRating
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.Book

@Composable
fun ComposeBookItem(
    book: Book,
    onDeleteClick: (Book) -> Unit,
    onRatingChanged: (Book, BookRating) -> Unit,
    onCheckboxChanged: (Book, Boolean) -> Unit,
) {
    val checked = remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = book.title,
                        fontSize = 18.sp,
                        color = colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    if (book.status == ReadingStatus.NOT_STARTED) {
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = { isChecked ->
                                checked.value = isChecked
                                onCheckboxChanged(book, isChecked)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = colors.primary,
                                uncheckedColor = colors.secondary,
                                checkmarkColor = colors.onPrimary
                            )
                        )
                    }
                }

                Text(
                    text = "Author: ${book.author}",
                    fontSize = 14.sp,
                    color = colors.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(
                            color = when (book.status) {
                                ReadingStatus.NOT_STARTED -> colors.primaryContainer
                                ReadingStatus.IN_PROGRESS -> colors.secondaryContainer
                                ReadingStatus.COMPLETED -> colors.tertiaryContainer
                                ReadingStatus.ON_HOLD -> colors.errorContainer
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Status: ${book.status.name}",
                        color = when (book.status) {
                            ReadingStatus.NOT_STARTED -> colors.primary
                            ReadingStatus.IN_PROGRESS -> colors.onSecondaryContainer
                            ReadingStatus.COMPLETED -> colors.onTertiaryContainer
                            ReadingStatus.ON_HOLD -> colors.error
                        },
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "${book.pages} pages",
                    fontSize = 14.sp,
                    color = colors.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )

                AndroidView(
                    factory = { context ->
                        RatingBar(context).apply {
                            numStars = 5
                            stepSize = 1f
                            rating = book.getRatingValue().toFloat()
                            setOnRatingBarChangeListener { _, rating, _ ->
                                val bookRating = BookRating.fromInt(rating.toInt() - 1)
                                onRatingChanged(book, bookRating)
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onDeleteClick(book) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Text("Delete", color = colors.onError)
                    }
                }
            }
        }
    }
}