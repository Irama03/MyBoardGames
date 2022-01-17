package com.example.myboardgames.ui.addGame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myboardgames.ButtonsActions;
import com.example.myboardgames.Game;
import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddGameFragment extends Fragment {

    private EditText nameText, descriptionText, photoPathText, rulesText, placeText;
    private Spinner smallestAgeSp, biggestAgeSp, smallestQuantOfPlayersSp;
    private Spinner biggestQuantOfPlayersSp, playingTimeSp;
    private ImageView imageView;
    private ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5;
    private ImageButton favoriteButton, addCategoryButton;
    private ImageButton[] stars = new ImageButton[5];
    private TextView categoriesText;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoriesList;
    private String[] categories;
    public static final int PICK_IMAGE = 1;
    private boolean isFavorite = false;
    private int quantOfPoints = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        //smallestAgeText = (EditText)(view.findViewById(R.id.smallestAgeText));
        smallestAgeSp = (Spinner)(view.findViewById(R.id.smallestAgeSp));
        initSpinner(R.array.years, smallestAgeSp, false);
        biggestAgeSp = (Spinner)(view.findViewById(R.id.biggestAgeSp));
        initSpinner(R.array.years, biggestAgeSp, true);
        smallestQuantOfPlayersSp = (Spinner)(view.findViewById(R.id.smallestQuantOfPlayersSp));
        initSpinner(R.array.quantOfPlayers, smallestQuantOfPlayersSp, false);
        biggestQuantOfPlayersSp = (Spinner)(view.findViewById(R.id.biggestQuantOfPlayersSp));
        initSpinner(R.array.quantOfPlayers, biggestQuantOfPlayersSp, true);
        playingTimeSp = (Spinner)(view.findViewById(R.id.playingTimeSp));
        initSpinner(R.array.time, playingTimeSp, false);
        categoriesText = (TextView)(view.findViewById(R.id.categoriesText));
        //ButtonsActions.initMultiSpinner(categoriesList, categories, selectedCategories);
        initMultiSpinner();
        ButtonsActions.setCategoriesListener(categoriesText, getActivity(), categoriesList, categories, selectedCategories);

        ibStar1 = (ImageButton)(view.findViewById(R.id.ibStar1));
        ibStar2 = (ImageButton)(view.findViewById(R.id.ibStar2));
        ibStar3 = (ImageButton)(view.findViewById(R.id.ibStar3));
        ibStar4 = (ImageButton)(view.findViewById(R.id.ibStar4));
        ibStar5 = (ImageButton)(view.findViewById(R.id.ibStar5));
        stars[0] = ibStar1;
        stars[1] = ibStar2;
        stars[2] = ibStar3;
        stars[3] = ibStar4;
        stars[4] = ibStar5;

        for (int i = 0; i < stars.length; i++) {
            int finalI = i;
            //Why fignya???
            if (quantOfPoints > i) ButtonsActions.addStar(stars[i]); else ButtonsActions.removeStar(stars[i]);
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO Перевірити в якомусь додатку!
                    if (quantOfPoints <= finalI) {
                        ButtonsActions.processSmallerQuantOfStars(stars, finalI);
                        quantOfPoints = finalI + 1;
                    }
                    else if (quantOfPoints == finalI + 1) {
                        ButtonsActions.processTheSameQuantOfStars(stars);
                        quantOfPoints = 0;
                    }
                    else {
                        ButtonsActions.processBiggerQuantOfStars(stars, finalI);
                        quantOfPoints = finalI + 1;
                    }
                }
            });
        }

        addCategoryButton = (ImageButton)(view.findViewById(R.id.addCategoryButton));
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        favoriteButton = (ImageButton)(view.findViewById(R.id.favoriteButton));
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) {
                    ButtonsActions.makeNotFavorite(favoriteButton);
                    isFavorite = false;
                }
                else {
                    ButtonsActions.makeFavorite(favoriteButton);
                    isFavorite = true;
                }
            }
        });

        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGame();
                if (GamesProcessor.saveGames(getContext())) clearFields();
            }
        });
        /*view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });*/

        //Связываемся с нашим ImageView:
        imageView = (ImageView)(view.findViewById(R.id.imageView));

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

    private void initSpinner(@ArrayRes int id, Spinner spinner, boolean setMaxSelection) {
        String[] arr = getResources().getStringArray(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.spinnerItem,arr);
        spinner.setAdapter(adapter);
        if (setMaxSelection) spinner.setSelection(arr.length - 1);
    }

    /**
     * method is used to initialise multispinner
     */
    private void initMultiSpinner() {
        categoriesList = new ArrayList<>();
        List<String> listCategories = GamesProcessor.getCategories();
        categories = listCategories.toArray(new String[0]);
        selectedCategories = new boolean[categories.length];
    }

    private void addGame(){
        String name = nameText.getText().toString();
        String description = descriptionText.getText().toString();
        String photoPath = photoPathText.getText().toString();
        String rules = rulesText.getText().toString();
        String place = placeText.getText().toString();
        //int smallestAge = Integer.parseInt(smallestAgeText.getText().toString());
        int smallestAge = Integer.parseInt((String)smallestAgeSp.getSelectedItem());
        int biggestAge = Integer.parseInt((String)biggestAgeSp.getSelectedItem());
        int smallestQuantOfPlayers = Integer.parseInt((String)smallestQuantOfPlayersSp.getSelectedItem());
        int biggestQuantOfPlayers = Integer.parseInt((String)biggestQuantOfPlayersSp.getSelectedItem());
        String playingTime = (String)playingTimeSp.getSelectedItem();
        //ArrayList<String> categories = (ArrayList<String>) (Arrays.asList(categoriesText.getText().toString().split(", ")));
        List<String> categories = Arrays.asList(categoriesText.getText().toString().split("\\s*,\\s*"));
        //int quantOfPoints = Integer.parseInt(quantOfPointsText.getText().toString());
        int quantOfTimesBeingChosen = 0;
        //boolean isFavorite = Boolean.parseBoolean(isFavoriteText.getText().toString());

        Game game = new Game(name, description, photoPath, rules, place, smallestAge,
                biggestAge, smallestQuantOfPlayers, biggestQuantOfPlayers, playingTime, categories,
                quantOfPoints, quantOfTimesBeingChosen, isFavorite, Utils.getCurrentDate(), null);
        GamesProcessor.getGames().add(game);
        Toast.makeText(getActivity(), "Quant of games: " + GamesProcessor.getGames().size(), Toast.LENGTH_LONG).show();
        //??????
        //((MainActivity)getActivity()).adapter.notifyDataSetChanged();
    }

    private void clearFields() {
        nameText.setText("");
        descriptionText.setText("");
        //see on phone!!!
        //photoPathText.setText("ic_cubiki.xml");
        photoPathText.setText("");
        imageView.setImageResource(R.drawable.ic_cubiki);
        rulesText.setText("");
        placeText.setText("");
        //smallestAgeText.setText("");
        smallestAgeSp.setSelection(0);
        biggestAgeSp.setSelection(0);
        smallestQuantOfPlayersSp.setSelection(0);
        biggestQuantOfPlayersSp.setSelection(0);
        playingTimeSp.setSelection(0);
        categoriesText.setText("");
        //ButtonsActions.initMultiSpinner(categoriesList, categories, selectedCategories);
        initMultiSpinner();
        ButtonsActions.setDefaultStars(stars);
        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        isFavorite = false;
    }

}