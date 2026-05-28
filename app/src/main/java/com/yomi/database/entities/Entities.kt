package com.yomi.database.entities
import androidx.room.*

@Entity(tableName = "players")
data class PlayerEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String,
    val avatarEmoji: String, val avatarColor: String, val createdAt: Long = System.currentTimeMillis())

@Entity(tableName = "stories",
    foreignKeys = [ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["creatorId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("creatorId")])
data class StoryEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val title: String,
    val creatorId: Long, val totalPanels: Int, val currentPanelIndex: Int = 0,
    val status: String = "IN_PROGRESS", val playerOrder: String = "",
    val createdAt: Long = System.currentTimeMillis(), val completedAt: Long? = null, val inviteCode: String = "")

@Entity(tableName = "panels",
    foreignKeys = [ForeignKey(entity = StoryEntity::class, parentColumns = ["id"], childColumns = ["storyId"], onDelete = ForeignKey.CASCADE),
                   ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["authorId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("storyId"), Index("authorId")])
data class PanelEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val storyId: Long,
    val authorId: Long, val authorName: String, val panelIndex: Int, val imagePath: String,
    val dialogText: String = "", val submittedAt: Long = System.currentTimeMillis(), val isEmpty: Boolean = false)

@Entity(tableName = "reactions",
    foreignKeys = [ForeignKey(entity = StoryEntity::class, parentColumns = ["id"], childColumns = ["storyId"], onDelete = ForeignKey.CASCADE),
                   ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["playerId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("storyId"), Index("playerId")], primaryKeys = ["storyId", "playerId"])
data class ReactionEntity(val storyId: Long, val playerId: Long, val emoji: String, val createdAt: Long = System.currentTimeMillis())
