package com.example.hand2sale.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hand2sale.MyApplication;

@Database(entities={Post.class},version=1)
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract PostDao postDao();
}
public class AppLocalDb {
    static public AppLocalDbRepository getAppDb(){
        return Room.databaseBuilder(
                MyApplication.getMyContext(),
                AppLocalDbRepository.class,
                "Hand2Sale.db"
        )
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}
