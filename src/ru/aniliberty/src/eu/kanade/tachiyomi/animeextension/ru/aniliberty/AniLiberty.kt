package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import android.app.Application
import android.util.Log
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
            addQueryParameter("limit", 20.toString())
            addQueryParameter("page", page.toString())
        }.build()
        Log.d("AniLiberty", "URL: $url")

        val dto = client.get(url = url, headers = apiHeaders).parseAs<SearchResult>()
        Log.d("AniLiberty", "Response data: ${dto.data.joinToString("; "){ it.name.russian }}")
        val animes = dto.data.mapNotNull { it.toSAnime() }
        val hasNextPage = dto.meta.pagination.currentPage < dto.meta.pagination.totalPages

        return AnimesPage(animes, hasNextPage)
    }
    override fun popularAnimeRequest(page: Int): Request = throw UnsupportedOperationException()

    override fun popularAnimeParse(response: Response): AnimesPage = throw UnsupportedOperationException()

    // =============================== Latest ===============================

    override suspend fun getLatestUpdates(page: Int): AnimesPage {
        val url = "$apiUrl/anime/catalog/releases".toHttpUrl().newBuilder().apply {
            addQueryParameter("f[sorting]", "FRESH_AT_DESC")
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

    override fun getFilterList(): AnimeFilterList = Filters.FILTER_LIST

    override suspend fun getSearchAnime(page: Int, query: String, filters: AnimeFilterList): AnimesPage {
        val url = "$apiUrl/anime/catalog/releases".toHttpUrl().newBuilder().apply {
            addQueryParameter("limit", 20.toString())
            addQueryParameter("page", page.toString())

            if (query.isNotBlank()) {
                addQueryParameter("f[search]", query)
            }

            filters.forEach { filter ->
                when (filter) {
                    is Filters.SortFilter -> addQueryParameter("f[sorting]", filter.getValue())
                    is Filters.TypeFilter -> addQueryParameter("f[types]", filter.getValue())
                    else -> {}
                }
            }
        }.build()
        Log.d("AniLiberty", "URL: $url")

        val dto = client.get(url = url, headers = apiHeaders).parseAs<SearchResult>()
        Log.d("AniLiberty", "Response data: ${dto.data.joinToString("; "){ it.name.russian }}")
        val animes = dto.data.map { it.toSAnime() }
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
}
