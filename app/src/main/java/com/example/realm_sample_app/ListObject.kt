package com.example.realm_sample_app

import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class ListObject {
    @PrimaryKey
    var id : Int? = null
    @Required
    var title = ""
}