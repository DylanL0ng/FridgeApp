package com.dylanlong.fridgeapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class, FridgeItem.class}, version = 3)
@TypeConverters(DateTypeConverter.class)
public abstract class ProductDatabase extends RoomDatabase {
    private static ProductDatabase instance;
    public abstract ProductDAO productDAO();
    public abstract FridgeDAO fridgeDAO();

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

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        PopulateDbAsyncTask(ProductDatabase instance) {
            ProductDAO productDAO = instance.productDAO();
            FridgeDAO fridgeDAO = instance.fridgeDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
