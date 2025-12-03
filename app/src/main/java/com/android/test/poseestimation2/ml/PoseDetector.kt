package com.android.test.poseestimation2.ml


import android.graphics.Bitmap
import com.android.test.poseestimation2.data.Person

interface PoseDetector : AutoCloseable {

    fun estimatePoses(bitmap: Bitmap): List<Person>

    fun lastInferenceTimeNanos(): Long
}