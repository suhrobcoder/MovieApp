package uz.suhrob.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, layout: Int): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(layout, container, false)
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }
        return rootView
    }
}