package com.example.medibox;

import com.google.firebase.database.FirebaseDatabase;

public class persistenciaDatos extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
