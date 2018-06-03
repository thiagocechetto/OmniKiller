package com.casadelfuego.omnikiller.ocr

import com.casadelfuego.omnikiller.ui.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
class OcrDetectorProcessor internal constructor(private val mGraphicOverlay: GraphicOverlay<OcrGraphic>, val valueChangedListener: ValueChangedListener): Detector.Processor<TextBlock> {

  interface ValueChangedListener {
    fun onValueChanged(newValue: String )
  }

  /**
   * Called by the detector to deliver detection results.
   * If your application called for it, this could be a place to check for
   * equivalent detections by tracking TextBlocks that are similar in location and content from
   * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
   * multiple detections.
   */
  override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
    mGraphicOverlay.clear()
    val items = detections.detectedItems
    for (i in 0 until items.size()) {
      val item = items.valueAt(i)
      var text = item.value
      text = text.replace("\\s+".toRegex(), "")
      try {
        val number = java.lang.Long.parseLong(text)
        if (CreditCardValidation.isValid(number)) {
          valueChangedListener.onValueChanged(text)
        }
      } catch (e: NumberFormatException) {
        // do nothing
      }

      val graphic = OcrGraphic(mGraphicOverlay, item)
      mGraphicOverlay.add(graphic)
    }
  }

  /**
   * Frees the resources associated with this detection processor.
   */
  override fun release() {
    mGraphicOverlay.clear()
  }
}
