package com.android.test.poseestimation2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.test.poseestimation2.ml.ModelType
import com.android.test.poseestimation2.ml.MoveNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // https://github.com/tensorflow/examples/blob/master/lite/examples/pose_estimation/android/app/build.gradle

    companion object {
        private const val FRAGMENT_DIALOG = "dialog"
    }

    /** A [SurfaceView] for camera preview.   */
    private lateinit var surfaceView: SurfaceView

    private lateinit var tvScore: TextView
    private lateinit var tvFPS: TextView
    private var cameraController: CameraController? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "This app needs camera permission.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        tvScore = findViewById(R.id.tvScore)
        tvFPS = findViewById(R.id.tvFps)
        surfaceView = findViewById(R.id.surfaceView)
        checkCameraPermission()
    }

    override fun onStart() {
        super.onStart()
        startCamera()
    }

    override fun onResume() {
        cameraController?.resume()
        super.onResume()
    }

    override fun onPause() {
        cameraController?.close()
        cameraController = null
        super.onPause()
    }

    // start camera
    private fun startCamera() {
            if (cameraController == null) {
                cameraController = CameraController(surfaceView, object : CameraController.CameraSourceListener {
                        override fun onFPSListener(fps: Int) {
                            tvFPS.text = getString(R.string.tfe_pe_tv_fps, fps)
                        }

                        override fun onDetectedInfo(
                            personScore: Float?,
                            poseLabels: List<Pair<String, Float>>?
                        ) {
                            tvScore.text = getString(R.string.tfe_pe_tv_score, personScore ?: 0f)
                        }

                    }).apply {
                        prepareCamera()
                    }
                lifecycleScope.launch(Dispatchers.Main) {
                    cameraController?.initCamera()
                }
            }
            createPoseEstimator()
    }

    private fun createPoseEstimator() {
        showDetectionScore(true)
        val poseDetector = MoveNet.create(this, ModelType.Lightning)
        // val poseDetector = MoveNet.create(this, ModelType.Thunder)

        poseDetector?.let { detector ->
            cameraController?.setDetector(detector)
        }
    }

    // Show/hide the detection score.
    private fun showDetectionScore(isVisible: Boolean) {
        tvScore.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}