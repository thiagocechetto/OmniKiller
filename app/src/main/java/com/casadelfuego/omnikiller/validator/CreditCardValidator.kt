package com.casadelfuego.omnikiller.validator

class CreditCardValidator {

  companion object {
    const val MIN_SIZE = 13
    const val MAX_SIZE = 16
  }

  enum class Type {
    VISA, MASTER_CARD, AMERICAN_EXPRESS, UNKNOWN
  }

  fun isValid(text: String): Boolean {
    val textDigitsOnly = getDigitsOnly(text)
    return if (!textDigitsOnly.isEmpty()) isValid(textDigitsOnly.toLong()) else false
  }

  fun isValid(number: Long): Boolean {
    val total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)
    return total % 10 == 0 && prefixMatched(number) && hasValidSize(number)
  }

  fun getDigitsOnly(text: String) = text.replace("\\D".toRegex(), "")

  private fun prefixMatched(number: Long): Boolean = getPrefix(number, 1) in 3L..5L

  private fun hasValidSize(number: Long) = getSize(number) in MIN_SIZE..MAX_SIZE

  private fun getDigit(number: Int) =
      if (number <= 9) {
        number
      } else {
        val firstDigit = number % 10
        val secondDigit = number / 10

        firstDigit + secondDigit
      }

  private fun sumOfOddPlace(initialNumber: Long): Int {
    var number = initialNumber
    var result = 0

    while (number > 0) {
      result += (number % 10).toInt()
      number /= 100
    }

    return result
  }

  private fun sumOfDoubleEvenPlace(initialNumber: Long): Int {
    var number = initialNumber

    var result = 0
    var temp: Long

    while (number > 0) {
      temp = number % 100
      result += getDigit((temp / 10).toInt() * 2)
      number /= 100
    }

    return result
  }

  fun getCardType(number: Long) =
      when (getPrefix(number, 1)) {
        3L -> Type.AMERICAN_EXPRESS
        4L -> Type.VISA
        5L -> Type.MASTER_CARD
        else -> Type.UNKNOWN
      }

  private fun getSize(initialDigits: Long): Int {
    var digits = initialDigits
    var count = 0

    while (digits > 0) {
      digits /= 10
      count++
    }

    return count
  }

  private fun getPrefix(initialNumber: Long, k: Int): Long {
    var number = initialNumber

    return if (getSize(number) < k) {
      number
    } else {
      val size = getSize(number)

      for (i in 0 until size - k) {
        number /= 10
      }

      number
    }
  }

}