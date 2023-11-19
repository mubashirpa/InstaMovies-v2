package instamovies.app.core.ext

import kotlin.math.floor
import kotlin.math.round

/**
 * Round off the value to it's highest value
 */
fun Double.roundHighest(): Double {
    return round(this * 10) / 10
}

/**
 * Round off the value to it's lowest value
 */
fun Double.roundLowest(): Double {
    return floor(this * 10) / 10
}
