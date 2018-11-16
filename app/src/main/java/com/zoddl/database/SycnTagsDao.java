package com.zoddl.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by vipin on 3/5/18.
 */
@Dao
public interface SycnTagsDao {

    @Query("SELECT * FROM sycntags")
    List<SycnTags> getAll();

    @Query("SELECT * FROM sycntags WHERE status IN (:status)")
    List<SycnTags> loadAllByStatus(boolean[] status);


    @Insert
    void insertAll(SycnTags... tags);

    @Delete
    void delete(SycnTags tags);

    @Query("DELETE FROM sycntags")
    void deleteAll();
}
