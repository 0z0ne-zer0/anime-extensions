package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SearchReleaseData(
    val id: Int,
    val alias: String = "",
    val name: ReleaseName,
    val poster: ReleasePoster,
    @SerialName("description") val animeDescription: String?,
    val genres: List<ReleaseGenre>,
) {
    @Serializable
    class ReleaseName(
        @SerialName("main") val russian: String,
        val english: String,
        val alternative: String?,
    )

    @Serializable
    class ReleasePoster(
        val src: String?,
        val preview: String?,
        val thumbnail: String?,
    )

    @Serializable
    class ReleaseGenre(
        val id: Int,
        val name: String,
    ) {
        override fun toString(): String = name
    }
}

@Serializable
class SearchResult(
    val data: List<SearchReleaseData>,
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

@Serializable
class ReleaseData(
    val year: Int,
    val season: ReleaseSeason,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    val description: String?,
    val notification: String?,
    @SerialName("is_in_production") val isInProduction: Boolean = false,
    @SerialName("is_blocked_by_geo") val isBlockedByGeo: Boolean = false,
    @SerialName("is_blocked_by_copyrights") val isBlockedByCopyright: Boolean = false,
    val genres: List<ReleaseGenre>,
) {
    @Serializable
    class ReleaseSeason(
        val description: String?,
    )

    @Serializable
    class ReleaseGenre(
        val id: Int,
        val name: String,
    )
}

@Serializable
class EpisodeList(
    val episodes: List<EpisodeData>,
) {
    @Serializable
    class EpisodeData(
        val id: String,
        val name: String?,
        val ordinal: Float,
        @SerialName("sort_order") val sortOrder: Int,
        val preview: EpisodePreview,
        @SerialName("updated_at") val updatedAt: String,
    ) {
        @Serializable
        class EpisodePreview(
            val src: String?,
            val preview: String?,
            val thumbnail: String?,
        )
    }
}

@Serializable
class VideoData(
    @SerialName("hls_480") val lqStream: String,
    @SerialName("hls_720") val mqStream: String,
    @SerialName("hls_1080") val hqStream: String,
)
