package com.casadelfuego.omnikiller.datasource

import android.arch.lifecycle.LiveData
import com.casadelfuego.omnikiller.model.Card

interface CardDataSource {

  val cards: LiveData<List<Card>>

  fun fetchCards()
}