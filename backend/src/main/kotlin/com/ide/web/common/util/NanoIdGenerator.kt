package com.ide.web.common.util

import java.security.SecureRandom

object NanoIdGenerator {
    private const val DEFAULT_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private const val DEFAULT_SIZE = 12
    private val random = SecureRandom()

    fun generate(size: Int = DEFAULT_SIZE, alphabet: String = DEFAULT_ALPHABET): String {
        val sb = StringBuilder(size)
        repeat(size) {
            sb.append(alphabet[random.nextInt(alphabet.length)])
        }
        return sb.toString()
    }
}