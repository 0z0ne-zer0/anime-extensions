package eu.kanade.tachiyomi.animeextension.ru.aniliberty

import eu.kanade.tachiyomi.animesource.model.AnimeFilter
import eu.kanade.tachiyomi.animesource.model.AnimeFilterList

object Filters {

    class TypeFilterCheckbox(name: String, val value: String) : AnimeFilter.CheckBox(name, false)
    class TypeFilter : AnimeFilter.Group<TypeFilterCheckbox>("Тип", TYPE_MAP.map { TypeFilterCheckbox(it.first, it.second) }) {
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

    val FILTER_LIST get() = AnimeFilterList(
        SortFilter(),
        TypeFilter(),
    )
}
