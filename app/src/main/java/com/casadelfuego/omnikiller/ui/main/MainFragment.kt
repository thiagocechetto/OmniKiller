package com.casadelfuego.omnikiller.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.casadelfuego.omnikiller.R
import com.casadelfuego.omnikiller.datasource.CardDataSource
import com.casadelfuego.omnikiller.datasource.CardDataSourceMockData
import com.casadelfuego.omnikiller.model.Card
import kotlinx.android.synthetic.main.main_fragment.addCardButton
import kotlinx.android.synthetic.main.main_fragment.message

class MainFragment: Fragment() {

  private lateinit var viewModel: MainViewModel

  private lateinit var cardDataSource: CardDataSource

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    cardDataSource = CardDataSourceMockData() // TODO How are we injecting this? using Dagger?
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    // TODO: Use the ViewModel
    addCardButton.setOnClickListener(Navigation.createNavigateOnClickListener(
        R.id.action_mainFragment_to_addCardFragment, null))

    cardDataSource.cards.observe(this, Observer { cards ->
      populateCardList(cards)
    })
    cardDataSource.fetchCards()
  }

  private fun populateCardList(cards: List<Card>?) {
    // TODO replace it when created card list (using Paging, of course!)
    message.setText(cards.toString())
  }
}
