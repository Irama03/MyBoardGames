package com.example.myboardgames;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstFragment extends Fragment {

    private ArrayAdapter<Game> adapter;
    private EditText nameText, descriptionText, photoPathText, rulesText, placeText;
    private EditText smallestAgeText, biggestAgeText, smallestQuantOfPlayersText;
    private EditText biggestQuantOfPlayersText, categoriesText, quantOfPointsText, isFavoriteText;
    private List<Game> games;
    ListView listView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameText = (EditText)(getActivity().findViewById(R.id.nameText));
        descriptionText = (EditText)(getActivity().findViewById(R.id.descriptionText));
        photoPathText = (EditText)(getActivity().findViewById(R.id.photoPathText));
        rulesText = (EditText)(getActivity().findViewById(R.id.rulesText));
        placeText = (EditText)(getActivity().findViewById(R.id.placeText));
        smallestAgeText = (EditText)(getActivity().findViewById(R.id.smallestAgeText));
        biggestAgeText = (EditText)(getActivity().findViewById(R.id.biggestAgeText));
        smallestQuantOfPlayersText = (EditText)(getActivity().findViewById(R.id.smallestQuantOfPlayersText));
        biggestQuantOfPlayersText = (EditText)(getActivity().findViewById(R.id.biggestQuantOfPlayersText));
        categoriesText = (EditText)(getActivity().findViewById(R.id.categoriesText));
        quantOfPointsText = (EditText)(getActivity().findViewById(R.id.quantOfPointsText));
        isFavoriteText = (EditText)(getActivity().findViewById(R.id.isFavoriteText));

        listView = (ListView)(getActivity().findViewById(R.id.list));
        games = new ArrayList<Game>();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
        listView.setAdapter(adapter);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGame(view);
            }
        });
        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
        view.findViewById(R.id.openButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open(view);
            }
        });
    }

    public void addGame(View view){
        String name = nameText.getText().toString();
        String description = descriptionText.getText().toString();
        String photoPath = photoPathText.getText().toString();
        String rules = rulesText.getText().toString();
        String place = placeText.getText().toString();
        int smallestAge = Integer.parseInt(smallestAgeText.getText().toString());
        int biggestAge = Integer.parseInt(biggestAgeText.getText().toString());
        int smallestQuantOfPlayers = Integer.parseInt(smallestQuantOfPlayersText.getText().toString());
        int biggestQuantOfPlayers = Integer.parseInt(biggestQuantOfPlayersText.getText().toString());
        //ArrayList<String> categories = (ArrayList<String>) (Arrays.asList(categoriesText.getText().toString().split(", ")));
        List<String> categories = Arrays.asList(categoriesText.getText().toString().split(", "));
        int quantOfPoints = Integer.parseInt(quantOfPointsText.getText().toString());
        int quantOfTimesBeingChosen = 0;
        boolean isFavorite = Boolean.parseBoolean(isFavoriteText.getText().toString());

        Game game = new Game(name, description, photoPath, rules, place, smallestAge,
                biggestAge, smallestQuantOfPlayers, biggestQuantOfPlayers, categories,
                quantOfPoints, quantOfTimesBeingChosen, isFavorite);
        games.add(game);
        adapter.notifyDataSetChanged();
    }

    public void save(View view){

        boolean result = JSONHelper.exportToJSON(getActivity(), games);
        if(result){
            Toast.makeText(getActivity(), "Дані збережено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не вдалося зберегти дані", Toast.LENGTH_LONG).show();
        }
    }

    public void open(View view){
        games = JSONHelper.importFromJSON(getActivity());
        if(games !=null){
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
            listView.setAdapter(adapter);
            Toast.makeText(getActivity(), "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }
    }
}