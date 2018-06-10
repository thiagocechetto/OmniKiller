package com.casadelfuego.omnikiller.datasource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.casadelfuego.omnikiller.model.Card

class CardDataSourceMockData: CardDataSource {

  override val cards: MutableLiveData<List<Card>> = MutableLiveData()

  override fun fetchCards() {
    val cardList = mutableListOf<Card>()
    for (i in 1..5000) {
      cardList.add(Card(id = "$i", cardNumber = "$i"))
    }
    cards.postValue(cardList)
  }
}