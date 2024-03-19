package petrova.ola.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

class Utils {
    companion object {
        const val DARK_THEME = "DARK_THEME"
        const val PREFERENCES = "PREFERENCES"
    }
}
fun dpToPx(dp: Int, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun msToTime(ms: Int): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

