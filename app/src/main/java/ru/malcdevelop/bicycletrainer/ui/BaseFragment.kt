package ru.malcdevelop.bicycletrainer.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected val mainActivity: MainActivity?
        get() = activity as? MainActivity
}