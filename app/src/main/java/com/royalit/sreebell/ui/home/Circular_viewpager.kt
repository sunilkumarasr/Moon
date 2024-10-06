package com.royalit.sreebell.ui.home

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

//class Circular_viewpager (context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
//    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//
//        // Check if the last item is reached
//        if (position == adapter?.count?.minus(1) && positionOffset == 0.0f && positionOffsetPixels == 0) {
//            // Automatically scroll back to the first item
//            setCurrentItem(0, true)
//        }
//    }
//}
import android.os.Handler
import android.os.Looper

class Circular_viewpager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private val handler = Handler(Looper.getMainLooper())
    private var autoScrollRunnable: AutoScrollRunnable? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScroll()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)

        // Check if the last item is reached
        if (position == adapter?.count?.minus(1) && positionOffset == 0.0f && positionOffsetPixels == 0) {
            // Automatically scroll back to the first item with a smooth transition
            startAutoScroll()
        }
    }

    private fun startAutoScroll() {
        if (autoScrollRunnable == null) {
            autoScrollRunnable = AutoScrollRunnable()
            handler.postDelayed(autoScrollRunnable!!, 1000) // Delay the transition
        }
    }

    private fun stopAutoScroll() {
        if (autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable!!)
            autoScrollRunnable = null
        }
    }

    private inner class AutoScrollRunnable : Runnable {
        override fun run() {
            // Switch to the next item with a smooth scroll
            setCurrentItem(currentItem + 1, true)
            handler.postDelayed(this, 4000) // Delay for the next transition
        }
    }
}
