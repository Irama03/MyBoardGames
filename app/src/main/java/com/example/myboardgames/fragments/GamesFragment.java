package com.example.myboardgames.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.helpers.ButtonsActions;
import com.example.myboardgames.helpers.SpinnerName;
import com.example.myboardgames.models.Game;
import com.example.myboardgames.helpers.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.helpers.SortType;
import com.example.myboardgames.helpers.Utils;
import com.example.myboardgames.adapters.ChangeStateEvent;
import com.example.myboardgames.adapters.ExpListViewAdapterWithCheckbox;
import com.example.myboardgames.adapters.GameAdapter;
import com.example.myboardgames.activities.GameInfoActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GamesFragment extends Fragment {

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
    private Spinner smallestAgeSpF, biggestAgeSpF, smallestQuantOfPlayersSpF;
    private Spinner biggestQuantOfPlayersSpF, playingTimeSpF;
    private Map<String, Set<String>> checkedFilterItems;
    private Map<SpinnerName, String> selectedFilterItems;
    private boolean selectionIsInitial;

    private List<Game> games;
    private List<Game> filteredGames;
    private static final String SAVED_INSTANCE_SORT_KEY = "sortPosition";
    private boolean filterChecked = true;
    private static final int QUANT_OF_RECOMMENDATIONS = 6;

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

    @Override
    public void onResume() {
        super.onResume();
        for (Game g: filteredGames) {
            if (!games.contains(g)) {
                filteredGames.remove(g);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void init() {
        drawerLayout = view.findViewById(R.id.filter_drawer_layout);
        filterListView = view.findViewById(R.id.filter_list_view);
        smallestAgeSpF = (Spinner)(view.findViewById(R.id.smallestAgeSpF));
        initSpinner(R.array.years, smallestAgeSpF, false);
        biggestAgeSpF = (Spinner)(view.findViewById(R.id.biggestAgeSpF));
        initSpinner(R.array.years, biggestAgeSpF, true);
        smallestQuantOfPlayersSpF = (Spinner)(view.findViewById(R.id.smallestQuantOfPlayersSpF));
        initSpinner(R.array.quantOfPlayers, smallestQuantOfPlayersSpF, false);
        biggestQuantOfPlayersSpF = (Spinner)(view.findViewById(R.id.biggestQuantOfPlayersSpF));
        initSpinner(R.array.quantOfPlayers, biggestQuantOfPlayersSpF, true);
        playingTimeSpF = (Spinner)(view.findViewById(R.id.playingTimeSpF));
        initSpinner(R.array.time, playingTimeSpF, false);
        filterButton = view.findViewById(R.id.filter_button);
        recommendationButton = view.findViewById(R.id.recommendation_button);
        sortSpinner = view.findViewById(R.id.sort);
        sortAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout_sort_closed, getResources().getStringArray(R.array.sort_items));
        search = view.findViewById(R.id.search);
    }

    private void initSpinner(@ArrayRes int id, Spinner spinner, boolean setMaxSelection) {
        String[] arr = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.spinnerItem,arr);
        spinner.setAdapter(adapter);
        if (setMaxSelection) spinner.setSelection(arr.length - 1);
    }

    private void initDrawer() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filterChecked) {
                    filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_base)));
                    recommendationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_light)));
                    filterChecked = true;
                    sortSpinner.setEnabled(true);
                    filterOrRecommend(true);
                }
                ButtonsActions.hideKeyboard(getActivity());
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        recommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterChecked) {
                    filterButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_light)));
                    recommendationButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_base)));
                    filterChecked = false;
                    sortSpinner.setEnabled(false);
                    filterOrRecommend(true);
                }
                ButtonsActions.hideKeyboard(getActivity());
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
                ButtonsActions.hideKeyboard(getActivity());
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
            type = SortType.POINTS_DESCENDING;
        } else {
            type = SortType.POINTS_ASCENDING;
        }
        GamesProcessor.sortGames(filteredGames, type);
    }

    private void initFilter() {
        selectionIsInitial = true;
        checkedFilterItems = new HashMap<>();
        selectedFilterItems = new HashMap<>();
        selectedFilterItems.put(SpinnerName.SMALLEST_AGE, (String)smallestAgeSpF.getSelectedItem());
        selectedFilterItems.put(SpinnerName.BIGGEST_AGE, (String)biggestAgeSpF.getSelectedItem());
        selectedFilterItems.put(SpinnerName.SMALLEST_QUANT_OF_PLAYERS, (String)smallestQuantOfPlayersSpF.getSelectedItem());
        selectedFilterItems.put(SpinnerName.BIGGEST_QUANT_OF_PLAYERS, (String)biggestQuantOfPlayersSpF.getSelectedItem());
        selectedFilterItems.put(SpinnerName.PLAYING_TIME, (String)playingTimeSpF.getSelectedItem());

        if (!GamesProcessor.categoriesAreLoaded())
            GamesProcessor.loadCategories(getActivity());
        String[] points = getResources().getStringArray(R.array.points);
        String[] favorites = getResources().getStringArray(R.array.favorite);

        Map<String, List<String>> childData = new HashMap<>();

        String categoryLabel = getResources().getString(R.string.categories_filter_label);
        String pointsLabel = getResources().getString(R.string.points_filter_label);
        String favoriteLabel = getResources().getString(R.string.favorite_filter_label);

        childData.put(categoryLabel, GamesProcessor.getCategories());
        childData.put(pointsLabel, Arrays.asList(points));
        childData.put(favoriteLabel, Arrays.asList(favorites));

        smallestAgeSpF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterItems.put(SpinnerName.SMALLEST_AGE, (String)smallestAgeSpF.getSelectedItem());
                filterOrRecommend(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        biggestAgeSpF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterItems.put(SpinnerName.BIGGEST_AGE, (String)biggestAgeSpF.getSelectedItem());
                filterOrRecommend(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        smallestQuantOfPlayersSpF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterItems.put(SpinnerName.SMALLEST_QUANT_OF_PLAYERS, (String)smallestQuantOfPlayersSpF.getSelectedItem());
                filterOrRecommend(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        biggestQuantOfPlayersSpF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterItems.put(SpinnerName.BIGGEST_QUANT_OF_PLAYERS, (String)biggestQuantOfPlayersSpF.getSelectedItem());
                filterOrRecommend(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        playingTimeSpF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterItems.put(SpinnerName.PLAYING_TIME, (String)playingTimeSpF.getSelectedItem());
                filterOrRecommend(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        checkboxFilterAdapter = new ExpListViewAdapterWithCheckbox(
                getActivity(),
                Arrays.asList(categoryLabel, pointsLabel, favoriteLabel),
                childData);

        checkboxFilterAdapter.setOnChangeStateListener(new ChangeStateEvent() {
            @Override
            public void callback(Map<String, Set<String>> checkedItems) {
                checkedFilterItems = checkedItems;
                filterOrRecommend(true);
            }
        });
        filterListView.setAdapter(checkboxFilterAdapter);
    }

    private void filterOrRecommend(boolean positionCondition) {
        if (checkedFilterItems.isEmpty() && selectionIsInitial && positionCondition) {
            showAll();
        } else {
            if (!positionCondition) selectionIsInitial = false;
            if (filterChecked) {
                filterGames();
                sortWithCurrentSorter();
            }
            else makeRecommendations();
            adapter.notifyDataSetChanged();
        }
        search.setQuery("", false);
    }

    private void makeRecommendations() {
        String categoryLabel = getResources().getString(R.string.categories_filter_label);
        Set<String> categoriesChecked = checkedFilterItems.get(categoryLabel);

        String pointsLabel = getResources().getString(R.string.points_filter_label);
        Set<String> pointsChecked = checkedFilterItems.get(pointsLabel);
        List<Integer> convertedPoints = (pointsChecked != null) ? pointsChecked
                .stream().map(Integer::parseInt).collect(Collectors.toList()) : null;

        String favoriteLabel = getResources().getString(R.string.favorite_filter_label);
        Set<String> favoriteChecked = checkedFilterItems.get(favoriteLabel);
        String[] favorites = getResources().getStringArray(R.array.favorite);

        int smallestAge = Integer.parseInt((String)smallestAgeSpF.getSelectedItem());
        int biggestAge = Integer.parseInt((String)biggestAgeSpF.getSelectedItem());
        int smallestQuantOfPlayers = Integer.parseInt((String)smallestQuantOfPlayersSpF.getSelectedItem());
        int biggestQuantOfPlayers = Integer.parseInt((String)biggestQuantOfPlayersSpF.getSelectedItem());
        int playingTimePos = playingTimeSpF.getSelectedItemPosition();
        String[] plTimeArr = getResources().getStringArray(R.array.time);

        filteredGames.clear();
        games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game g1, Game g2) {
                int score1 = 0;
                int score2 = 0;

                if (categoriesChecked != null) {
                    for (String category : g1.getCategories()) {
                        if (categoriesChecked.contains(category))
                            score1 += 7;
                    }
                    for (String category : g2.getCategories()) {
                        if (categoriesChecked.contains(category))
                            score2 += 7;
                    }
                }

                if (convertedPoints != null) {
                    int points1 = g1.getQuantOfPoints();
                    int points2 = g2.getQuantOfPoints();
                    if (convertedPoints.contains(points1))
                        score1 += 10;
                    else if (convertedPoints.contains(points1 + 1)
                            || convertedPoints.contains(points1 - 1))
                        score1 += 5;
                    if (convertedPoints.contains(points2))
                        score2 += 10;
                    else if (convertedPoints.contains(points2 + 1)
                            || convertedPoints.contains(points2 - 1))
                        score2 += 5;
                }

                if (favoriteChecked != null) {
                    if (favoriteChecked.contains(favorites[g1.isFavorite() ? 0 : 1]))
                        score1 += 15;
                    if (favoriteChecked.contains(favorites[g2.isFavorite() ? 0 : 1]))
                        score2 += 15;
                }

                int smAge1 = g1.getSmallestAge();
                int bgAge1 = g1.getBiggestAge();
                int smAge2 = g2.getSmallestAge();
                int bgAge2 = g2.getBiggestAge();
                if (smAge1 >= smallestAge && bgAge1 <= biggestAge)
                    score1 += 10;
                else if (smAge1 < smallestAge) {
                    if (smAge1 + 1 == smallestAge)
                        score1 += 8;
                    else if (smAge1 + 2 == smallestAge)
                        score1 += 6;
                    else if (smAge1 + 3 == smallestAge)
                        score1 += 4;
                }
                //bgAge1 > biggestAge
                else {
                    if (bgAge1 - 1 == biggestAge || bgAge1 - 2 == biggestAge)
                        score1 += 8;
                    else if (bgAge1 - 3 == biggestAge || bgAge1 - 4 == biggestAge)
                        score1 += 6;
                    else score1 += 4;
                }

                if (smAge2 >= smallestAge && bgAge2 <= biggestAge)
                    score2 += 10;
                else if (smAge2 < smallestAge) {
                    if (smAge2 + 1 == smallestAge)
                        score2 += 8;
                    else if (smAge2 + 2 == smallestAge)
                        score2 += 6;
                    else if (smAge2 + 3 == smallestAge)
                        score2 += 4;
                }
                //bgAge2 > biggestAge
                else {
                    if (bgAge2 - 1 == biggestAge || bgAge2 - 2 == biggestAge)
                        score2 += 8;
                    else if (bgAge2 - 3 == biggestAge || bgAge2 - 4 == biggestAge)
                        score2 += 6;
                    else score2 += 4;
                }

                int smQoP1 = g1.getSmallestQuantOfPlayers();
                int bgQoP1 = g1.getBiggestQuantOfPlayers();
                int smQoP2 = g2.getSmallestQuantOfPlayers();
                int bgQoP2 = g2.getBiggestQuantOfPlayers();
                if (smQoP1 >= smallestQuantOfPlayers && bgQoP1 <= biggestQuantOfPlayers)
                    score1 += 10;
                else if (smQoP1 < smallestQuantOfPlayers) {
                    if (smQoP1 + 1 == smallestQuantOfPlayers)
                        score1 += 5;
                    else if (smQoP1 + 2 == smallestQuantOfPlayers)
                        score1 += 2;
                }
                //bgQoP1 > biggestQuantOfPlayers
                else {
                    if (bgQoP1 - 1 == biggestQuantOfPlayers)
                        score1 += 7;
                    else if (bgQoP1 - 2 == biggestQuantOfPlayers)
                        score1 += 5;
                }

                if (smQoP2 >= smallestQuantOfPlayers && bgQoP2 <= biggestQuantOfPlayers)
                    score2 += 10;
                else if (smQoP2 < smallestQuantOfPlayers) {
                    if (smQoP2 + 1 == smallestQuantOfPlayers)
                        score2 += 5;
                    else if (smQoP2 + 2 == smallestQuantOfPlayers)
                        score2 += 2;
                }
                //bgQoP2 > biggestQuantOfPlayers
                else {
                    if (bgQoP2 - 1 == biggestQuantOfPlayers)
                        score2 += 7;
                    else if (bgQoP2 - 2 == biggestQuantOfPlayers)
                        score2 += 5;
                }

                if (playingTimePos != 0) {
                    int plTimePos1 = Utils.getPositionOfStr(g1.getPlayingTime(), plTimeArr);
                    int plTimePos2 = Utils.getPositionOfStr(g2.getPlayingTime(), plTimeArr);

                    if (plTimePos1 == 0 || plTimePos1 == playingTimePos)
                        score1 += 10;
                    else if (plTimePos1 + 1 == playingTimePos || plTimePos1 - 1 == playingTimePos)
                        score1 += 7;
                    else if (plTimePos1 + 2 == playingTimePos || plTimePos1 - 2 == playingTimePos)
                        score1 += 5;

                    if (plTimePos2 == 0 || plTimePos2 == playingTimePos)
                        score2 += 10;
                    else if (plTimePos2 + 1 == playingTimePos || plTimePos2 - 1 == playingTimePos)
                        score2 += 7;
                    else if (plTimePos2 + 2 == playingTimePos || plTimePos2 - 2 == playingTimePos)
                        score2 += 5;
                }

                score1 += g1.getQuantOfTimesBeingChosen() / 8;
                score2 += g2.getQuantOfTimesBeingChosen() / 8;
                return score2 - score1;
            }
        });
        int quant = Math.min(QUANT_OF_RECOMMENDATIONS, games.size());
        int i = 0;
        for (Game game : games) {
            if (i < quant)  {
                filteredGames.add(game);
                i++;
            }
            else break;
        }
        adapter.notifyDataSetChanged();
    }

    private void filterGames() {
        String categoryLabel = getResources().getString(R.string.categories_filter_label);
        String pointsLabel = getResources().getString(R.string.points_filter_label);
        String favoriteLabel = getResources().getString(R.string.favorite_filter_label);

        filteredGames.clear();

        //Categories check
        Set<String> categoriesChecked = checkedFilterItems.get(categoryLabel);
        if (categoriesChecked != null) {
            for (Game game : games) {
                if (!Collections.disjoint(categoriesChecked, game.getCategories()))
                    filteredGames.add(game);
            }
        } else {
            if (games != null)
                filteredGames.addAll(games);
        }

        //Points check
        Set<String> pointsChecked = checkedFilterItems.get(pointsLabel);
        if (pointsChecked != null) {
            List<Integer> convertedPoints = pointsChecked
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            for (int i = 0; i < filteredGames.size(); ++i) {
                if (!convertedPoints.contains(filteredGames.get(i).getQuantOfPoints())) {
                    filteredGames.remove(i);
                    --i;
                }
            }
        }

        //Favorite check
        Set<String> favoriteChecked = checkedFilterItems.get(favoriteLabel);
        if (favoriteChecked != null) {
            String[] favorites = getResources().getStringArray(R.array.favorite);
            for (int i = 0; i < filteredGames.size(); ++i) {
                if (!favoriteChecked.contains(favorites[filteredGames.get(i).isFavorite() ? 0 : 1])) {
                    filteredGames.remove(i);
                    --i;
                }
            }
        }

        //Selected check
        int smallestAge = Integer.parseInt((String)smallestAgeSpF.getSelectedItem());
        int biggestAge = Integer.parseInt((String)biggestAgeSpF.getSelectedItem());
        int smallestQuantOfPlayers = Integer.parseInt((String)smallestQuantOfPlayersSpF.getSelectedItem());
        int biggestQuantOfPlayers = Integer.parseInt((String)biggestQuantOfPlayersSpF.getSelectedItem());
        String playingTime = (String)playingTimeSpF.getSelectedItem();
        for (int i = 0; i < filteredGames.size(); ++i) {
            Game game = filteredGames.get(i);
            boolean gameSuits = game.getSmallestAge() >= smallestAge && game.getBiggestAge() <= biggestAge &&
                    game.getSmallestQuantOfPlayers() >= smallestQuantOfPlayers &&
                    game.getBiggestQuantOfPlayers() <= biggestQuantOfPlayers
                    && (playingTime.equals(getResources().getStringArray(R.array.time)[0]) || playingTime.equals(game.getPlayingTime()));
            if (!gameSuits) {
                filteredGames.remove(i);
                --i;
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void showAll() {
        filteredGames.clear();
        if (games != null)
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
        sortWithCurrentSorter();
        adapter = new GameAdapter(getContext(), filteredGames, new GameAdapter.OnGameClickListener() {
            @Override
            public void onGameClicked(Game game, int position) {
                Intent intent = new Intent(GamesFragment.this.getContext(), GameInfoActivity.class);
                intent.putExtra("Game", game);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}