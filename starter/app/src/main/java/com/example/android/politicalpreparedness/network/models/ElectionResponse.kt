package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.Json

data class ElectionResponse(
        @Json(name="elections")val elections: List<Election>,
        @Json(name="kind")val kind: String,
)