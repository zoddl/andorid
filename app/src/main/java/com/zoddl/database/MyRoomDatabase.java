package com.zoddl.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.zoddl.fcmService.Message;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.model.report.ReportSecondaryTag;

/**
 * Created by avanish.
 */

@Database(entities = { SycnTags.class , Message.class,HomePrimaryTagPayload.class,
                ReportSecondaryTag.class},version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {

    public abstract SycnTagsDao getDao();
    public abstract MessageDao getMessageDao();
    public abstract PrimaryTagsDao getPrimaryTagsDao();
    public abstract SecTagsDao getSecTagsDao();

    //public abstract SecTagsDao getSecTagsDao();

   /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users "
                    + "ADD COLUMN address TEXT");

        }
    };*/

}
