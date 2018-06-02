package com.casadelfuego.omnikiller.model

import com.casadelfuego.omnikiller.ktx.toBoolean
import com.casadelfuego.omnikiller.ktx.toDate
import com.casadelfuego.omnikiller.ktx.toDateTime

fun CardDto.toDomain() = Card(
        id,
        activated?.toBoolean(),
        activationCode as? String,
        expirationDate?.toDate(),
        denomination?.toDouble(),
        activatedDate?.toDateTime(),
        hint,
        currentBalance?.toDouble(),
        reloadable?.toBoolean(),
        atmAccess?.toBoolean(),
        servicePak?.toBoolean(),
        order?.toDomain(),
        ccNumber,
        registration?.toDomain(),
        fullRegistration,
        promotions?.toDomain()
)

fun OrderDto.toDomain() = Order(id, purchaseDate?.toDateTime(), type)

fun RegistrationDto.toDomain() = Registration(
        firstName,
        lastName,
        address1,
        address2,
        city,
        state,
        zip,
        email,
        mobile
)

fun PromotionsDto.toDomain() = Promotions(fiveBackEnabled)
