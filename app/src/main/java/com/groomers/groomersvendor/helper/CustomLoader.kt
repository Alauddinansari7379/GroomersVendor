package com.groomers.groomersvendor.helper
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.groomers.groomersvendor.R

object CustomLoader {

    private var dialog: Dialog? = null

    fun showLoaderDialog(context: Context?) {
        try {
            if (dialog != null) if (dialog!!.isShowing) dialog!!.dismiss()
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setContentView(R.layout.customize_loader)

            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoaderDialog() {
        try {
            if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
