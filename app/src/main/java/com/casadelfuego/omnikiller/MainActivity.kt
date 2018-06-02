package com.casadelfuego.omnikiller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.casadelfuego.omnikiller.ui.main.MainFragment
import android.content.Intent
import com.casadelfuego.omnikiller.ocr.OcrActivity

class MainActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, MainFragment.newInstance())
          .commitNow()
    }

    startActivity(Intent(this, OcrActivity::class.java))
  }

}
