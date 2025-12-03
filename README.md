# Real-Time Pose Estimation with MoveNet on Android

This is an Android sample project that demonstrates real-time, single-person pose estimation using the **MoveNet.Thunder** model from TensorFlow Lite and the **CameraX API**.  
The application captures the live video feed from the camera, detects a person's pose, and overlays a skeleton of 17 keypoints in real-time.

---

## 📸 MoveNet Demo
<img width="360" height="780" alt="Screenshot_20251203_204829" src="https://github.com/user-attachments/assets/8538529e-2129-4b3c-89f1-205593d36cfd" />


---

## 🚀 Key Features
- **Real-Time Pose Estimation**: Utilizes the MoveNet model for fast and accurate detection of body keypoints.  
- **High-Performance Rendering**: Leverages a `SurfaceView` and `Canvas` for efficient, low-level drawing of the skeleton.  
- **GPU Acceleration**: Uses RenderScript for an ultra-fast conversion of camera frames from YUV to RGB, maximizing the frame rate (FPS).  
- **Clean Architecture**: The code is decoupled into logical components (UI, controller, ML) for enhanced readability and maintainability.  
- **Data Visualization**: Displays the inference frame rate (FPS) and the model's confidence score in real-time.  

---

## 🏗️ Software Architecture
The project follows a decoupled architecture to separate concerns and improve code testability.

### `MainActivity.kt` (UI & Configuration)
- Manages the User Interface (UI), including the `SurfaceView` and `TextViews` for displaying data.  
- Handles the application's lifecycle and permission management.  
- Creates and configures the `CameraController`, delegating all complex logic to it.  

### `CameraController.kt` (Main Engine)
- The core of the application. It encapsulates all processing logic.  
- Manages the CameraX API to receive video frames.  
- Orchestrates the analysis pipeline for each frame: color conversion, inference, and drawing.  
- Draws directly onto the `SurfaceView`'s `Canvas` to maximize performance.  

### `MoveNet.kt` (Machine Learning Logic)
- A wrapper for the `movenet_thunder.tflite` TensorFlow Lite model.  
- Handles image pre-processing (resizing and padding) and post-processing of the model's output to extract keypoints.  

### `YuvToRgbConverter.kt` (Conversion Utility)
- A specialized class that uses RenderScript to efficiently convert frames from the camera's native `YUV_420_888` format to RGB, leveraging GPU acceleration.  

### `VisualizationUtils.kt` (Drawing Utility)
- Provides helper methods to draw the skeleton (points and lines) onto a given `Canvas`.  

---

## 🔄 Workflow
1. `MainActivity` creates and configures `CameraController`, passing it the `SurfaceView`.  
2. `CameraController` starts CameraX and begins receiving video frames.  
3. For each frame:  
   a. `YuvToRgbConverter` converts the frame into an RGB `Bitmap`.  
   b. `MoveNet` analyzes the `Bitmap` and returns the keypoints of a detected person.  
   c. `CameraController` locks the `SurfaceView`'s `Canvas`.  
   d. `VisualizationUtils` draws the skeleton onto the `Canvas`.  
   e. `CameraController` unlocks the `Canvas`, displaying the result to the user.  
4. `CameraController` sends the FPS and confidence score back to `MainActivity` to update the UI.  

---

## 🛠️ Technologies Used
- **Kotlin**: Primary programming language.  
- **CameraX**: For easy and robust camera access.  
- **TensorFlow Lite**: For on-device machine learning inference.  
- **RenderScript**: For GPU-accelerated YUV to RGB conversion.  
- **SurfaceView**: For high-performance rendering on a dedicated drawing surface.  
