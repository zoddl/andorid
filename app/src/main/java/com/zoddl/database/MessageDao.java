package com.zoddl.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zoddl.fcmService.Message;

import java.util.List;

/**
 * Created by avanish on 11/6/18.
 */
@Dao
public interface MessageDao {

    @Query("SELECT * FROM Message")
    List<Message> getAll();

    @Insert
    void insertAll(Message... tags);

    @Delete
    void delete(Message tags);

    @Query("DELETE FROM Message")
    void deleteAll();
}
