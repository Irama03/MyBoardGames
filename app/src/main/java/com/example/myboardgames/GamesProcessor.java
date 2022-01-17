package com.example.myboardgames;

import android.content.Context;
import android.widget.Toast;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GamesProcessor {

    private static List<Game> games;
    private static List<String> categories;

    public static List<Game> getGames() {
        return games;
    }

    public static List<String> getCategories() {
        return categories;
    }

    public static boolean gamesAreLoaded(){
        return games != null;
    }

    public static boolean categoriesAreLoaded(){
        return categories != null;
    }

    public static void loadGames(Context context){
        games = JSONHelper.importGamesFromJSON(context);
        if(games != null){
            Toast.makeText(context, "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }
    }

    public static void loadCategories(Context context){
        categories = JSONHelper.importCategoriesFromJSON(context);
        if(categories != null){
            Toast.makeText(context, "Категорії відновлено", Toast.LENGTH_LONG).show();
            if (categories.size() == 0) {
                categories.addAll(Arrays.asList(context.getResources().getStringArray(R.array.start_categories)));
                saveCategories(context);
            }
        }
        else{
            categories = new ArrayList<>();
            categories.addAll(Arrays.asList(context.getResources().getStringArray(R.array.start_categories)));
            saveCategories(context);
            Toast.makeText(context, "Встановлено дефолтні категорії", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean saveGames(Context context){
        boolean result = JSONHelper.exportGamesToJSON(context, getGames());
        if(result){
            Toast.makeText(context, "Дані збережено", Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            Toast.makeText(context, "Не вдалося зберегти дані", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void saveCategories(Context context){
        boolean result = JSONHelper.exportCategoriesToJSON(context, getCategories());
        if(result)
            Toast.makeText(context, "Категорії збережено", Toast.LENGTH_LONG).show();
        else Toast.makeText(context, "Не вдалося зберегти категорії", Toast.LENGTH_LONG).show();
    }

    public static void sortGames(List<Game> games, SortType type) {
        if (type == SortType.NAME_ASCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (type == SortType.NAME_DESCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return -o1.getName().compareTo(o2.getName());
                }
            });
        }
        else if (type == SortType.DATE_ASCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return DateTimeUtils.getDateDiff(o1.getDateOfLastChoosing(), o2.getDateOfLastChoosing(), DateTimeUnits.MILLISECONDS);
                }
            });
        } else if (type == SortType.DATE_DESCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return -DateTimeUtils.getDateDiff(o1.getDateOfLastChoosing(), o2.getDateOfLastChoosing(), DateTimeUnits.MILLISECONDS);
                }
            });
        } else if (type == SortType.POINTS_DESCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return o2.getQuantOfPoints() - o1.getQuantOfPoints();
                }
            });
        } else if (type == SortType.POINTS_ASCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return o1.getQuantOfPoints() - o2.getQuantOfPoints();
                }
            });
        }
    }

    public static Game getGameByName(String name) {
        for (Game game: games) {
            if (game.getName().equals(name))
                return game;
        }
        return null;
    }

    public static List<Game> getCopyOfGames() {
        List<Game> result = new ArrayList<Game>(games.size());
        result.addAll(games);
        return result;
    }

    public void createGame(Game game){
        games.add(game);
    }

    public static void chooseGame(Game game){
        game.increaseQuantOfTimesBeingChosen();
        game.setDateOfLastChoosing(Utils.getCurrentDate());
    }

    public void deleteGame(Game game){
        //games.remove(position);
        games.remove(game);
    }

    public Game getRecommendationOfTheDay(){
        Game game = new Game();
        //some algorithm
        return game;
    }


}
