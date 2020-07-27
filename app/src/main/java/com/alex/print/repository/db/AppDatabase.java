package com.alex.print.repository.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alex.print.repository.AppExecutors;
import com.alex.print.repository.db.dao.ProductDao;
import com.alex.print.repository.db.entity.Product;

@Database(entities = {Product.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    @VisibleForTesting
    private static final String DATABASE_NAME = "print_zpl_db";
    private static AppDatabase INSTANCE;

    public abstract ProductDao productDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(roomDatabaseCallback)
                .build();
    }

    /**
     * Override the onCreate method to populate the database when the database is created for the 1st time
     */
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Populate the database in the background.
            AppExecutors.getInstance().diskIO().execute(() -> {
                INSTANCE.productDao().insert(DataGenerator.generateNewProductList());
            });
        }
    };
}
