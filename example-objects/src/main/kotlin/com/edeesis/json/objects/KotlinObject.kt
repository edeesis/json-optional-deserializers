package com.edeesis.json.objects

import java.util.*

sealed interface KotlinObject

data class OptionalParameter(
    val value: Optional<String>
) : KotlinObject

data class OptionalWrappedNullableParameter(
    val value: Optional<String?>
) : KotlinObject

data class NullableOptionalParameter(
    val value: Optional<String>?
) : KotlinObject

data class NullableOptionalWrappedNullableParameter(
    val value: Optional<String?>?
) : KotlinObject

data class NullableParameter(
    val value: String?
) : KotlinObject

data class RequiredParameter(
    val value: String
) : KotlinObject