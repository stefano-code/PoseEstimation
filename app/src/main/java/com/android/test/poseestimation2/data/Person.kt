package com.android.test.poseestimation2.data


data class Person(
    var id: Int = -1, // default id is -1
    val keyPoints: List<KeyPoint>,
    val score: Float
)