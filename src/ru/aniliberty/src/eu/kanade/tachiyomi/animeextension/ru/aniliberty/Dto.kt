package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReleaseData(
    val id: Int,
    val alias: String = "",
    val name: ReleaseName,
    val year: Int,
    val season: ReleaseSeason,
    val poster: ReleasePoster,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    val description: String? = null,
    val notification: String? = null,
    @SerialName("external_player") val externalPlayer: String? = null,
    @SerialName("is_in_production") val isInProduction: Boolean = false,
    @SerialName("is_blocked_by_geo") val isBlockedByGeo: Boolean = false,
    @SerialName("is_blocked_by_copyrights") val isBlockedByCopyright: Boolean = false,
    val genres: List<ReleaseGenre>,
) {

    @Serializable
    class ReleaseName(
        @SerialName("main") val russian: String,
        val english: String,
        val alternative: String? = null,
    )

    @Serializable
    class ReleaseSeason(
        val description: String? = null,
    )

    @Serializable
    class ReleasePoster(
        val src: String? = null,
        val preview: String? = null,
        val thumbnail: String? = null,
    )

    @Serializable
    class ReleaseGenre(
        val id: Int,
        val name: String,
    )
}

@Serializable
class SearchResult(
    val data: List<ReleaseData>,
    val meta: SearchMeta,
) {

    @Serializable
    class SearchMeta(
        val pagination: SearchPagination,
    ) {
        @Serializable
        class SearchPagination(
            @SerialName("current_page") val currentPage: Int,
            @SerialName("total_pages") val totalPages: Int,
        )
    }
}
