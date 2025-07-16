package uzb.smt.questionsdemo.data.source.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uzb.smt.questionsdemo.data.source.storage.room.dao.QuestionDao
import uzb.smt.questionsdemo.data.source.storage.room.entities.OptionsEntity
import uzb.smt.questionsdemo.data.source.storage.room.entities.QuestionEntity

@Database(entities = [QuestionEntity::class, OptionsEntity::class], version = 1)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}