package ooo.cron.delivery.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/*
 * Created by Muhammad on 29.04.2021
 */



object Utils {


    fun phoneReplace(et_phone: EditText): String {
        return et_phone.text.toString()
            .replace("-", "")
            .replace(" ", "")
            .replace("+", "")
            .replace("(", "")
            .replace(")", "")
    }


    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}