package com.example.myboardgames.ui.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.adapters.GameAdapter;
import com.example.myboardgames.ui.GameInfoActivity;

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

public class GamesFragment extends Fragment {

    private GamesViewModel gamesViewModel;

    private View view;
    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private List<Game> games;
    private List<Game> filteredGames;
    private ListView listView;
    private static final int GAME_INFO_REQUEST = 2;
    //public ArrayAdapter<Game> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gamesViewModel =
                new ViewModelProvider(this).get(GamesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_games, container, false);
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
        initRecyclerView();

        /*listView = (ListView)(view.findViewById(R.id.recyclerViewGames));
        if (!GamesProcessor.gamesAreLoaded())
            GamesProcessor.loadGames(getActivity());
        games = GamesProcessor.getGames();

        ((MainActivity)getActivity()).adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
        listView.setAdapter(((MainActivity)getActivity()).adapter);)*/
    }

    /**
     * method is used to initialise recycler view
     */
    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerViewGames);
        if (!GamesProcessor.gamesAreLoaded())
            GamesProcessor.loadGames(getActivity());
        games = GamesProcessor.getGames();
        filteredGames = games;
        adapter = new GameAdapter(getContext(), filteredGames, new GameAdapter.OnGameClickListener() {
            @Override
            public void onGameClicked(Game game, int position) {
                Intent intent = new Intent(GamesFragment.this.getContext(), GameInfoActivity.class);
                intent.putExtra("Game", game);
                startActivityForResult(intent, GAME_INFO_REQUEST);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}