package com.casadelfuego.ocr.ui.camera.validation

import java.util.Scanner

object CreditCardValidation {

  fun isValid(number: Long): Boolean {

    val total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)

    return total % 10 == 0 && prefixMatched(number, 1) && getSize(number) >= 13 && getSize(number) <= 16
  }

  fun getDigit(number: Int): Int {

    if (number <= 9) {
      return number
    } else {
      val firstDigit = number % 10
      val secondDigit = number / 10

      return firstDigit + secondDigit
    }
  }

  fun sumOfOddPlace(number: Long): Int {
    var number = number
    var result = 0

    while (number > 0) {
      result += (number % 10).toInt()
      number = number / 100
    }

    return result
  }

  fun sumOfDoubleEvenPlace(number: Long): Int {
    var number = number

    var result = 0
    var temp: Long = 0

    while (number > 0) {
      temp = number % 100
      result += getDigit((temp / 10).toInt() * 2)
      number = number / 100
    }

    return result
  }

  fun prefixMatched(number: Long, d: Int): Boolean {

    if (getPrefix(number, d) == 4L
        || getPrefix(number, d) == 5L
        || getPrefix(number, d) == 3L) {

      if (getPrefix(number, d) == 3L) {
        println("\nVisa Card ")
      } else if (getPrefix(number, d) == 5L) {
        println("\nMaster Card ")
      } else if (getPrefix(number, d) == 3L) {
        println("\nAmerican Express Card ")
      }

      return true

    } else {

      return false

    }
  }

  fun getSize(d: Long): Int {
    var d = d

    var count = 0

    while (d > 0) {
      d = d / 10

      count++
    }

    return count

  }

  fun getPrefix(number: Long, k: Int): Long {
    var number = number

    if (getSize(number) < k) {
      return number
    } else {

      val size = getSize(number)

      for (i in 0 until size - k) {
        number = number / 10
      }

      return number

    }

  }

  @JvmStatic
  fun main(args: Array<String>) {

    val sc = Scanner(System.`in`)

    print("Enter a credit card number as a long integer: ")

    val input = sc.nextLong()


    if (isValid(input) == true) {
      println("\n$input is Valid. ")
    } else {
      println("\n$input is Invalid. ")
    }

  }
}