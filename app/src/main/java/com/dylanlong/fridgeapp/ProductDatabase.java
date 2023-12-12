package com.dylanlong.fridgeapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Database class using Room Persistence Library to provide an abstraction layer
 * over the SQLite database for managing product and fridge item data.
 *
 * The class is annotated with Room's Database annotation, specifying the entities
 * (Product and FridgeItem) and the database version.
 */
@Database(entities = {Product.class, FridgeItem.class}, version = 7)
//@TypeConverters(DateTypeConverter.class)
public abstract class ProductDatabase extends RoomDatabase {
    private static ProductDatabase instance;

    /**
     * Retrieves the DAO (Data Access Object) for managing product data.
     *
     * @return The ProductDAO instance.
     */
    public abstract ProductDAO productDAO();

    /**
     * Retrieves the DAO for managing fridge item data.
     *
     * @return The FridgeDAO instance.
     */
    public abstract FridgeDAO fridgeDAO();

    /**
     * Retrieves a singleton instance of the ProductDatabase.
     *
     * @param context The application context.
     * @return The singleton instance of the ProductDatabase.
     */
    public static synchronized ProductDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDatabase.class, "fridge_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /**
     * Callback for database creation, which populates the initial data asynchronously.
     */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    /**
     * AsyncTask to populate the database with initial data.
     */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        PopulateDbAsyncTask(ProductDatabase instance) {
            ProductDAO productDAO = instance.productDAO();
            FridgeDAO fridgeDAO = instance.fridgeDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform database population logic here
            return null;
        }
    }
}
