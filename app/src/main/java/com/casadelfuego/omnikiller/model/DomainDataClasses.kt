package com.casadelfuego.omnikiller.model

import java.util.*

data class Card(
        var id: String?,
        var activated: Boolean?,
        var activationCode: String?,
        var expirationDate: Date?,
        var denomination: Number?,
        var activatedDate: Date?,
        var hint: String?,
        var currentBalance: Number?,
        var reloadable: Boolean?,
        var atmAccess: Boolean?,
        var servicePak: Boolean?,
        var order: Order?,
        var cardNumber: String?,
        var registration: Registration?,
        var fullRegistration: Boolean?,
        var promotions: Promotions?
)

data class Order(
        var id: String?,
        var purchaseDate: Date?,
        var type: String?
)

data class Promotions(
        var fiveBackEnabled: Boolean?
)

data class Registration(
        var firstName: String?,
        var lastName: String?,
        var address1: String?,
        var address2: String?,
        var city: String?,
        var state: String?,
        var zip: String?,
        var email: String?,
        var mobile: String?
)