package com.example.android.politicalpreparedness.network

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.android.politicalpreparedness.network.models.Election
import com.squareup.moshi.Moshi

open class UserRequestConverter {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromJson(string: String): Election? {
        if (TextUtils.isEmpty(string))
            return null

        val jsonAdapter = moshi.adapter(Election::class.java)
        return jsonAdapter.fromJson(string)
    }

    @TypeConverter
    fun toJson(division: Election): String {
        val jsonAdapter = moshi.adapter(Election::class.java)
        return jsonAdapter.toJson(division)
    }
}