package com.example.chattingapp.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface DatabaseManager {
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure(DatabaseError databaseError);
}
