package uz.suhrob.movieapp.extension

import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import uz.suhrob.movieapp.R

fun Fragment.setupStatusBar(transparent: Boolean) {
    (activity as? AppCompatActivity)?.window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (transparent) {
                this?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                this?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                this?.statusBarColor = Color.TRANSPARENT
            } else {
                this?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                this?.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
            }
        }
    }
}

fun Fragment.setupToolbar(toolbar: Toolbar) {
    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
}