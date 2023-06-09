package com.myproject.gymphysique.feature.charts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myproject.gymphysique.core.designsystem.theme.Dimens
import com.myproject.gymphysique.core.model.Measurement
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
internal fun ChartComponent(
    modifier: Modifier = Modifier,
    measurements: List<Measurement>
) {
    val measurementValues = measurements
        .map {
            FloatEntry(
                x = it.date.takeLast(2).toFloat(),
                y = it.measurementResult.toFloat()
            )
        }
    val entryModel = entryModelOf(measurementValues)
    Column(
        modifier = modifier.padding(Dimens.margin),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Chart(
            chart = lineChart(
                lines = listOf(
                    lineSpec(
                        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0f)
                            )
                        )
                    )
                )
            ),
            model = entryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
            chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = true),
            isZoomEnabled = true,
            marker = rememberMarker(),
            chartScrollState = rememberChartScrollState()
        )
    }
}

@Preview
@Composable
fun ChartComponentPreview() {
    ChartComponent(
        measurements = emptyList()
    )
}
