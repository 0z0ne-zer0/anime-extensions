package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import eu.kanade.tachiyomi.animesource.model.AnimeFilter
import eu.kanade.tachiyomi.animesource.model.AnimeFilterList

object Filters {

    class TypeFilter : AnimeFilter.Select<String>("Тип", TYPE_ENTRIES.toTypedArray(), 0) {
        fun getValue() = TYPE_VALUES[state]

        companion object {
            private val TYPE_ENTRIES = listOf("ТВ", "ONA", "WEB", "OVA", "OAD", "Фильм", "Дорама", "Спешл")
            private val TYPE_VALUES = listOf("TV", "ONA", "WEB", "OVA", "OAD", "MOVIE", "DORAMA", "SPECIAL")
        }
    }

    class SortFilter : AnimeFilter.Select<String>("Сортировать по", SORT_ENTRIES.toTypedArray(), 0) {
        fun getValue() = SORT_VALUES[state]
        companion object {
            private val SORT_ENTRIES = listOf("Популярность", "Свежесть", "Год")
            private val SORT_VALUES = listOf("RATING_DESC", "FRESH_AT_DESC", "YEAR_DESC")
        }
    }

    val FILTER_LIST get() = AnimeFilterList(
        SortFilter(),
        TypeFilter(),
    )
}
