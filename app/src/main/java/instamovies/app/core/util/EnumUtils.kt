package instamovies.app.core.util

inline fun <reified T : Enum<T>> String.enumValueOf(defaultValue: T): T =
    enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue

inline fun <reified T : Enum<*>> enumValueOf(name: String?): T? = T::class.java.enumConstants?.firstOrNull { it.name == name }
