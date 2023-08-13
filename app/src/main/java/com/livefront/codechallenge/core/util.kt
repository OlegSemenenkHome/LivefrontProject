package com.livefront.codechallenge.core

/*
 * function to replace "-" with "Unknown"
 * as the api has more than a few unknowns
 */
internal fun String.checkIfUnknown(): String {
    return if (this == "-") "Unknown" else
        this
}