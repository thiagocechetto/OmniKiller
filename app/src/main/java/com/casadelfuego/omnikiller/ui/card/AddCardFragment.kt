package com.casadelfuego.omnikiller.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.casadelfuego.omnikiller.R

class AddCardFragment: Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.add_card_fragment, container, false)
  }
}