package com.casadelfuego


import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView

import com.google.android.gms.common.api.CommonStatusCodes

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
class MainActivity: Activity(), View.OnClickListener {

  // Use a compound button so either checkbox or switch widgets work.
  private var autoFocus: CompoundButton? = null
  private var useFlash: CompoundButton? = null
  private var statusMessage: TextView? = null
  private var textValue: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    statusMessage = findViewById(R.id.status_message) as TextView
    textValue = findViewById(R.id.text_value) as TextView

    autoFocus = findViewById(R.id.auto_focus) as CompoundButton
    useFlash = findViewById(R.id.use_flash) as CompoundButton

    findViewById(R.id.read_text).setOnClickListener(this)
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  override fun onClick(v: View) {
    if (v.id == R.id.read_text) {
      // launch Ocr capture activity.
      val intent = Intent(this, OcrCaptureActivity::class.java)
      intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus!!.isChecked)
      intent.putExtra(OcrCaptureActivity.UseFlash, useFlash!!.isChecked)

      startActivityForResult(intent, RC_OCR_CAPTURE)
    }
  }

  /**
   * Called when an activity you launched exits, giving you the requestCode
   * you started it with, the resultCode it returned, and any additional
   * data from it.  The <var>resultCode</var> will be
   * [.RESULT_CANCELED] if the activity explicitly returned that,
   * didn't return any result, or crashed during its operation.
   *
   *
   *
   * You will receive this call immediately before onResume() when your
   * activity is re-starting.
   *
   *
   *
   * @param requestCode The integer request code originally supplied to
   * startActivityForResult(), allowing you to identify who this
   * result came from.
   * @param resultCode  The integer result code returned by the child activity
   * through its setResult().
   * @param data        An Intent, which can return result data to the caller
   * (various data can be attached to Intent "extras").
   * @see .startActivityForResult
   *
   * @see .createPendingResult
   *
   * @see .setResult
   */
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == RC_OCR_CAPTURE) {
      if (resultCode == CommonStatusCodes.SUCCESS) {
        if (data != null) {
          val text = data.getStringExtra(OcrCaptureActivity.TextBlockObject)
          statusMessage!!.setText(R.string.ocr_success)
          textValue!!.text = text
          Log.d(TAG, "Text read: $text")
        } else {
          statusMessage!!.setText(R.string.ocr_failure)
          Log.d(TAG, "No Text captured, intent data is null")
        }
      } else {
        statusMessage!!.text = String.format(getString(R.string.ocr_error),
            CommonStatusCodes.getStatusCodeString(resultCode))
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data)
    }
  }

  companion object {

    private val RC_OCR_CAPTURE = 9003
    private val TAG = "MainActivity"
  }
}

