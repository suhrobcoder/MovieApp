package uz.suhrob.movieapp.data.db.converter

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun listToString(list: List<Int>): String {
        return list.joinToString(separator = " ")
    }

    @TypeConverter
    fun stringToList(s: String): List<Int> {
        val list = ArrayList<Int>()
        for (i in s.split(" ")) {
            if (i.isNotEmpty()) {
                list.add(i.toInt())
            }
        }
        return list
    }
}