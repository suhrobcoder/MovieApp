package uz.suhrob.movieapp.util

import android.content.res.Resources

class MetricUtils {
    companion object {
        fun dpToPx(dp: Int): Int {
            return ((dp * Resources.getSystem().displayMetrics.density).toInt())
        }
        fun pxToDp(px: Int): Int {
            return ((px / Resources.getSystem().displayMetrics.density).toInt())
        }
    }
}