package io.bkbn.skelegro.docker

fun String.modify(transformer: (String) -> String) = transformer(this)
