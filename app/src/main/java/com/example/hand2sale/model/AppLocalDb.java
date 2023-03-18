package com.example.hand2sale.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hand2sale.MyApplication;

@Database(entities={Post.class,User.class},version=5)
abstract class AppLocalDbRepository extends RoomDatabase{
    public abstract PostDao postDao();
    public abstract UserDao userDao();
}
public class AppLocalDb {
    static public AppLocalDbRepository getAppDb(){
        return Room.databaseBuilder(
                MyApplication.getMyContext(),
                AppLocalDbRepository.class,
                "Hand2Sale.db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}
