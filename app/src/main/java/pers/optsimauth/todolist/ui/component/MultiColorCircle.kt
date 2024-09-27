import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun MultiColorCircle(
    modifier: Modifier = Modifier,
    segments: Int,
    colors: List<Color>,
    gapAngle: Float = 6f,
) {
    Canvas(modifier = modifier) {
        val adjustedGapAngle = if (segments == 1) 0f else gapAngle
        val sweepAngle = (360f - segments * adjustedGapAngle) / segments
        val radius = size.minDimension / 2
        val strokeWidth = (size.minDimension * 0.1f).coerceAtMost(20.dp.toPx())


        for (i in 0 until segments) {
            drawArc(
                color = colors[i % colors.size],
                startAngle = i * (sweepAngle + adjustedGapAngle),
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = center.copy(x = center.x - radius, y = center.y - radius),
                size = size.copy(width = radius * 2, height = radius * 2),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}