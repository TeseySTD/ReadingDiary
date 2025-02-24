package com.example.readingdiary.enums

enum class BookRating {
    ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS;

    companion object {
        fun fromInt(value: Int) : BookRating{
            if(value > BookRating.entries.max().ordinal || value < BookRating.entries.min().ordinal)
                return ONE_STAR
            return BookRating.entries.first {it.ordinal == value }
        }
    }
}