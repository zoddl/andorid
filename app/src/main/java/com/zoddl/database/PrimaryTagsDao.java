package com.zoddl.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zoddl.model.home.HomePrimaryTagPayload;

import java.util.List;

/**
 * Created by avanish on 22/6/18.
 */
@Dao
public interface PrimaryTagsDao {
    @Query("SELECT * FROM homeprimarytagpayload")
    List<HomePrimaryTagPayload> getAll();

    @Insert
    void insert(HomePrimaryTagPayload... tags);

    @Delete
    void delete(HomePrimaryTagPayload tags);

    @Query("DELETE FROM homeprimarytagpayload")
    void deleteAll();
}
