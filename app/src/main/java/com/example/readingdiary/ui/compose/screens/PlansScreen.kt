package com.example.readingdiary.ui.compose.screens

import com.example.readingdiary.ui.compose.components.ComposePlanItem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import com.example.readingdiary.models.ReadingPlan
import com.example.readingdiary.view_models.PlansScreenViewModel

@Composable
fun PlansScreen(
    viewModel: PlansScreenViewModel = viewModel()
) {
    val plans by viewModel.readingPlans.observeAsState(emptyList())

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PlansList(
                plans = plans,
                onCompletePlan = { plan ->
                    viewModel.completeReadingPlan(plan)
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
        items(
            items = plans,
            key = {plan -> plan.id}
        ) { plan ->
            ComposePlanItem(
                plan = plan,
                onCompletePlan = { onCompletePlan(plan) }
            )
        }
    }
}
