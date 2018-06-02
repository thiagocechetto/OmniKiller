package com.casadelfuego.omnikiller.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.casadelfuego.omnikiller.R
import kotlinx.android.synthetic.main.main_fragment.addCardButton

class MainFragment: Fragment() {

  private lateinit var viewModel: MainViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    // TODO: Use the ViewModel

    addCardButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_addCardFragment, null))
  }

}
