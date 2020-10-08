package ru.malcdevelop.bicycletrainer.ui

import androidx.fragment.app.Fragment
import ru.malcdevelop.bicycletrainer.MainApp

abstract class BaseFragment : Fragment() {

    protected val mainApp: MainApp?
        get() = activity?.application as? MainApp

    protected val mainActivity: MainActivity?
        get() = activity as? MainActivity
}