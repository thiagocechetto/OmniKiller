package com.casadelfuego.omnikiller.ocr

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import com.casadelfuego.omnikiller.R
import com.casadelfuego.omnikiller.camera.CameraSourcePreview
import com.casadelfuego.omnikiller.ui.camera.GraphicOverlay
import com.casadelfuego.omnikiller.ui.camera.CameraSource
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException

/**
 * Activity for the multi-tracker app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
class OcrCaptureActivity: AppCompatActivity() {

  private var mCameraSource: CameraSource? = null
  private var mPreview: CameraSourcePreview? = null
  private var mGraphicOverlay: GraphicOverlay<OcrGraphic>? = null

  // Helper objects for detecting taps and pinches.
  private var scaleGestureDetector: ScaleGestureDetector? = null
  private var gestureDetector: GestureDetector? = null

  /**
   * Initializes the UI and creates the detector pipeline.
   */
  public override fun onCreate(icicle: Bundle?) {
    super.onCreate(icicle)
    setContentView(R.layout.ocr_capture)

    mPreview = findViewById(R.id.preview)
    mGraphicOverlay = findViewById(R.id.graphicOverlay)

    // read parameters from the intent used to launch the activity.
    val autoFocus = intent.getBooleanExtra(AutoFocus, false)
    val useFlash = intent.getBooleanExtra(UseFlash, false)

    // Check for the camera permission before accessing the camera.  If the
    // permission is not granted yet, request permission.
    val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    if (rc == PackageManager.PERMISSION_GRANTED) {
      createCameraSource(autoFocus, useFlash)
    } else {
      requestCameraPermission()
    }

    gestureDetector = GestureDetector(this, CaptureGestureListener())
    scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

    Snackbar.make(mGraphicOverlay!!, "Tap to capture. Pinch/Stretch to zoom",
        Snackbar.LENGTH_LONG)
        .show()
  }

  /**
   * Handles the requesting of the camera permission.  This includes
   * showing a "Snackbar" message of why the permission is needed then
   * sending the request.
   */
  private fun requestCameraPermission() {
    Log.w(TAG, "Camera permission is not granted. Requesting permission")

    val permissions = arrayOf(Manifest.permission.CAMERA)

    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.CAMERA)) {
      ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
      return
    }

    val thisActivity = this

    val listener = View.OnClickListener {
      ActivityCompat.requestPermissions(thisActivity, permissions,
          RC_HANDLE_CAMERA_PERM)
    }

    Snackbar.make((mGraphicOverlay as View), R.string.permission_camera_rationale,
        Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.ok, listener)
        .show()
  }

  override fun onTouchEvent(e: MotionEvent): Boolean {
    val b = scaleGestureDetector!!.onTouchEvent(e)

    val c = gestureDetector!!.onTouchEvent(e)

    return b || c || super.onTouchEvent(e)
  }

  /**
   * Creates and starts the camera.  Note that this uses a higher resolution in comparison
   * to other detection examples to enable the ocr detector to detect small text samples
   * at long distances.
   *
   * Suppressing InlinedApi since there is a check that the minimum version is met before using
   * the constant.
   */
  @SuppressLint("InlinedApi")
  private fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
    val context = applicationContext

    // A text recognizer is created to find text.  An associated processor instance
    // is set to receive the text recognition results and display graphics for each text block
    // on screen.
    val textRecognizer = TextRecognizer.Builder(context).build()
    textRecognizer.setProcessor(OcrDetectorProcessor(mGraphicOverlay!!))

    if (!textRecognizer.isOperational) {
      // Note: The first time that an app using a Vision API is installed on a
      // device, GMS will download a native libraries to the device in order to do detection.
      // Usually this completes before the app is run for the first time.  But if that
      // download has not yet completed, then the above call will not detect any text,
      // barcodes, or faces.
      //
      // isOperational() can be used to check if the required native libraries are currently
      // available.  The detectors will automatically become operational once the library
      // downloads complete on device.
      Log.w(TAG, "Detector dependencies are not yet available.")

      // Check for low storage.  If there is low storage, the native library will not be
      // downloaded, so detection will not become operational.
      val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
      val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

      if (hasLowStorage) {
        Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
        Log.w(TAG, getString(R.string.low_storage_error))
      }
    }

    // Creates and starts the camera.  Note that this uses a higher resolution in comparison
    // to other detection examples to enable the text recognizer to detect small pieces of text.
    @Suppress("DEPRECATION")
    mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
        .setFacing(CameraSource.CAMERA_FACING_BACK)
        .setRequestedPreviewSize(1280, 1024)
        .setRequestedFps(2.0f)
        .setFlashMode(if (useFlash) Camera.Parameters.FLASH_MODE_TORCH else null)
        .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE else null)
        .build()
  }

  /**
   * Restarts the camera.
   */
  override fun onResume() {
    super.onResume()
    startCameraSource()
  }

  /**
   * Stops the camera.
   */
  override fun onPause() {
    super.onPause()
    if (mPreview != null) {
      mPreview!!.stop()
    }
  }

  /**
   * Releases the resources associated with the camera source, the associated detectors, and the
   * rest of the processing pipeline.
   */
  override fun onDestroy() {
    super.onDestroy()
    if (mPreview != null) {
      mPreview!!.release()
    }
  }

  /**
   * Callback for the result from requesting permissions. This method
   * is invoked for every call on [.requestPermissions].
   *
   *
   * **Note:** It is possible that the permissions request interaction
   * with the user is interrupted. In this case you will receive empty permissions
   * and results arrays which should be treated as a cancellation.
   *
   *
   * @param requestCode  The request code passed in [.requestPermissions].
   * @param permissions  The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions
   * which is either [PackageManager.PERMISSION_GRANTED]
   * or [PackageManager.PERMISSION_DENIED]. Never null.
   * @see .requestPermissions
   */
  override fun onRequestPermissionsResult(requestCode: Int,
      permissions: Array<String>,
      grantResults: IntArray) {
    if (requestCode != RC_HANDLE_CAMERA_PERM) {
      Log.d(TAG, "Got unexpected permission result: $requestCode")
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      return
    }

    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Log.d(TAG, "Camera permission granted - initialize the camera source")
      // We have permission, so create the camerasource
      val autoFocus = intent.getBooleanExtra(AutoFocus, false)
      val useFlash = intent.getBooleanExtra(UseFlash, false)
      createCameraSource(autoFocus, useFlash)
      return
    }

    Log.e(TAG, "Permission not granted: results len = " + grantResults.size +
        " Result code = " + if (grantResults.isNotEmpty()) grantResults[0] else "(empty)")

    val listener = DialogInterface.OnClickListener { _, _ -> finish() }

    val builder = AlertDialog.Builder(this)
    builder.setTitle("Multitracker sample")
        .setMessage(R.string.no_camera_permission)
        .setPositiveButton(R.string.ok, listener)
        .show()
  }

  /**
   * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  @Throws(SecurityException::class)
  private fun startCameraSource() {
    // Check that the device has play services available.
    val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
        applicationContext)
    if (code != ConnectionResult.SUCCESS) {
      val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
      dlg.show()
    }

    if (mCameraSource != null) {
      try {
        mPreview!!.start(mCameraSource!!, mGraphicOverlay!!)
      } catch (e: IOException) {
        Log.e(TAG, "Unable to start camera source.", e)
        mCameraSource!!.release()
        mCameraSource = null
      }

    }
  }

  /**
   * onTap is called to capture the first TextBlock under the tap location and return it to
   * the Initializing Activity.
   *
   * @param rawX - the raw position of the tap
   * @param rawY - the raw position of the tap.
   * @return true if the activity is ending.
   */
  private fun onTap(rawX: Float, rawY: Float): Boolean {
    val graphic = mGraphicOverlay!!.getGraphicAtLocation(rawX, rawY)
    var text: TextBlock? = null
    if (graphic != null) {
      text = graphic.textBlock
      if (text != null && text.value != null) {
        val data = Intent()
        data.putExtra(TextBlockObject, text.value)
        setResult(CommonStatusCodes.SUCCESS, data)
        finish()
      } else {
        Log.d(TAG, "text data is null")
      }
    } else {
      Log.d(TAG, "no text detected")
    }
    return text != null
  }

  private inner class CaptureGestureListener: GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
      return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
    }
  }

  private inner class ScaleListener: ScaleGestureDetector.OnScaleGestureListener {

    /**
     * Responds to scaling events for a gesture in progress.
     * Reported by pointer motion.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     * @return Whether or not the detector should consider this event
     * as handled. If an event was not handled, the detector
     * will continue to accumulate movement until an event is
     * handled. This can be useful if an application, for example,
     * only wants to update scaling factors if the change is
     * greater than 0.01.
     */
    override fun onScale(detector: ScaleGestureDetector): Boolean {
      return false
    }

    /**
     * Responds to the beginning of a scaling gesture. Reported by
     * new pointers going down.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     * @return Whether or not the detector should continue recognizing
     * this gesture. For example, if a gesture is beginning
     * with a focal point outside of a region where it makes
     * sense, onScaleBegin() may return false to ignore the
     * rest of the gesture.
     */
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
      return true
    }

    /**
     * Responds to the end of a scale gesture. Reported by existing
     * pointers going up.
     *
     *
     * Once a scale has ended, [ScaleGestureDetector.getFocusX]
     * and [ScaleGestureDetector.getFocusY] will return focal point
     * of the pointers remaining on the screen.
     *
     * @param detector The detector reporting the event - use this to
     * retrieve extended info about event state.
     */
    override fun onScaleEnd(detector: ScaleGestureDetector) {
      mCameraSource!!.doZoom(detector.scaleFactor)
    }
  }

  companion object {
    private const val TAG = "OcrCaptureActivity"

    // Intent request code to handle updating play services if needed.
    private const val RC_HANDLE_GMS = 9001

    // Permission request codes need to be < 256
    private const val RC_HANDLE_CAMERA_PERM = 2

    // Constants used to pass extra data in the intent
    const val AutoFocus = "AutoFocus"
    const val UseFlash = "UseFlash"
    const val TextBlockObject = "String"
  }
}
