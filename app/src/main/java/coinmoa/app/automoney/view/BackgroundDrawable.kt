package coinmoa.app.automoney.view

import coinmoa.app.automoney.R
import coinmoa.app.automoney.utils.ThemeUtils
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.v4.content.ContextCompat

/**
 * Create a new transition drawable with the specified list of layers. At least
 * 2 layers are required for this drawable to work properly.
 */
class BackgroundDrawable(
        context: Context
) : TransitionDrawable(
        arrayOf<Drawable>(
                ColorDrawable(ContextCompat.getColor(context, R.color.transparent)),
                ColorDrawable(ThemeUtils.getColor(context, R.attr.selectedBackground))
        )
) {

    private var isSelected: Boolean = false

    override fun startTransition(durationMillis: Int) {
        if (!isSelected) {
            super.startTransition(durationMillis)
        }
        isSelected = true
    }

    override fun reverseTransition(duration: Int) {
        if (isSelected) {
            super.reverseTransition(duration)
        }
        isSelected = false
    }

}
