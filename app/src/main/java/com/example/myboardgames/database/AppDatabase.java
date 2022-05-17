package com.example.myboardgames.database;

import com.example.myboardgames.models.SharedGame;
import com.example.myboardgames.models.UniqueObject;
import com.example.myboardgames.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

public class AppDatabase {

    public static final String USERS_GROUP_KEY = User.class.getSimpleName();
    public static final String SHARED_GAMES_GROUP_KEY = SharedGame.class.getSimpleName();

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public AppDatabase(){
        database = FirebaseDatabase.getInstance();
    }

    public Task<Void> addSharedGameToDatabase(SharedGame sharedGame){
        if (sharedGame == null)
            throw new NullPointerException("Object must not be null");
        databaseReference = database.getReference(SHARED_GAMES_GROUP_KEY);
        return databaseReference.child(sharedGame.getId()).setValue(sharedGame);
    }

    public Task<Void> addUserToDatabase(User user){
        if (user == null)
            throw new NullPointerException("Object must not be null");
        databaseReference = database.getReference(USERS_GROUP_KEY);
        return databaseReference.child(user.getId()).setValue(user);
    }

    public Task<Void> updateObjectInDatabase(UniqueObject objectToUpdate, Map<String, Object> updates){
        if (objectToUpdate == null || updates == null)
            throw new NullPointerException("Object or updates must not be null");
        databaseReference = database.getReference(objectToUpdate.whichGroup());
        return databaseReference.child(objectToUpdate.getId()).updateChildren(updates);
    }

    public Query usernameExists(String username) {
        databaseReference = database.getReference(USERS_GROUP_KEY);
        return databaseReference.orderByChild("username").equalTo(username);
    }

    public Query userIdExists(String id) {
        databaseReference = database.getReference(USERS_GROUP_KEY);
        return databaseReference.orderByChild("id").equalTo(id);
    }

    public DatabaseReference getReferenceToGroup(String group){
        return database.getReference(group);
    }

    public FirebaseDatabase getDatabase(){
        return database;
    }

}
