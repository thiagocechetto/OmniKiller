package com.casadelfuego.omnikiller.model

import java.util.*

data class Card(
        var id: String? = null,
        var activated: Boolean? = null,
        var activationCode: String? = null,
        var expirationDate: Date? = null,
        var denomination: Number? = null,
        var activatedDate: Date? = null,
        var hint: String? = null,
        var currentBalance: Number? = null,
        var reloadable: Boolean? = null,
        var atmAccess: Boolean? = null,
        var servicePak: Boolean? = null,
        var order: Order? = null,
        var cardNumber: String? = null,
        var registration: Registration? = null,
        var fullRegistration: Boolean? = null,
        var promotions: Promotions? = null
)

data class Order(
        var id: String? = null,
        var purchaseDate: Date? = null,
        var type: String? = null
)

data class Promotions(
        var fiveBackEnabled: Boolean? = null
)

data class Registration(
        var firstName: String? = null,
        var lastName: String? = null,
        var address1: String? = null,
        var address2: String? = null,
        var city: String? = null,
        var state: String? = null,
        var zip: String? = null,
        var email: String? = null,
        var mobile: String? = null
)