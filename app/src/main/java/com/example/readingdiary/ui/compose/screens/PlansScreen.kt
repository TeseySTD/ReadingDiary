package com.example.readingdiary.ui.compose.screens

import com.example.readingdiary.ui.compose.components.ComposePlanItem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.repo.ReadingPlanRepository


@Composable
fun PlansScreen() {
    val plansRepository = ReadingPlanRepository.getInstance()
    val plans = plansRepository.getReadingPlansLiveData().observeAsState(initial = emptyList())

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PlansList(
                plans = plans.value,
                onCompletePlan = { plan ->
                    plansRepository.completeReadingPlan(plan)
                }
            )
        }
    }
}

@Composable
fun PlansList(
    plans: List<ReadingPlan>,
    onCompletePlan: (ReadingPlan) -> Unit
) {
    LazyColumn {
        items(plans) { plan ->
            ComposePlanItem(
                plan = plan,
                onCompletePlan = { onCompletePlan(plan) }
            )
        }
    }
}
