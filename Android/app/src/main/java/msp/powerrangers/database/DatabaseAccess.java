package msp.powerrangers.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * This class provides an interface to the Firebase real time database
 * A listener let the application receive data in realtime
 */

public class DatabaseAccess {

    private DatabaseReference database;

    public DatabaseAccess() {
        database = FirebaseDatabase.getInstance().getReference();

    }


    //https://firebase.google.com/docs/database/android/read-and-write

}
