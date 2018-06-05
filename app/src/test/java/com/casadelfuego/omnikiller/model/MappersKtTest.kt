package com.casadelfuego.omnikiller.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class MappersKtTest {

    @Test
    fun nullCardDtoToDomain() {
        val cardDto = CardDto(null, null, null, null,
                null, null, null, null, null,
                null, null, null, null,
                null, null, null
        )

        val card = cardDto.toDomain()

        assertNotNull(card)
    }

    @Test
    fun cardDtoToDomain() {
        val cardDto = CardDto(
                "1",
                "1",
                "1",
                "2019-05-24",
                "500.00",
                "2018-05-24 20:34:48",
                "hint",
                "10.00",
                "0",
                "0",
                "0",
                null,
                "451129xxxxxx5491",
                null,
                false,
                null
        )

        val card = cardDto.toDomain()

        assertEquals("1", card.id)
        assertEquals(true, card.activated)
        assertEquals("1", card.activationCode)

        val expDateCal = Calendar.getInstance()
        expDateCal.time = card.expirationDate
        assertEquals(2019, expDateCal.get(Calendar.YEAR))
        assertEquals(5, expDateCal.get(Calendar.MONTH) + 1)
        assertEquals(24, expDateCal.get(Calendar.DAY_OF_MONTH))

        assertEquals(500.00, card.denomination)

        val actDateCal = Calendar.getInstance()
        actDateCal.time = card.activatedDate
        assertEquals(2018, actDateCal.get(Calendar.YEAR))
        assertEquals(5, actDateCal.get(Calendar.MONTH) + 1)
        assertEquals(24, actDateCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(20, actDateCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(34, actDateCal.get(Calendar.MINUTE))
        assertEquals(48, actDateCal.get(Calendar.SECOND))

        assertEquals("hint", card.hint)
        assertEquals(10.00, card.currentBalance)
        assertEquals(false, card.reloadable)
        assertEquals(false, card.atmAccess)
        assertEquals(false, card.servicePak)
        assertEquals("451129xxxxxx5491", card.cardNumber)
        assertEquals(false, card.fullRegistration)
    }

    @Test
    fun orderDtoToDomain() {
        val orderDto = OrderDto("2", "2018-05-24 20:34:48", "shopping")

        val order = orderDto.toDomain()

        assertEquals("2", order.id)

        val purchaseDateCal = Calendar.getInstance()
        purchaseDateCal.time = order.purchaseDate
        assertEquals(2018, purchaseDateCal.get(Calendar.YEAR))
        assertEquals(5, purchaseDateCal.get(Calendar.MONTH) + 1)
        assertEquals(24, purchaseDateCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(20, purchaseDateCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(34, purchaseDateCal.get(Calendar.MINUTE))
        assertEquals(48, purchaseDateCal.get(Calendar.SECOND))

        assertEquals("shopping", order.type)
    }

    @Test
    fun registrationDtoToDomain() {
        val registrationDto = RegistrationDto(
                "John",
                "Doe",
                "750 Lake Street",
                "Room 10",
                "Rainycliff",
                "SC",
                "95084",
                "d@c.com",
                "(123) 123-1231"
        )

        val registration = registrationDto.toDomain()

        assertEquals("John", registration.firstName)
        assertEquals("Doe", registration.lastName)
        assertEquals("750 Lake Street", registration.address1)
        assertEquals("Room 10", registration.address2)
        assertEquals("Rainycliff", registration.city)
        assertEquals("SC", registration.state)
        assertEquals("95084", registration.zip)
        assertEquals("d@c.com", registration.email)
        assertEquals("(123) 123-1231", registration.mobile)
    }

    @Test
    fun promotionsDtoToDomain() {
        val promotionsDto = PromotionsDto(true)

        val promotions = promotionsDto.toDomain()

        assertEquals(true, promotions.fiveBackEnabled)
    }
}