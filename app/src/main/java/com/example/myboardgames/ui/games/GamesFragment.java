package com.example.myboardgames.ui.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.SortType;
import com.example.myboardgames.adapters.ChangeStateEvent;
import com.example.myboardgames.adapters.ExpListViewAdapterWithCheckbox;
import com.example.myboardgames.adapters.GameAdapter;
import com.example.myboardgames.ui.GameInfoActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GamesFragment extends Fragment { // implements AdapterInterface {

    private AppCompatActivity mActivity;

    private View view;
    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private DrawerLayout drawerLayout;
    private Button filterButton;
    private Button recommendationButton;
    private Spinner sortSpinner;
    private ArrayAdapter sortAdapter;
    private ExpandableListView filterListView;
    private SearchView search;
    private ExpListViewAdapterWithCheckbox checkboxFilterAdapter;

    private List<Game> games;
    private List<Game> filteredGames;
    public static final int GAME_INFO_REQUEST = 2;
    private static final String SAVED_INSTANCE_SORT_KEY = "sortPosition";

    //In previous project it was used for databaseReference
    //private ValueEventListener valueEventListener;

    /**
     * method is used to initialise fragment on attach
     * @param context
     */
    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }*/

    /**
     * method is used to initialise fragment when it is started
     */
    /*@Override
    public void onStart() {
        super.onStart();
        initRecyclerView();
    }*/

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROBLEM_INFO_REQUEST && resultCode == PROBLEM_INFO_REQUEST) {
            NavHostFragment.findNavController(ListProblemsFragment.this)
                    .navigate(R.id.action_ListProblemsFragment_to_MapsFragment);
        }
    }
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.filter_drawer, container, false);
        ViewStub stub = view.findViewById(R.id.filter_content);
        stub.setLayoutResource(R.layout.fragment_games);
        stub.inflate();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initDrawer();
        initFilter();
        initRecyclerView();
        //initRecyclerView();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_SORT_KEY, sortSpinner.getSelectedItemPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            sortSpinner.setSelection(savedInstanceState.getInt(SAVED_INSTANCE_SORT_KEY, 0));
        }
    }

    /**
     * method is used to hide keyboard
     */
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void init() {
        drawerLayout = view.findViewById(R.id.filter_drawer_layout);
        filterListView = view.findViewById(R.id.filter_list_view);
        filterButton = view.findViewById(R.id.filter_button);
        recommendationButton = view.findViewById(R.id.recommendation_button);
        sortSpinner = view.findViewById(R.id.sort);
        sortAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout_sort_closed, getResources().getStringArray(R.array.sort_items));
        search = view.findViewById(R.id.search);
    }

    private void initDrawer() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        sortAdapter.setDropDownViewResource(R.layout.spinner_layout_sort_open);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortWithCurrentSorter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sortSpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.search(newText);
                recyclerView.smoothScrollToPosition(0);
                return false;
            }
        });
    }

    private void sortWithCurrentSorter() {
        int position = sortSpinner.getSelectedItemPosition();
        SortType type;
        if (position == 0) {
            type = SortType.NAME_ASCENDING;
        } else if (position == 1) {
            type = SortType.NAME_DESCENDING;
        } else if (position == 2) {
            type = SortType.DATE_ASCENDING;
        } else if (position == 3) {
            type = SortType.DATE_DESCENDING;
        } else if (position == 4) {
            type = SortType.POINTS_ASCENDING;
        } else {
            type = SortType.POINTS_DESCENDING;
        }
        GamesProcessor.sortGames(filteredGames, type);
    }

    private void initFilter() {
        String[] categories = {"funny", "other"};
        String[] points = getResources().getStringArray(R.array.points);
        String[] favorites = getResources().getStringArray(R.array.favorite);

        Map<String, List<String>> childData = new HashMap<>();

        String categoryLabel = getResources().getString(R.string.categories_filter_label);
        String pointsLabel = getResources().getString(R.string.points_filter_label);
        String favoriteLabel = getResources().getString(R.string.favorite_filter_label);

        //childData.put(categoryLabel, GamesProcessor.getCategories());
        childData.put(categoryLabel, Arrays.asList(categories));
        childData.put(pointsLabel, Arrays.asList(points));
        childData.put(favoriteLabel, Arrays.asList(favorites));

        checkboxFilterAdapter = new ExpListViewAdapterWithCheckbox(
                getActivity(),
                Arrays.asList(categoryLabel, pointsLabel, favoriteLabel),
                childData);

        checkboxFilterAdapter.setOnChangeStateListener(new ChangeStateEvent() {
            @Override
            public void callback(Map<String, Set<String>> checkedItems) {
                if (checkedItems.isEmpty()) {
                    showAll();
                } else {
                    filterGames(checkedItems);
                    sortWithCurrentSorter();
                    adapter.notifyDataSetChanged();
                }
                search.setQuery("", false);
            }
        });
        filterListView.setAdapter(checkboxFilterAdapter);
    }

    private void filterGames(Map<String, Set<String>> checkedItems) {
        String categoryLabel = getResources().getString(R.string.categories_filter_label);
        String pointsLabel = getResources().getString(R.string.points_filter_label);
        String favoriteLabel = getResources().getString(R.string.favorite_filter_label);

        filteredGames.clear();
        //Toast.makeText(getActivity(), "games size: " + games.size(), Toast.LENGTH_LONG).show();

        //Categories check
        Set<String> categoriesChecked = checkedItems.remove(categoryLabel);
        if (categoriesChecked != null) {
            //Toast.makeText(getActivity(), "categoriesChecked", Toast.LENGTH_LONG).show();
            for (Game game : games) {
                if (!Collections.disjoint(categoriesChecked, game.getCategories()))
                    filteredGames.add(game);
            }
        } else {
            filteredGames.addAll(games);
        }

        //Points check
        Set<String> pointsChecked = checkedItems.remove(pointsLabel);
        if (pointsChecked != null) {
            //Toast.makeText(getActivity(), "pointsChecked", Toast.LENGTH_LONG).show();
            List<Integer> convertedLevels = pointsChecked
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            for (int i = 0; i < filteredGames.size(); ++i) {
                if (!convertedLevels.contains((Integer) filteredGames.get(i).getQuantOfPoints())) {
                    filteredGames.remove(i);
                    --i;
                }
            }
        }

        //Favorite check
        Set<String> favoriteChecked = checkedItems.remove(favoriteLabel);
        if (favoriteChecked != null) {
            //Toast.makeText(getActivity(), "favoriteChecked", Toast.LENGTH_LONG).show();
            String[] favorites = getResources().getStringArray(R.array.favorite);
            for (int i = 0; i < filteredGames.size(); ++i) {
                if (!favoriteChecked.contains(favorites[filteredGames.get(i).isFavorite() ? 0 : 1])) {
                    filteredGames.remove(i);
                    //Toast.makeText(getActivity(), "favoriteChecked: " + favoriteChecked + "; favorites[" + (filteredGames.get(i).isFavorite() ? 0 : 1) + "] = " + favorites[filteredGames.get(i).isFavorite() ? 0 : 1], Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), filteredGames.get(i).getName() + " is removing. FilteredGames size: " + filteredGames.size(), Toast.LENGTH_LONG).show();
                    --i;
                }
            }
        }
        Toast.makeText(getActivity(), "filteredGames size: " + filteredGames.size(), Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }

    private void showAll() {
        filteredGames.clear();
        filteredGames.addAll(games);
        sortWithCurrentSorter();
        adapter.notifyDataSetChanged();
    }


    /**
     * method is used to initialise recycler view
     */
    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerViewGames);
        if (!GamesProcessor.gamesAreLoaded())
            GamesProcessor.loadGames(getActivity());
        games = GamesProcessor.getGames();
        filteredGames = GamesProcessor.getCopyOfGames();
        adapter = new GameAdapter(getContext(), filteredGames, new GameAdapter.OnGameClickListener() {
            @Override
            public void onGameClicked(Game game, int position) {
                Intent intent = new Intent(GamesFragment.this.getContext(), GameInfoActivity.class);
                intent.putExtra("Game", game);
                //intent.putExtra("GamePosition", position);
                //intent.putExtra("Adapter", (Parcelable) adapter);
                //startActivityForResult(intent, GAME_INFO_REQUEST);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /*@Override
    public void notifyGameChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyGameRemoved(int position) {
        //adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(position);
    }*/
}