package com.casadelfuego.omnikiller.ocr

import com.casadelfuego.omnikiller.validator.CreditCardValidator
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CreditCardValidatorTest {

  private val creditCardValidator = CreditCardValidator()

  @Test
  fun `Correct digits only`() =
      Assert.assertEquals(creditCardValidator.getDigitsOnly("A \\ + = % #@! &^1B2C-? 3"), "123")

  @Test
  fun `Already just digits`() =
      Assert.assertEquals(creditCardValidator.getDigitsOnly("123"), "123")

  @Test
  fun `Empty digits only`() =
      Assert.assertEquals(creditCardValidator.getDigitsOnly(""), "")


  @Test
  fun `Valid credit card`() =
      assertTrue(creditCardValidator.isValid(4511293452724763))

  @Test
  fun `Invalid credit card`() =
      assertFalse(creditCardValidator.isValid(1111111111111111))

  @Test
  fun `Valid credit card String`() =
      assertTrue(creditCardValidator.isValid("4511293452724763"))

  @Test
  fun `Invalid credit card String`() =
      assertFalse(creditCardValidator.isValid("1111111111111111"))

  @Test
  fun `Invalid credit card Empty String`() =
      assertFalse(creditCardValidator.isValid(""))

  @Test
  fun `Valid credit card Dirty String`() =
      assertTrue(creditCardValidator.isValid("451$1%2ˆ9&3*4(5)2a7f2g4h763"))

  @Test
  fun `Invalid credit card Dirty String`() =
      assertFalse(creditCardValidator.isValid("51$1%2ˆ9&3*4(5)2a7f2g4h763"))

  @Test
  fun `Valid Visa credit card`() =
      Assert.assertEquals(creditCardValidator.getCardType(4511293452724763), CreditCardValidator.Type.VISA)

  @Test
  fun `Valid Mastercard credit card`() =
      Assert.assertEquals(creditCardValidator.getCardType(5555555555554444), CreditCardValidator.Type.MASTER_CARD)

  @Test
  fun `Valid American Express credit card`() =
      Assert.assertEquals(creditCardValidator.getCardType(348286442345928), CreditCardValidator.Type.AMERICAN_EXPRESS)

  @Test
  fun `Invalid Unknown credit card`() =
      Assert.assertEquals(creditCardValidator.getCardType(1111111111111111), CreditCardValidator.Type.UNKNOWN)

}
