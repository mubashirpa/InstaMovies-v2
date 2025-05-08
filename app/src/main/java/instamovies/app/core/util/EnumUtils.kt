package instamovies.app.core.util

inline fun <reified T : Enum<*>> enumValueOf(name: String?): T? = T::class.java.enumConstants?.firstOrNull { it.name == name }
