package com.myproject.gymphysique.feature.charts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myproject.gymphysique.core.designsystem.theme.Dimens
import com.myproject.gymphysique.core.designsystem.theme.GymPhysiqueTheme
import com.myproject.gymphysique.core.model.MeasurementType
import com.myproject.gymphysique.feature.charts.ChartsState
import com.myproject.gymphysique.feature.charts.components.ChartComponent
import com.myproject.gymphysique.feature.charts.components.ChartDropdownMenu
import com.myproject.gymphysique.feature.charts.components.DatePicker
import com.myproject.gymphysique.feature.charts.dialog.DatePickerDialog
import com.myproject.gymphysique.feature.charts.viewModel.ChartsViewModel

@Composable
internal fun ChartsRoute(
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    ChartsScreen(
        uiState,
        screenActions = ChartsScreenActions(
            onMeasurementDropdownSelected = viewModel::onMeasurementTypeDropdownSelected,
            onMeasurementTypeSelected = viewModel::onMeasurementTypeSelected,
            onDateDropdownSelected = viewModel::onDateDropdownSelected,
            onDateSelected = viewModel::onDateSelected
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartsScreen(
    uiState: ChartsState,
    screenActions: ChartsScreenActions
) {
    if (uiState.dropdownDateExpanded) {
        DatePickerDialog(
            onDateSelected = { date -> screenActions.onDateSelected(date) },
            onDismissed = { screenActions.onDateDropdownSelected() }
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ChartPropertiesComponent(
                uiState,
                screenActions
            )
            ChartComponent(
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimens.screenPadding),
                measurements = uiState.measurements
            )
        }
    }
}

@Composable
private fun ChartPropertiesComponent(
    uiState: ChartsState,
    screenActions: ChartsScreenActions
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = Dimens.screenPadding,
                    bottomEnd = Dimens.screenPadding
                )
            )
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(Dimens.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChartDropdownMenu(
            measurementTypes = MeasurementType.values().toList(),
            expanded = uiState.dropdownMeasurementTypeExpanded,
            selectedMeasurementType = uiState.selectedMeasurementType,
            onMeasurementTypeSelected = { screenActions.onMeasurementTypeSelected(it) },
            onDismissRequest = { screenActions.onMeasurementDropdownSelected() },
            onExpandedChange = { screenActions.onMeasurementDropdownSelected() }
        )
        Spacer(modifier = Modifier.height(Dimens.margin))
        DatePicker(
            date = uiState.selectedDate,
            expanded = uiState.dropdownDateExpanded,
            onClick = { screenActions.onDateDropdownSelected() }
        )
    }
}

@Preview
@Composable
private fun ChartsPreview() {
    GymPhysiqueTheme() {
        ChartsScreen(
            ChartsState(),
            ChartsScreenActions(
                {},
                {},
                {},
                {}
            )
        )
    }
}
