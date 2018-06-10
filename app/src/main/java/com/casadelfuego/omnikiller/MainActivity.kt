package com.casadelfuego.omnikiller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController
import com.casadelfuego.omnikiller.ui.camera.OcrCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes

class MainActivity: AppCompatActivity() {

  companion object {
    private const val RC_OCR_CAPTURE = 1000
    private const val TAG = "MainActivity"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)

    openOcrReader()
  }

  private fun openOcrReader(autoFocus: Boolean = true, useFlash: Boolean = false) {
    val intent = Intent(this, OcrCaptureActivity::class.java)
    intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus)
    intent.putExtra(OcrCaptureActivity.UseFlash, useFlash)

    startActivityForResult(intent, RC_OCR_CAPTURE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == RC_OCR_CAPTURE) {
      if (resultCode == CommonStatusCodes.SUCCESS) {
        if (data != null) {
          val card = data.getStringExtra(OcrCaptureActivity.TextBlockObject)
          Log.d(TAG, "Card: $card")
        } else {
          Log.d(TAG, "No Text captured, intent data is null")
        }
      } else {
        Log.d(TAG, String.format(getString(R.string.ocr_error), CommonStatusCodes.getStatusCodeString(resultCode)))
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onSupportNavigateUp() = findNavController(R.id.navigation_host_fragment).navigateUp()
}
