package com.ide.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebIdeApplication

fun main(args: Array<String>) {
	runApplication<WebIdeApplication>(*args)
}
