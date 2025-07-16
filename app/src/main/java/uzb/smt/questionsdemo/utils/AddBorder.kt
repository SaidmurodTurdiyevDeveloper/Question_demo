package uzb.smt.questionsdemo.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat


fun createBorderBackground(context: Context, borderColorResId: Int): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = 12f * context.resources.displayMetrics.density
        setStroke(2, ContextCompat.getColor(context, borderColorResId))
        setColor(ContextCompat.getColor(context, android.R.color.white))
    }
}