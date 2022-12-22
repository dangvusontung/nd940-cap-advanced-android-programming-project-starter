package com.example.android.politicalpreparedness.network.models

data class RepresentativeInfo(
    val offices: List<Office>,
    val officials: List<RepresentativeOfficial>
)