package com.myproject.gymphysique.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddChart
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.myproject.gymphysique.core.designsystem.R

object GPIcons {
    @JvmStatic
    val Theme = Icons.Rounded
    val Charts = Theme.BarChart
    val Measure = Theme.AddChart
    val Settings = R.drawable.ic_settings
    val Search = Theme.Search
    val Circle = Theme.Circle
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
