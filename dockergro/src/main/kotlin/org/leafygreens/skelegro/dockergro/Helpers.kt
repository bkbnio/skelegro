package org.leafygreens.skelegro.dockergro

fun String.modify(transformer: (String) -> String) = transformer(this)
