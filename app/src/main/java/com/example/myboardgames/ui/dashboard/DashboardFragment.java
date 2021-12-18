package com.example.myboardgames.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.JSONHelper;
import com.example.myboardgames.MainActivity;
import com.example.myboardgames.R;
import com.example.myboardgames.ui.notifications.NotificationsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//example of game
/*
games = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        categories.add("Interesting");
        Game game = new Game("A", "A", "A", "A", "A", 1,
                3, 2, 4, categories,
                2, 1, false);
        games.add(game);
* */

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private List<Game> games;
    private ListView listView;
    //public ArrayAdapter<Game> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //((MainActivity)getActivity()).open(listView);

        //games = new ArrayList<Game>();
        //games = ((MainActivity)getActivity()).games;
        //listView.setAdapter(((MainActivity)getActivity()).adapter);

        listView = (ListView)(view.findViewById(R.id.list));
        if (!GamesProcessor.gamesAreLoaded())
            GamesProcessor.loadGames(getActivity());
        games = GamesProcessor.getGames();

        //adapter = ((MainActivity)getActivity()).adapter;
        ((MainActivity)getActivity()).adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
        /*if (games == null)
            Toast.makeText(getActivity(), "games is null", Toast.LENGTH_LONG).show();
        else if (listView == null)
            Toast.makeText(getActivity(), "listview is null", Toast.LENGTH_LONG).show();
        //Null!!!?
        else if (((MainActivity)getActivity()).adapter == null)
            Toast.makeText(getActivity(), "adapter is null", Toast.LENGTH_LONG).show();
        else{*/
            listView.setAdapter(((MainActivity)getActivity()).adapter);
        //}
    }
}