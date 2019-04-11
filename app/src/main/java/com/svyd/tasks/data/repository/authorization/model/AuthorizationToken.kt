package com.svyd.tasks.data.repository.authorization.model

import com.google.gson.annotations.SerializedName

data class AuthorizationToken(@field:SerializedName("token")
                         val token: String)
