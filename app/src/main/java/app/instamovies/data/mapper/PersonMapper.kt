package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.person.credits.Cast
import app.instamovies.data.remote.dto.person.credits.PersonCreditsDto
import app.instamovies.data.remote.dto.person.details.PersonDetailsDto
import app.instamovies.data.remote.dto.person.images.PersonImagesDto
import app.instamovies.data.remote.dto.person.images.Profile
import app.instamovies.data.remote.dto.person.popular.KnownFor
import app.instamovies.data.remote.dto.person.popular.PersonResult
import app.instamovies.domain.model.person.PersonDetails
import app.instamovies.domain.model.person.credits.PersonCast
import app.instamovies.domain.model.person.credits.PersonCredits
import app.instamovies.domain.model.person.images.PersonImages
import app.instamovies.domain.model.person.images.PersonProfile
import app.instamovies.domain.model.person.popular.KnownForModel
import app.instamovies.domain.model.person.popular.PersonResultModel

fun PersonResult.toPersonResultModel(): PersonResultModel {
    return PersonResultModel(
        id,
        knownFor?.map { it.toKnownForModel() },
        knownForDepartment,
        name,
        profilePath,
    )
}

fun KnownFor.toKnownForModel(): KnownForModel {
    return KnownForModel(mediaType, name, title)
}

fun PersonDetailsDto.toPersonDetails(): PersonDetails {
    return PersonDetails(
        biography,
        birthday,
        knownForDepartment,
        name,
        placeOfBirth,
        popularity,
        profilePath,
        credits?.toPersonCredits(),
        images?.toPersonImages(),
    )
}

fun PersonCreditsDto.toPersonCredits(): PersonCredits {
    return PersonCredits(cast?.map { it.toPersonCast() })
}

fun Cast.toPersonCast(): PersonCast {
    return PersonCast(
        firstAirDate,
        id,
        mediaType,
        name,
        posterPath,
        releaseDate,
        title,
        voteAverage,
    )
}

fun PersonImagesDto.toPersonImages(): PersonImages {
    return PersonImages(profiles?.map { it.toPersonProfile() })
}

fun Profile.toPersonProfile(): PersonProfile {
    return PersonProfile(filePath)
}
