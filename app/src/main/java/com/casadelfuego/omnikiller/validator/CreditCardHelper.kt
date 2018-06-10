package com.casadelfuego.omnikiller.validator

import android.support.annotation.VisibleForTesting

class CreditCardHelper {

  fun scavengeForValidCreditCards(initialText: String): ArrayList<String> {
    val creditCardValidator = CreditCardValidator()
    val creditCardNumbers = arrayListOf<String>()
    val text = creditCardValidator.getDigitsOnly(initialText)

    if (isBigEnough(text)) {
      for (i in 0..text.length - CreditCardValidator.MAX_SIZE) {
        val substring = text.substring(i, i + CreditCardValidator.MAX_SIZE)
        if (creditCardValidator.isValid(substring)) {
          creditCardNumbers.add(substring)
        }
      }
    }

    return creditCardNumbers
  }

  @VisibleForTesting
  fun isBigEnough(text: String) = text.length >= CreditCardValidator.MAX_SIZE

}