package com.example.myboardgames.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myboardgames.R;
import com.example.myboardgames.models.User;
import com.example.myboardgames.helpers.Utils;
import com.example.myboardgames.database.AppDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("com.example.myboardgames", MODE_PRIVATE);
        appDatabase = new AppDatabase();

        TextView uuid = (TextView)(findViewById(R.id.uuid));
        uuid.setText(prefs.getString("userId", ""));
        registerForContextMenu(uuid);

        EditText username = (EditText)(findViewById(R.id.username));
        username.setText(prefs.getString("username", ""));

        findViewById(R.id.updateBtn).setOnClickListener(view -> {
            String u = String.valueOf(username.getText()).trim();
            if (Utils.validateName(u) && !u.equals(prefs.getString("username", ""))) {
                if (!Utils.isNetworkAvailable(ProfileActivity.this)) {
                    Toast.makeText(ProfileActivity.this, "Неможливо змінити псевдонім. Мережа Інтернет відсутня", Toast.LENGTH_LONG).show();
                }
                else {
                    appDatabase.usernameExists(u).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                                Toast.makeText(ProfileActivity.this, "Такий псевдонім вже існує!", Toast.LENGTH_LONG).show();
                            else {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("username", u);
                                appDatabase.updateObjectInDatabase(new User(prefs.getString("userId", ""), prefs.getString("username", "")), updates);
                                prefs.edit().putString("username", u).apply();
                                Toast.makeText(ProfileActivity.this, "Псевдонім збережено", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.uuid) {
            menu.add(0, v.getId(), 0, "Скопіювати");
            TextView textView = (TextView) v;
            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", textView.getText());
            if (manager != null) {
                manager.setPrimaryClip(clipData);
            }
        }
    }
}
