package com.example.myboardgames.ui.addGame;

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

import com.example.myboardgames.ui.dialogs.CategoryDialog;
import com.example.myboardgames.helpers.ButtonsActions;
import com.example.myboardgames.models.Game;
import com.example.myboardgames.helpers.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.helpers.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddGameFragment extends Fragment {

    private View view;
    private EditText nameText, descriptionText, photoPathText, rulesText, placeText;
    private Spinner smallestAgeSp, biggestAgeSp, smallestQuantOfPlayersSp;
    private Spinner biggestQuantOfPlayersSp, playingTimeSp;
    private ImageView imageView;
    private ImageButton ibStar1, ibStar2, ibStar3, ibStar4, ibStar5;
    private ImageButton favoriteButton, addCategoryButton;
    private ImageButton[] stars = new ImageButton[5];
    private CategoryDialog dialog;
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
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        nameText = (EditText)(view.findViewById(R.id.nameText));
        descriptionText = (EditText)(view.findViewById(R.id.descriptionText));
        photoPathText = (EditText)(view.findViewById(R.id.photoPathText));
        rulesText = (EditText)(view.findViewById(R.id.rulesText));
        placeText = (EditText)(view.findViewById(R.id.placeText));
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
        initMultiSpinner();

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
            if (quantOfPoints > i) ButtonsActions.addStar(stars[i]); else ButtonsActions.removeStar(stars[i]);
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                dialog = new CategoryDialog(getContext(), R.string.dialog_title_add,
                        R.string.dialog_category_name_hint_add, R.string.dialog_button_add, "",
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoryName = dialog.getCategoryName();
                        if(!Utils.validateName(categoryName))
                            Toast.makeText(getContext(), "Введіть непорожню назву категорії!", Toast.LENGTH_LONG).show();
                        else if (GamesProcessor.categoryNameAlreadyExists(categoryName))
                            Toast.makeText(getContext(), "Така категорія вже існує!", Toast.LENGTH_LONG).show();
                        else{
                            GamesProcessor.addCategory(categoryName);
                            GamesProcessor.saveCategories(getContext());
                            int size = GamesProcessor.getCategories().size();
                            String[] helpCategories = new String[size];
                            System.arraycopy(categories, 0, helpCategories, 0, size - 1);
                            helpCategories[size-1] = categoryName;
                            categories = helpCategories;
                            boolean[] helpSelectedCategories = new boolean[size];
                            System.arraycopy(selectedCategories, 0, helpSelectedCategories, 0, size - 1);
                            selectedCategories = helpSelectedCategories;
                            ButtonsActions.setCategoriesListener(categoriesText, getActivity(), categoriesList, categories, selectedCategories);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
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
                if (addGame() && GamesProcessor.saveGames(getContext())) clearFields();
            }
        });

        imageView = (ImageView)(view.findViewById(R.id.imageView));
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
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
        categoriesText = (TextView)(view.findViewById(R.id.categoriesText));
        categoriesList = new ArrayList<>();
        List<String> listCategories = GamesProcessor.getCategories();
        categories = listCategories.toArray(new String[0]);
        selectedCategories = new boolean[categories.length];
        ButtonsActions.setCategoriesListener(categoriesText, getActivity(), categoriesList, categories, selectedCategories);
    }

    private boolean addGame(){
        String name = nameText.getText().toString().trim();
        if (name.length() == 0) {
            Toast.makeText(getActivity(), "Введіть назву гри!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (GamesProcessor.gameAlreadyExists(name)) {
            Toast.makeText(getActivity(), "Гра з такою назвою вже існує", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            String description = descriptionText.getText().toString();
            String photoPath = photoPathText.getText().toString();
            String rules = rulesText.getText().toString();
            String place = placeText.getText().toString();
            int smallestAge = Integer.parseInt((String)smallestAgeSp.getSelectedItem());
            int biggestAge = Integer.parseInt((String)biggestAgeSp.getSelectedItem());
            int smallestQuantOfPlayers = Integer.parseInt((String)smallestQuantOfPlayersSp.getSelectedItem());
            int biggestQuantOfPlayers = Integer.parseInt((String)biggestQuantOfPlayersSp.getSelectedItem());
            String playingTime = (String)playingTimeSp.getSelectedItem();
            List<String> categories = Arrays.asList(categoriesText.getText().toString().split("\\s*,\\s*"));
            if (categories.get(0).equals("")) {
                categories = new ArrayList<>();
                categories.add("загальна категорія");
            }
            int quantOfTimesBeingChosen = 0;

            Game game = new Game(name, description, photoPath, rules, place, smallestAge,
                    biggestAge, smallestQuantOfPlayers, biggestQuantOfPlayers, playingTime, categories,
                    quantOfPoints, quantOfTimesBeingChosen, isFavorite, Utils.getCurrentDate(), null);
            GamesProcessor.addGame(game);
            return true;
        }
    }

    private void clearFields() {
        nameText.setText("");
        descriptionText.setText("");
        photoPathText.setText("");
        imageView.setImageResource(R.drawable.ic_cubiki);
        rulesText.setText("");
        placeText.setText("");
        smallestAgeSp.setSelection(0);
        biggestAgeSp.setSelection(0);
        smallestQuantOfPlayersSp.setSelection(0);
        biggestQuantOfPlayersSp.setSelection(0);
        playingTimeSp.setSelection(0);
        categoriesText.setText("");
        initMultiSpinner();
        ButtonsActions.setDefaultStars(stars);
        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        isFavorite = false;
    }

}