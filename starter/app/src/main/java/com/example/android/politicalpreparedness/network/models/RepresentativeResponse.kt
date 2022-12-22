package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.Json

data class RepresentativeResponse(
        @Json(name="offices")val offices: List<Office>,
        @Json(name="officials")val officials: List<Official>
)