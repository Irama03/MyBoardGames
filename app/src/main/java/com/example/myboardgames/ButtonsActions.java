package com.example.myboardgames;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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

    public static void chooseAction(Game game, ImageButton check, Context context) {
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamesProcessor.chooseGame(game);
                GamesProcessor.saveGames(context);
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
}
