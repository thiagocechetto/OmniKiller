package com.casadelfuego.omnikiller.ocr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CreditCardHelperTest {

  private val creditCardHelper = CreditCardHelper()

  @Test
  fun `Is big enough`() =
      assertTrue(creditCardHelper.isBigEnough("1234567890123456"))

  @Test
  fun `Is not big enough`() =
      assertFalse(creditCardHelper.isBigEnough("123456789012"))

  @Test
  fun `Is bigger than enough`() =
      assertTrue(creditCardHelper.isBigEnough("12345678901234567"))

  @Test
  fun `Find one valid credit card`() =
      assertEquals(creditCardHelper.scavengeForValidCreditCards("4511293452724763").size, 1)

  @Test
  fun `Find one valid credit card with chars`() =
      assertEquals(creditCardHelper.scavengeForValidCreditCards("4511A29$345%2724^763)_+").size, 1)

  @Test
  fun `Find two valid credit card with chars`() =
      assertEquals(creditCardHelper.scavengeForValidCreditCards("0.40 - 4511 - 2934 - 5272 - 4763 - 722").size, 1)

}
