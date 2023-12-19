package com.waminiyi.realestatemanager.core.model.data

import java.sql.Timestamp

data class Commit(
    val commitId: String,
    val changes: List<Change>,
    val timestamp: Timestamp,
    val authorId:String
)