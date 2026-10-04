package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import android.os.Build
import androidx.annotation.RequiresApi
import eu.kanade.tachiyomi.animesource.model.AnimeFilter
import eu.kanade.tachiyomi.animesource.model.AnimeFilterList

object Filters {

    class TypeCheckBox(name: String, val value: String) : AnimeFilter.CheckBox(name, false)
    class TypeFilter : AnimeFilter.Group<TypeCheckBox>("Тип", TYPE_MAP.map { TypeCheckBox(it.first, it.second) }) {
        fun getSelectedValues(): String = state.filter { it.state }.joinToString(",") { it.value }
        companion object {
            private val TYPE_MAP = listOf(
                Pair("ТВ", "TV"),
                Pair("ONA", "ONA"),
                Pair("WEB", "WEB"),
                Pair("OVA", "OVA"),
                Pair("OAD", "OAD"),
                Pair("Фильм", "MOVIE"),
                Pair("Дорама", "DORAMA"),
                Pair("Спешл", "SPECIAL"),
            )
        }
    }

    class SortFilter : AnimeFilter.Select<String>("Сортировать по", SORT_ENTRIES.toTypedArray(), 0) {
        fun getValue() = SORT_VALUES[state]
        companion object {
            private val SORT_ENTRIES = listOf("Популярность", "Свежесть", "Год")
            private val SORT_VALUES = listOf("RATING_DESC", "FRESH_AT_DESC", "YEAR_DESC")
        }
    }

    class GenreCheckBox(name: String, val value: Int) : AnimeFilter.CheckBox(name, false)
    class GenreFilter : AnimeFilter.Group<GenreCheckBox>("Жанры", GENRES.map { GenreCheckBox(it.first, it.second) }) {
        fun getSelectedValues(): String = state.filter { it.state }.joinToString(",") { it.value.toString() }
        companion object {
            private val GENRES = listOf(
                Pair("Боевые искусства", 15),
                Pair("Вампиры", 24),
                Pair("Гарем", 32),
                Pair("Демоны", 16),
                Pair("Детектив", 25),
                Pair("Дзёсей", 33),
                Pair("Драма", 8),
                Pair("Игры", 17),
                Pair("Исекай", 34),
                Pair("Исторический", 26),
                Pair("Киберпанк", 30),
                Pair("Комедия", 1),
                Pair("Магия", 18),
                Pair("Меха", 2),
                Pair("Мистика", 9),
                Pair("Музыка", 19),
                Pair("Пародия", 36),
                Pair("Повседневность", 10),
                Pair("Приключения", 27),
                Pair("Психологическое", 3),
                Pair("Романтика", 11),
                Pair("Сверхъестественное", 28),
                Pair("Сёдзе", 20),
                Pair("Сёдзе-ай", 31),
                Pair("Сейнен", 5),
                Pair("Сёнен", 4),
                Pair("Спорт", 12),
                Pair("Супер сила", 21),
                Pair("Триллер", 6),
                Pair("Ужасы", 13),
                Pair("Фантастика", 22),
                Pair("Фэнтези", 29),
                Pair("Школа", 7),
                Pair("Экшен", 14),
                Pair("Этти", 23),
            )
        }
    }

    class SeasonCheckBox(name: String, val value: String) : AnimeFilter.CheckBox(name, false)
    class SeasonFilter : AnimeFilter.Group<SeasonCheckBox>("Сезон", SEASON_MAP.map { SeasonCheckBox(it.first, it.second) }) {
        fun getSelectedValues(): String = state.filter { it.state }.joinToString(",") { it.value }
        companion object {
            private val SEASON_MAP = listOf(
                Pair("Весна", "SPRING"),
                Pair("Лето", "SUMMER"),
                Pair("Осень", "FALL"),
                Pair("Зима", "WINTER"),
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    class YearFilter : AnimeFilter.Select<String>("Year", YEAR_ENTRIES.toTypedArray(), 0) {
        fun getValue() = if (state == 0) null else YEAR_VALUES[state]
        companion object {
            @RequiresApi(Build.VERSION_CODES.O)
            private val currentYear = java.time.LocalDate.now().year

            @RequiresApi(Build.VERSION_CODES.O)
            private val YEAR_ENTRIES = listOf("Любой") + (currentYear downTo 1977).map { it.toString() }

            @RequiresApi(Build.VERSION_CODES.O)
            private val YEAR_VALUES = listOf("") + (currentYear downTo 1977).map { it.toString() }
        }
    }

    class AgeRatingFilter : AnimeFilter.Select<String>("Age Rating", AGE_RATING_ENTRIES.toTypedArray(), 0) {
        fun getValue() = AGE_RATING_VALUES[state]
        companion object {
            private val AGE_RATING_ENTRIES = listOf("Любой", "0+", "6+", "12+", "16+", "18+")
            private val AGE_RATING_VALUES = listOf("", "R0_PLUS", "R6_PLUS", "R12_PLUS", "R16_PLUS", "R18_PLUS")
        }
    }

    class OngoingFilter : AnimeFilter.TriState("Онгоинг?", 0)
    class ProductionFilter : AnimeFilter.TriState("В процессе перевода?", 0)

    val FILTER_LIST
        @RequiresApi(Build.VERSION_CODES.O)
        get() = AnimeFilterList(
            SortFilter(),
            TypeFilter(),
            GenreFilter(),
            SeasonFilter(),
            YearFilter(),
            AgeRatingFilter(),
            OngoingFilter(),
            ProductionFilter(),
        )
}
