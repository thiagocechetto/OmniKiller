package com.casadelfuego.omnikiller.model

data class CardResponse(val card: CardDto?)

data class CardDto(
        var id: String?,
        var activated: String?,
        var activationCode: Any?,
        var expirationDate: String?,
        var denomination: String?,
        var activatedDate: String?,
        var hint: String?,
        var currentBalance: String?,
        var reloadable: String?,
        var atmAccess: String?,
        var servicePak: String?,
        var order: OrderDto?,
        var ccNumber: String?,
        var registration: RegistrationDto?,
        var fullRegistration: Boolean?,
        var promotions: PromotionsDto?
)

data class OrderDto(
        var id: String?,
        var purchaseDate: String?,
        var type: String?
)

data class PromotionsDto(
        var fiveBackEnabled: Boolean?
)

data class RegistrationDto(
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