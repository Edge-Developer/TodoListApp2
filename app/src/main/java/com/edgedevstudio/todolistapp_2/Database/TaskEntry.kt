package com.edgedevstudio.todolistapp_2.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Olorunleke Opeyemi on 21/01/2019.
 **/

@Entity(tableName = "task")
class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var description: String? = null
    var priority: Int = 0
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date? = null

    constructor(id: Int, description: String?, priority: Int, updatedAt: Date?) {
        this.id = id
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }

    @Ignore
    constructor(description: String?, priority: Int, updatedAt: Date?) {
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }
}