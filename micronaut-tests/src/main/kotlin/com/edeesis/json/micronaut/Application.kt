package com.edeesis.json.micronaut

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    Micronaut.build()
        .args(*args)
        .packages("com.edeesis.json.micronaut")
        .start()
}