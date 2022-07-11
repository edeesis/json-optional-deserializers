package com.edeesis.json.micronaut

import com.edeesis.json.objects.*
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch

@Controller("deserialization/kotlin")
class DeserializationController(
    private val service: Service
) {
    @Patch("optional")
    fun kotlin(@Body body: OptionalParameter) {
        service.accept(body)
    }

    @Patch("optional-nullable")
    fun kotlin(@Body body: OptionalWrappedNullableParameter) {
        service.accept(body)
    }

    @Patch("nullable-optional")
    fun kotlin(@Body body: NullableOptionalParameter) {
        service.accept(body)
    }

    @Patch("nullable-optional-nullable")
    fun kotlin(@Body body: NullableOptionalWrappedNullableParameter) {
        service.accept(body)
    }

    @Patch("nullable")
    fun kotlin(@Body body: NullableParameter) {
        service.accept(body)
    }

    @Patch("required")
    fun kotlin(@Body body: RequiredParameter) {
        service.accept(body)
    }


}