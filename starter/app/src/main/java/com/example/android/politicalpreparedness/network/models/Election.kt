package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.UserRequestConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*
import kotlinx.android.parcel.Parcelize

@TypeConverters(UserRequestConverter::class)
@JsonClass(generateAdapter = true)
@Entity(tableName = "election_table")
@Parcelize
data class Election(
    @Json(name = "id")
    @PrimaryKey val id: Int,
    @Json(name = "name")
    @ColumnInfo(name = "name") val name: String,
    @Json(name = "electionDay")
    @ColumnInfo(name = "electionDay") val electionDay: String,
    @Json(name = "ocdDivisionId")
    @ColumnInfo(name = "division")
    val division: String
):Parcelable