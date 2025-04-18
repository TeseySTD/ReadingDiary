package com.example.readingdiary.view_models

import androidx.lifecycle.ViewModel
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.ReadingPlanRepository

class PlansScreenViewModel : ViewModel() {
    private val readingPlanRepository = ReadingPlanRepository.getInstance()
    var readingPlans = readingPlanRepository.getReadingPlansLiveData()

    fun completeReadingPlan(plan: ReadingPlan) {
        readingPlanRepository.completeReadingPlan(plan)
    }
}