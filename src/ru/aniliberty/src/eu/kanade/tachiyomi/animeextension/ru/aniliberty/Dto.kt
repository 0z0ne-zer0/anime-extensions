package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import eu.kanade.tachiyomi.animesource.model.SAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReleaseData(
    val id: Int,
    val alias: String,
    val name: ReleaseName,
    val year: Int,
    val season: ReleaseSeason,
    val poster: ReleasePoster,
    @SerialName("is_ongoing") val isOngoing: Boolean,
    val description: String,
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
        val preview: String,
        val thumbnail: String,
    )

    @Serializable
    class ReleaseGenre(
        val id: Int,
        val name: String,
    )

    private fun descriptionBuilder(): String {
        val builder = StringBuilder()
        if (isBlockedByGeo) {
            builder.append("🛑 ДАННОЕ АНИМЕ ЗАБЛОКИРОВАНО В СВЯЗИ С РЕГИОНАЛЬНЫМИ ОГРАНИЧЕНИЯМИ🛑\n")
        }
        if (isBlockedByCopyright) {
            builder.append("🛑 ДАННОЕ АНИМЕ ЗАБЛОКИРОВАНО В СВЯЗИ С КОПИРАЙТАМИ 🛑\n")
        }
        if (notification != null) {
            builder.append("🛑 $notification 🛑\n")
        }
        builder.append("Ceзон: ${season.description} $year\n")
        builder.append("Статус озвучки: ${if (isInProduction) "В процессе" else "Завершен"}\n")

        if (builder.isNotEmpty()) {
            builder.append(description)
        }

        return builder.toString()
    }

    fun toSAnime(): SAnime? = SAnime.create().apply {
        url = alias
        title = name.russian
        thumbnail_url = poster.thumbnail
        description = descriptionBuilder()
        genre = genres.joinToString(", ") { it.name }
        status = if (isOngoing) SAnime.ONGOING else SAnime.COMPLETED
    }
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
