package gr.logistic_i.logistic_i;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Creds.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}