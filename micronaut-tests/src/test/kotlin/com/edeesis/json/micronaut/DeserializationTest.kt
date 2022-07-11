package com.edeesis.json.micronaut

import com.edeesis.json.objects.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension.getMock
import io.mockk.mockk
import io.mockk.verify
import java.util.*


@MicronautTest
class DeserializationTest(
    service: Service,
    @param:Client("/deserialization/kotlin")
    val client: HttpClient
) : StringSpec({

    fun exchange(uri: String, valueType: ValueType): HttpResponse<String> {
        return client.toBlocking().exchange(HttpRequest.PATCH(
            uri,
            when (valueType) {
                ValueType.UNDEFINED -> """ {} """
                ValueType.NULL -> """ { "value": null } """
                ValueType.DEFINED -> """ { "value": "value" } """
            }.trimIndent()
        ))
    }

    fun expect(expected: KotlinObject, uri: String, valueType: ValueType) {
        val mock = getMock(service)
        exchange(uri, valueType)
        verify { mock.accept(expected) }
    }



    "optional" {
        shouldThrow<HttpClientResponseException> { exchange(testCase.name.testName, ValueType.UNDEFINED) }.status shouldBe HttpStatus.BAD_REQUEST
        expect(OptionalParameter(Optional.ofNullable(null)), testCase.name.testName, ValueType.NULL)
        expect(OptionalParameter(Optional.of("value")), testCase.name.testName, ValueType.DEFINED)
    }

//    "optional-nullable" {
//        shouldThrow<HttpClientResponseException> { exchange(testCase.name.testName, ValueType.UNDEFINED) }.status shouldBe HttpStatus.BAD_REQUEST
//        expect(OptionalWrappedNullableParameter(Optional.ofNullable(null)), testCase.name.testName, ValueType.NULL)
//        expect(OptionalWrappedNullableParameter(Optional.of("value")), testCase.name.testName, ValueType.DEFINED)
//    }

    "nullable-optional" {
        expect(NullableOptionalParameter(null), testCase.name.testName, ValueType.UNDEFINED)
        expect(NullableOptionalParameter(Optional.ofNullable(null)), testCase.name.testName, ValueType.NULL)
        expect(NullableOptionalParameter(Optional.of("value")), testCase.name.testName, ValueType.DEFINED)
    }

//    "nullable-optional-nullable" {
//        expect(NullableOptionalWrappedNullableParameter(null), testCase.name.testName, ValueType.UNDEFINED)
//        expect(NullableOptionalWrappedNullableParameter(Optional.ofNullable(null)), testCase.name.testName, ValueType.NULL)
//        expect(NullableOptionalWrappedNullableParameter(Optional.of("value")), testCase.name.testName, ValueType.DEFINED)
//    }

    "nullable" {
        expect(NullableParameter(null), testCase.name.testName, ValueType.UNDEFINED)
        shouldThrow<HttpClientResponseException> { exchange(testCase.name.testName, ValueType.NULL) }.status shouldBe HttpStatus.BAD_REQUEST
        expect(NullableParameter("value"), testCase.name.testName, ValueType.DEFINED)
    }

    "required" {
        //shouldThrow<HttpClientResponseException> { exchange(testCase.name.testName, ValueType.UNDEFINED) }.status shouldBe HttpStatus.BAD_REQUEST
        //shouldThrow<HttpClientResponseException> { exchange(testCase.name.testName, ValueType.NULL) }.status shouldBe HttpStatus.BAD_REQUEST
        expect(RequiredParameter("value"), testCase.name.testName, ValueType.DEFINED)
    }
}) {

    @MockBean(Service::class)
    fun service(): Service {
        return mockk()
    }
}