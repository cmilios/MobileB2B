package gr.logistic_i.logistic_i;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleCreds (Creds creds);
    @Insert
    void insertMultipleCreds (List<Creds> credsList);
    @Query("SELECT * FROM Creds WHERE name = :name")
    Creds fetchOneCredTokenbyName (String name);
    @Update
    void updateCreds (Creds creds);
    @Delete
    void deleteCreds (Creds creds);
    @Query("SELECT * FROM Creds")
    List<Creds> loadAllUsers();
}

