package com.example.readingdiary.repo

import com.example.readingdiary.models.ReadingPlan

class ReadingPlanRepository private constructor() {

    private val readingPlans = mutableListOf<ReadingPlan>()

    fun addReadingPlan(readingPlan: ReadingPlan) {
        readingPlans.add(readingPlan)
    }

    fun getReadingPlans(): List<ReadingPlan> = readingPlans

    fun removeReadingPlan(readingPlan: ReadingPlan) {
        readingPlans.remove(readingPlan)
    }

    companion object {
        private var instance: ReadingPlanRepository? = null

        fun getInstance(): ReadingPlanRepository {
            if (instance == null) {
                instance = ReadingPlanRepository()
            }
            return instance!!
        }
    }
}