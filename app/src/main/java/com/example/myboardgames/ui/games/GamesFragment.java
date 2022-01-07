package com.example.myboardgames.ui.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.adapters.GameAdapter;
import com.example.myboardgames.ui.GameInfoActivity;

import java.io.Serializable;
import java.util.List;

public class GamesFragment extends Fragment implements AdapterInterface {

    private AppCompatActivity mActivity;

    private View view;
    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private List<Game> games;
    private List<Game> filteredGames;
    public static final int GAME_INFO_REQUEST = 2;

    //In previous project it was used for databaseReference
    //private ValueEventListener valueEventListener;

    /**
     * method is used to initialise fragment on attach
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    /**
     * method is used to initialise fragment when it is started
     */
    @Override
    public void onStart() {
        super.onStart();
        initRecyclerView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initRecyclerView();
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
                intent.putExtra("GamePosition", position);
                //intent.putExtra("Adapter", (Parcelable) adapter);
                //startActivityForResult(intent, GAME_INFO_REQUEST);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyGameChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyGameRemoved(int position) {
        //adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
    }
}