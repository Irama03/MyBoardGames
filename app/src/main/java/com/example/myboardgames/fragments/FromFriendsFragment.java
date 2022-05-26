package com.example.myboardgames.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.R;
import com.example.myboardgames.adapters.SharedGameAdapter;
import com.example.myboardgames.database.AppDatabase;
import com.example.myboardgames.helpers.Utils;
import com.example.myboardgames.models.SharedGame;
import com.example.myboardgames.activities.SharedGameInfoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FromFriendsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SharedGameAdapter adapter;

    private List<SharedGame> games;
    private AppDatabase appDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    SharedPreferences prefs = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_from_friends, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = getActivity().getSharedPreferences("com.example.myboardgames", Context.MODE_PRIVATE);
        if (!Utils.isNetworkAvailable(getContext()))
            Toast.makeText(getContext(), "Неможливо завантажити рекомендовані ігри. Мережа Інтернет відсутня", Toast.LENGTH_LONG).show();
        initRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }

    /**
     * method is used to initialise recycler view
     */
    private void initRecyclerView() {
        appDatabase = new AppDatabase();
        recyclerView = view.findViewById(R.id.recyclerViewFromFriends);
        games = new ArrayList<>();
        adapter = new SharedGameAdapter(getContext(), games, new SharedGameAdapter.OnSharedGameClickListener() {
            @Override
            public void onSharedGameClicked(SharedGame game, int position) {
                Intent intent = new Intent(FromFriendsFragment.this.getContext(), SharedGameInfoActivity.class);
                intent.putExtra("SharedGame", game);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        databaseReference = appDatabase.getReferenceToGroup(AppDatabase.SHARED_GAMES_GROUP_KEY);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (games.size() > 0) {
                    games.clear();
                    adapter.notifyDataSetChanged();
                }
                String userId = prefs.getString("userId", "");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SharedGame sharedGame = dataSnapshot.getValue(SharedGame.class);
                    assert sharedGame != null;
                    if (sharedGame.getReceiverId().equals(userId)) {
                        games.add(sharedGame);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

}