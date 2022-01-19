package com.example.myboardgames;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ButtonsActions {

    public static void makeNotFavorite(ImageButton heart) {
        heart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    public static void makeFavorite(ImageButton heart) {
        heart.setImageResource(R.drawable.ic_baseline_favorite_24);
    }

    public static void favoriteAction(Game game, ImageButton heart, Context context) {
        if (game.isFavorite())
            heart.setImageResource(R.drawable.ic_baseline_favorite_24);
        else heart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.isFavorite()) {
                    makeNotFavorite(heart);
                    game.setIsFavorite(false);
                }
                else {
                    makeFavorite(heart);
                    game.setIsFavorite(true);
                }
                GamesProcessor.saveGames(context);
            }
        });
    }

    public static void chooseAction(Game game, ImageButton check, Context context, TextView quantOfTimesBeingChosenText, TextView dateOfLastChoosingText) {
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamesProcessor.chooseGame(game);
                GamesProcessor.saveGames(context);
                if (quantOfTimesBeingChosenText != null) {
                    quantOfTimesBeingChosenText.setText(game.getQuantOfTimesBeingChosen() + "");
                    dateOfLastChoosingText.setText(DateTimeUtils.formatDate(game.getDateOfLastChoosing()));
                }
            }
        });
    }

    public static void addStar(ImageButton ibStar) {
        ibStar.setImageResource(R.drawable.ic_baseline_star_24);
    }

    public static void removeStar(ImageButton ibStar) {
        ibStar.setImageResource(R.drawable.ic_baseline_star_outline_24);
    }

    public static void processSmallerQuantOfStars(ImageButton[] stars, int finalI) {
        for (int j = 0; j <= finalI; j++)
            addStar(stars[j]);
    }

    public static void processTheSameQuantOfStars(ImageButton[] stars) {
        for (ImageButton star : stars)
            removeStar(star);
    }

    public static void processBiggerQuantOfStars(ImageButton[] stars, int finalI) {
        for (int j = finalI + 1; j < stars.length; j++)
            removeStar(stars[j]);
    }

    public static void pointsAction(Game game, ImageButton[] stars, Context context) {
        for (int i = 0; i < stars.length; i++) {
            int finalI = i;
            //Why fignya???
            if (game.getQuantOfPoints() > i) addStar(stars[i]); else removeStar(stars[i]);
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int points = game.getQuantOfPoints();
                    //TODO Перевірити в якомусь додатку!
                    if (points <= finalI) {
                        processSmallerQuantOfStars(stars, finalI);
                        game.setQuantOfPoints(finalI + 1);
                        //Toast.makeText(context, "smaller; there were " + points + " points; now " + (finalI + 1) + " points", Toast.LENGTH_LONG).show();
                    }
                    else if (points == finalI + 1) {
                        processTheSameQuantOfStars(stars);
                        game.setQuantOfPoints(0);
                        //Toast.makeText(context, "the same; there were " + points + " points; now " + (0) + " points", Toast.LENGTH_LONG).show();
                    }
                    else {
                        processBiggerQuantOfStars(stars, finalI);
                        game.setQuantOfPoints(finalI + 1);
                        //Toast.makeText(context, "bigger; there were " + points + " points; now " + (finalI + 1) + " points", Toast.LENGTH_LONG).show();
                    }
                    GamesProcessor.saveGames(context);
                }
            });
        }
    }

    public static void setDefaultStars(ImageButton[] stars) {
        for (ImageButton star : stars)
            removeStar(star);
    }

    /**
     * method is used to hide keyboard
     */
    public static void hideKeyboard(FragmentActivity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * method is used to set listener to spheres list
     */
    public static void setCategoriesListener(TextView categoriesText, FragmentActivity activity, List<Integer> categoriesList, String[] categories, boolean[] selectedCategories) {
        categoriesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonsActions.hideKeyboard(activity);
                //Toast.makeText(activity, "categoriesList: " + categoriesList + "; categories.length: " + categories.length + "; selectedCategories.length: " + selectedCategories.length, Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.selectCategories);
                builder.setCancelable(true);
                builder.setMultiChoiceItems(categories, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            categoriesList.add(which);
                            Collections.sort(categoriesList);
                        }else{
                            Integer integer = which;
                            categoriesList.remove(integer);
                        }
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i = 0; i < categoriesList.size(); i++){
                            stringBuilder.append(categories[categoriesList.get(i)]);
                            if(i != categoriesList.size() - 1)
                                stringBuilder.append(", ");
                        }
                        String concatCategories = stringBuilder.toString();
                        categoriesText.setText(concatCategories);
                        //setEnabledButton();
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static void removeOnClickListener(View view) {
        view.setOnClickListener(null);
        view.setClickable(false);
    }

}
