package com.example.myboardgames.ui.addGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.JSONHelper;
import com.example.myboardgames.R;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddGameFragment extends Fragment {

    private AddGameViewModel addGameViewModel;

    private EditText nameText, descriptionText, photoPathText, rulesText, placeText;
    private EditText smallestAgeText, biggestAgeText, smallestQuantOfPlayersText;
    private EditText biggestQuantOfPlayersText, categoriesText, quantOfPointsText;
    private ImageView imageView;
    public static final int PICK_IMAGE = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addGameViewModel =
                new ViewModelProvider(this).get(AddGameViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_game, container, false);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        addGameViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameText = (EditText)(view.findViewById(R.id.nameText));
        descriptionText = (EditText)(view.findViewById(R.id.descriptionText));
        photoPathText = (EditText)(view.findViewById(R.id.photoPathText));
        rulesText = (EditText)(view.findViewById(R.id.rulesText));
        placeText = (EditText)(view.findViewById(R.id.placeText));
        smallestAgeText = (EditText)(view.findViewById(R.id.smallestAgeText));
        biggestAgeText = (EditText)(view.findViewById(R.id.biggestAgeText));
        smallestQuantOfPlayersText = (EditText)(view.findViewById(R.id.smallestQuantOfPlayersText));
        biggestQuantOfPlayersText = (EditText)(view.findViewById(R.id.biggestQuantOfPlayersText));
        categoriesText = (EditText)(view.findViewById(R.id.categoriesText));
        quantOfPointsText = (EditText)(view.findViewById(R.id.quantOfPointsText));

        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGame(view);
                save(view);
            }
        });
        /*view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });*/

        //Связываемся с нашим ImageView:
        imageView = (ImageView)(view.findViewById(R.id.imageViewI));

        //Связываемся с нашей кнопкой Button:
        //Button pickImage = (Button)(view.findViewById(R.id.pickImageButton));
        //Настраиваем для нее обработчик нажатий OnClickListener:
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                //view.getContext().startActivity(photoPickerIntent);
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
        //boolean isFavorite = Boolean.parseBoolean(isFavoriteText.getText().toString());
        boolean isFavorite = quantOfPoints == 5;

        Game game = new Game(name, description, photoPath, rules, place, smallestAge,
                biggestAge, smallestQuantOfPlayers, biggestQuantOfPlayers, categories,
                quantOfPoints, quantOfTimesBeingChosen, isFavorite, new Date(), null);
        GamesProcessor.getGames().add(game);
        Toast.makeText(getActivity(), "Quant of games: " + GamesProcessor.getGames().size(), Toast.LENGTH_LONG).show();
        //??????
        //((MainActivity)getActivity()).adapter.notifyDataSetChanged();
    }

    public void save(View view){

        boolean result = JSONHelper.exportToJSON(getActivity(), GamesProcessor.getGames());
        if(result){
            Toast.makeText(getActivity(), "Дані збережено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не вдалося зберегти дані", Toast.LENGTH_LONG).show();
        }
    }

    public void open(View view){

        /*games = JSONHelper.importFromJSON(getActivity());
        if(games !=null){
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, games);
            listView.setAdapter(adapter);
            Toast.makeText(getActivity(), "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }*/
    }

}