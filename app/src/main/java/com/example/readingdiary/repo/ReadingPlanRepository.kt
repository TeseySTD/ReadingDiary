package com.example.readingdiary.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.readingdiary.enums.ReadingStatus
import com.example.readingdiary.models.ReadingPlan

class ReadingPlanRepository private constructor() {

    private val readingPlans = mutableListOf<ReadingPlan>()
    private val readingPlansLiveData = MutableLiveData<List<ReadingPlan>>(readingPlans)

    fun addReadingPlan(readingPlan: ReadingPlan) {
        readingPlans.add(readingPlan)
        readingPlansLiveData.value = readingPlans
    }

    fun getReadingPlansLiveData(): LiveData<List<ReadingPlan>> = readingPlansLiveData

    fun completeReadingPlan(readingPlan: ReadingPlan) {
        val bookRepo = BookRepository.getInstance()
        readingPlan.getBooks().forEach{
            bookRepo.changeBookStatus(it,ReadingStatus.COMPLETED)
        }

        readingPlans.remove(readingPlan)
        readingPlansLiveData.value = readingPlans
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