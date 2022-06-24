package com.example.a10.dars.sodda.passportgeneration.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Passport : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var surName: String? = null
    var middleName: String? = null
    var passportNumber: String? = null
    var region: String? = null
    var city: String? = null
    var description: String? = null
    var passportDate: String? = null
    var bestBeforeDate: String? = null
    var gender: String? = null
    var image: String? = null

    constructor(
        name: String?,
        surName: String?,
        middleName: String?,
        passportNumber: String?,
        region: String?,
        city: String?,
        description: String?,
        passportDate: String?,
        bestBeforeDate: String?,
        gender: String?,
        image: String?
    ) {
        this.name = name
        this.surName = surName
        this.middleName = middleName
        this.passportNumber = passportNumber
        this.region = region
        this.city = city
        this.description = description
        this.passportDate = passportDate
        this.bestBeforeDate = bestBeforeDate
        this.gender = gender
        this.image = image
    }

}