package ru.malcdevelop.bicycletrainer.ui

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.malcdevelop.bicycletrainer.R
import ru.malcdevelop.bicycletrainer.ui.settings.DeviceSettingsFragment

private const val DIALOG_TAG = "DIALOG_TAG"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                super.onFragmentStarted(fm, f)
                if (f.id == R.id.contentFrameLayout) {
                    (f as? MainFragment)?.title?.let {
                        supportActionBar?.title = it
                    }
                }
            }
        }, false)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.contentFrameLayout, MenuFragment())
                .commit()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val rect = Rect()
            dialogFrameLayout.getGlobalVisibleRect(rect)

            if (!rect.contains(ev.x.toInt(), ev.y.toInt())) {
                if (supportFragmentManager.findFragmentByTag(DIALOG_TAG) != null) {
                    supportFragmentManager.popBackStack()
                    return false
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    fun navigateToDeviceSettings(menuFragment: MenuFragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.contentFrameLayout, DeviceSettingsFragment())
            .commit()
    }

    fun showDialogFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            if (supportFragmentManager.findFragmentByTag(DIALOG_TAG) != null)
                supportFragmentManager.popBackStack()

            addToBackStack(null)
            replace(R.id.dialogFrameLayout, fragment, DIALOG_TAG)
            commit()
        }
    }

    fun hideDialog() {
        if (supportFragmentManager.findFragmentByTag(DIALOG_TAG) != null)
            supportFragmentManager.popBackStack()
    }
}