package com.example.myboardgames;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

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

}
