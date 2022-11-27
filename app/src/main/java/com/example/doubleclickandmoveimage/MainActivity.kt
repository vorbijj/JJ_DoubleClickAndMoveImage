package com.example.doubleclickandmoveimage

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    val TAG = "@@@"
    val KEY_VIEW_SAVE_IS_WAS_CLICK = "Key_View_Save_Is_Was_Ckick"

    private var currentEntityImg: EntityImg = EntityImg()
    private var isWasEnableClick = false
    private var isLandscape = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val targetView = findViewById<ImageView>(R.id.img_bw)
        val moveView = findViewById<ImageView>(R.id.img_color)
        val refreshView = findViewById<ImageView>(R.id.img_refresh)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (savedInstanceState != null) {
            isWasEnableClick = savedInstanceState.getBoolean(KEY_VIEW_SAVE_IS_WAS_CLICK)
        }

        Log.d(TAG, "Запущен onCreate")

        val vtoTarget = targetView.viewTreeObserver
        vtoTarget.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetView.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)

                currentEntityImg.bwX = targetView.x
                currentEntityImg.bwY = targetView.y
                Log.d(TAG, "Присвоен currentEntityImg.bwX и currentEntityImg.bwY")
                Log.d(
                    TAG,
                    "Присвоен currentEntityImg.bwX=${currentEntityImg.bwX}, currentEntityImg.bwY=${currentEntityImg.bwY}, targetView.z=${targetView.z}"
                )
            }
        })

        val vtoViewToMove = moveView.viewTreeObserver
        vtoViewToMove.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                moveView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                currentEntityImg.colorX = moveView.x
                currentEntityImg.colorY = moveView.y
                Log.d(TAG, "Присвоен currentEntityImg.colorX и currentEntityImg.colorY")
                Log.d(
                    TAG,
                    "Присвоен currentEntityImg.colorX=${currentEntityImg.colorX}, currentEntityImg.colorY=${currentEntityImg.colorY}, moveView.z=${moveView.z}"
                )

                if (isWasEnableClick) {
                    moveView.animate()
                        .x(targetView.x)
                        .y(targetView.y)
                        .setDuration(0)
                        .start()

                    refreshView.visibility = View.VISIBLE
                }
            }
        })


        var dX = 0.0f
        var dY = 0.0f

        moveView.setOnTouchListener { v, event ->
            if (isWasEnableClick) {
                Log.d(TAG, "NOT TOUCH")
            } else {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        Log.d(TAG, "ACTION_DOWN")
                    }

                    MotionEvent.ACTION_MOVE -> {
                        Log.d(TAG, "ACTION_MOVE")
                        v.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d(TAG, "ACTION_UP")
                        val vRec = Rect()
                        v.getHitRect(vRec)
                        val centerX = vRec.centerX()
                        val centerY = vRec.centerY()

                        val tRec = Rect()
                        targetView.getHitRect(tRec)

                        val isIntersection = centerX < targetView.x ||
                                centerX > targetView.x + targetView.width ||
                                centerY < targetView.y ||
                                centerY > targetView.y + targetView.height
                        if (isIntersection) {
                            v.animate()
                                .x(currentEntityImg.colorX)
                                .y(currentEntityImg.colorY)
                                .setDuration(500)
                                .start()
                        } else {
                            translateView(
                                moveView,
                                refreshView,
                                currentEntityImg.bwX,
                                currentEntityImg.bwY
                            )
                        }
                    }
                }
            }
            false
        }

        moveView.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                Log.d(TAG, "onDoubleClick")
                if (isWasEnableClick) {
                    translateView(
                        moveView, refreshView, currentEntityImg.colorX, currentEntityImg.colorY
                    )
                } else {
                    translateView(moveView, refreshView, currentEntityImg.bwX, currentEntityImg.bwY)
                }
            }
        })

        refreshView.setOnClickListener {
            translateView(moveView, refreshView, currentEntityImg.colorX, currentEntityImg.colorY)
        }
    }

    private fun translateView(
        moveView: ImageView,
        refreshView: ImageView,
        coordinateX: Float,
        coordinateY: Float
    ) {
        moveView.animate()
            .x(coordinateX)
            .y(coordinateY)
            .setDuration(500)
            .start()

        if (isWasEnableClick) {
            isWasEnableClick = false
            refreshView.visibility = View.INVISIBLE
        } else {
            isWasEnableClick = true
            refreshView.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VIEW_SAVE_IS_WAS_CLICK, isWasEnableClick)
        Log.d(TAG, "Запущен onSaveInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}