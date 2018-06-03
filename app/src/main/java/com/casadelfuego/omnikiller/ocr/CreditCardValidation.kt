package com.casadelfuego.omnikiller.ocr

import java.util.Scanner

object CreditCardValidation {

  fun isValid(number: Long): Boolean {

    val total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)

    return total % 10 == 0 && prefixMatched(number, 1) && getSize(number) >= 13 && getSize(number) <= 16
  }

  private fun getDigit(number: Int): Int {

    return if (number <= 9) {
      number
    } else {
      val firstDigit = number % 10
      val secondDigit = number / 10

      firstDigit + secondDigit
    }
  }

  private fun sumOfOddPlace(number: Long): Int {
    var number = number
    var result = 0

    while (number > 0) {
      result += (number % 10).toInt()
      number /= 100
    }

    return result
  }

  private fun sumOfDoubleEvenPlace(number: Long): Int {
    var number = number

    var result = 0
    var temp: Long

    while (number > 0) {
      temp = number % 100
      result += getDigit((temp / 10).toInt() * 2)
      number /= 100
    }

    return result
  }

  private fun prefixMatched(number: Long, d: Int): Boolean {

    if (getPrefix(number, d) == 4L
        || getPrefix(number, d) == 5L
        || getPrefix(number, d) == 3L) {

      when {
        getPrefix(number, d) == 3L -> println("\nVisa Card ")
        getPrefix(number, d) == 5L -> println("\nMaster Card ")
        getPrefix(number, d) == 3L -> println("\nAmerican Express Card ")
      }

      return true

    } else {

      return false

    }
  }

  private fun getSize(d: Long): Int {
    var d = d

    var count = 0

    while (d > 0) {
      d /= 10

      count++
    }

    return count

  }

  private fun getPrefix(number: Long, k: Int): Long {
    var number = number

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

  @JvmStatic
  fun main(args: Array<String>) {

    val sc = Scanner(System.`in`)

    print("Enter a credit card number as a long integer: ")

    val input = sc.nextLong()


    if (isValid(input)) {
      println("\n$input is Valid. ")
    } else {
      println("\n$input is Invalid. ")
    }

  }
}