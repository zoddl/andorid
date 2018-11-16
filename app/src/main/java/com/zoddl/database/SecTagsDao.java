package com.zoddl.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zoddl.model.report.ReportSecondaryTag;

import java.util.List;

/**
 * Created by vipin on 22/6/18.
 */
@Dao
public interface SecTagsDao {

    @Query("SELECT * FROM reportsecondarytag")
    List<ReportSecondaryTag> getAll();

    @Insert
    void insertAll(ReportSecondaryTag... tags);

    @Delete
    void delete(ReportSecondaryTag tags);

    @Query("DELETE FROM reportsecondarytag")
    void deleteAll();
}
