package com.example.coursetext

import android.app.Application
import androidx.room.Room
import com.example.coursetext.db.HistoryDao
import com.example.coursetext.db.HistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDb(
        application: Application
    ): HistoryDatabase{
        return Room.databaseBuilder(
            context = application,
            klass = HistoryDatabase::class.java,
            name = "history_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDao(db: HistoryDatabase): HistoryDao{
        return db.itemDao()
    }

}