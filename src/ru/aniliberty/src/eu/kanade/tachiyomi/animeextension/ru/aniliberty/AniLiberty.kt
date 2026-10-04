package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceScreen
import eu.kanade.tachiyomi.animesource.ConfigurableAnimeSource
import eu.kanade.tachiyomi.animesource.model.AnimeFilterList
import eu.kanade.tachiyomi.animesource.model.AnimesPage
import eu.kanade.tachiyomi.animesource.model.Hoster
import eu.kanade.tachiyomi.animesource.model.SAnime
import eu.kanade.tachiyomi.animesource.model.SEpisode
import eu.kanade.tachiyomi.animesource.online.AnimeHttpSource
import keiyoushi.network.get
import keiyoushi.utils.parseAs
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.Response
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class AniLiberty :
    AnimeHttpSource(),
    ConfigurableAnimeSource {

    private val userAgent by lazy { }

    override val name: String = "AniLiberty"

    override val baseUrl: String = "https://aniliberty.top"

    private val apiUrl: String = "https://aniliberty.top/api/v1"

    override val lang: String = "ru"

    override val supportsLatest: Boolean = true

    private val apiHeaders: Headers = Headers.Builder()
        .add("Accept", "application/json")
        .add("Charset", "UTF-8")
        .build()

    private val defaultRemoveFilter: String = "torrents,player.rutube,names.alternative,type.full_string,season.week_day"

    private val defaultSearchFilter: String = "names,posters,id"

    private val preferences by lazy {
        Injekt.get<Application>().getSharedPreferences("source_$id", 0x0000)
    }

    companion object {
        private const val PREF_QUALITY_KEY = "preferred_quality"
        private const val PREF_QUALITY_TITLE = "Качество по умолчанию"
        private const val PREF_QUALITY_DEFAULT = "480p"
        private val PREF_QUALITY_ENTRIES = arrayOf("1080p", "720p", "480p")
        private val PREF_QUALITY_VALUES by lazy {
            PREF_QUALITY_ENTRIES.map { it.substringBefore("p") }.toTypedArray()
        }

        const val PREFIX_SEARCH = "prefix_path:"
    }

    // ============================== Popular ==============================

    override suspend fun getPopularAnime(page: Int): AnimesPage {
        val url = "$apiUrl/anime/catalog/releases".toHttpUrl().newBuilder().apply {
            addQueryParameter("f[sorting]", "RATING_DESC")
            addQueryParameter("include", "id,alias,name,poster")
            addQueryParameter("limit", 20.toString())
            addQueryParameter("page", page.toString())
        }.build()
        Log.d("AniLiberty", "URL: $url")

        val dto = client.get(url = url, headers = apiHeaders).parseAs<SearchResult>()
        Log.d("AniLiberty", "Response data: ${dto.data.joinToString("; "){ it.name.russian }}")
        val animes = dto.data.map { it.toSAnime() }
        val hasNextPage = dto.meta.pagination.currentPage < dto.meta.pagination.totalPages

        return AnimesPage(animes, hasNextPage)
    }
    override fun popularAnimeRequest(page: Int): Request = throw UnsupportedOperationException()

    override fun popularAnimeParse(response: Response): AnimesPage = throw UnsupportedOperationException()

    // =============================== Latest ===============================

    override suspend fun getLatestUpdates(page: Int): AnimesPage {
        val url = "$apiUrl/anime/catalog/releases".toHttpUrl().newBuilder().apply {
            addQueryParameter("f[sorting]", "FRESH_AT_DESC")
            addQueryParameter("include", "id,alias,name,poster")
            addQueryParameter("limit", 20.toString())
            addQueryParameter("page", page.toString())
        }.build()
        Log.d("AniLiberty", "URL: $url")

        val dto = client.get(url = url, headers = apiHeaders).parseAs<SearchResult>()
        Log.d("AniLiberty", "Response data: ${dto.data.joinToString("; "){ it.name.russian }}")
        val animes = dto.data.map { it.toSAnime() }
        val hasNextPage = dto.meta.pagination.currentPage < dto.meta.pagination.totalPages

        return AnimesPage(animes, hasNextPage)
    }

    override fun latestUpdatesRequest(page: Int): Request = throw UnsupportedOperationException()

    override fun latestUpdatesParse(response: Response): AnimesPage = throw UnsupportedOperationException()

    // =============================== Search ===============================

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFilterList(): AnimeFilterList = Filters.FILTER_LIST

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getSearchAnime(page: Int, query: String, filters: AnimeFilterList): AnimesPage {
        val url = "$apiUrl/anime/catalog/releases".toHttpUrl().newBuilder().apply {
            addQueryParameter("include", "id,alias,name,poster")
            addQueryParameter("limit", 20.toString())
            addQueryParameter("page", page.toString())

            if (query.isNotBlank()) {
                addQueryParameter("f[search]", query)
            }

            filters.forEach { filter ->
                when (filter) {
                    is Filters.SortFilter -> addQueryParameter("f[sorting]", filter.getValue())
                    is Filters.TypeFilter -> {
                        val types = filter.getSelectedValues()
                        if (types.isNotEmpty()) addQueryParameter("f[types]", types)
                    }
                    is Filters.GenreFilter -> {
                        val genres = filter.getSelectedValues()
                        if (genres.isNotEmpty()) addQueryParameter("f[genres]", genres)
                    }
                    is Filters.SeasonFilter -> {
                        val seasons = filter.getSelectedValues()
                        if (seasons.isNotEmpty()) addQueryParameter("f[seasons]", seasons)
                    }
                    is Filters.YearFilter -> {
                        addQueryParameter("f[years][from_year]", filter.getValue())
                        addQueryParameter("f[years][to_year]", filter.getValue())
                    }
                    is Filters.AgeRatingFilter -> addQueryParameter("f[age_ratings]", filter.getValue())
                    is Filters.OngoingFilter -> {
                        if (filter.state == 1) {
                            addQueryParameter("f[publish_statuses]", "IS_ONGOING")
                        }
                        if (filter.state == 2) {
                            addQueryParameter("f[publish_statuses]", "IS_NOT_ONGOING")
                        }
                    }
                    is Filters.ProductionFilter -> {
                        if (filter.state == 1) {
                            addQueryParameter("f[production_statuses]", "IS_IN_PRODUCTION")
                        }
                        if (filter.state == 2) {
                            addQueryParameter("f[production_statuses]", "IS_NOT_IN_PRODUCTION")
                        }
                    }
                    else -> {}
                }
            }
        }.build()
        Log.d("AniLiberty", "URL: $url")

        val dto = client.get(url = url, headers = apiHeaders).parseAs<SearchResult>()
        Log.d("AniLiberty", "Response data: ${dto.data.joinToString("; "){ it.name.russian }}")
        val animes = dto.data.map { it.toSAnime() }
        Log.d("AniLiberty", "Test URL: ${animes.joinToString { it.url }}")
        val hasNextPage = dto.meta.pagination.currentPage < dto.meta.pagination.totalPages

        return AnimesPage(animes, hasNextPage)
    }

    override fun searchAnimeRequest(
        page: Int,
        query: String,
        filters: AnimeFilterList,
    ): Request = throw UnsupportedOperationException()

    override fun searchAnimeParse(response: Response): AnimesPage = throw UnsupportedOperationException()

    // =========================== Anime Details ============================

    override fun getAnimeUrl(anime: SAnime): String = "$baseUrl/anime/releases/release/${ anime.url }"

    override fun animeDetailsParse(response: Response): SAnime {
        TODO("Not yet implemented")
    }

    override fun episodeListParse(response: Response): List<SEpisode> {
        TODO("Not yet implemented")
    }

    override fun seasonListParse(response: Response): List<SAnime> {
        TODO("Not yet implemented")
    }

    override fun hosterListParse(response: Response): List<Hoster> {
        TODO("Not yet implemented")
    }

    override fun setupPreferenceScreen(screen: PreferenceScreen) {
        TODO("Not yet implemented")
    }

    // ============================= Utilities ==============================

    private fun SearchReleaseData.toSAnime(): SAnime = SAnime.create().apply {
        url = alias.ifBlank { id.toString() }
        title = name.russian
        thumbnail_url = poster.let {
            if (it.src != null) {
                "$baseUrl/${it.src}"
            } else if (it.preview != null) {
                "$baseUrl/${it.preview}"
            } else if (it.thumbnail != null) {
                "$baseUrl/${it.thumbnail}"
            } else {
                ""
            }
        }
    }

    private fun ReleaseData.descriptionBuilder(): String = buildString {
        if (isBlockedByGeo) {
            append("🛑 ДАННОЕ АНИМЕ ЗАБЛОКИРОВАНО В СВЯЗИ С РЕГИОНАЛЬНЫМИ ОГРАНИЧЕНИЯМИ🛑\n")
        }
        if (isBlockedByCopyright) {
            append("🛑 ДАННОЕ АНИМЕ ЗАБЛОКИРОВАНО В СВЯЗИ С КОПИРАЙТАМИ 🛑\n")
        }
        if (notification != null) {
            append("🛑 $notification 🛑\n")
        }
        append("Ceзон: ${season.description ?: ""} $year\n")
        append("Статус озвучки: ${if (isInProduction) "В процессе" else "Завершен"}\n")

        if (description != null) {
            append(description)
        }
    }
}
