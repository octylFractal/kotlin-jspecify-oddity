package sample.jspecify.oddity.different

import sample.jspecify.oddity.Subtype

inline fun <T : Any, reified U : T> Subtype<out T>.getExtended(): U = get().let {
    it as? U ?: error("failed")
}
