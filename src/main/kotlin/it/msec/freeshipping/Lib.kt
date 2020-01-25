package it.msec.freeshipping

infix fun <A, B, C> ((A) -> B).andThen(that: (B) -> C): (A) -> C = { a-> that(this(a)) }
