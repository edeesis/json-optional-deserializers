package com.edeesis.json.objects

import com.edeesis.json.optional.JsonOptional

sealed interface KotlinJsonOptionalObject

data class JsonOptionalParameter(
    val value: JsonOptional<String>
) : KotlinJsonOptionalObject

data class JsonOptionalWrappedNullableParameter(
    val value: JsonOptional<String?>
) : KotlinJsonOptionalObject

data class NullableJsonOptionalParameter(
    val value: JsonOptional<String>?
) : KotlinJsonOptionalObject

data class NullableJsonOptionalWrappedNullableParameter(
    val value: JsonOptional<String?>?
) : KotlinJsonOptionalObject