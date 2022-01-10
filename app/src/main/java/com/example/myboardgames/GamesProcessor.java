package com.example.myboardgames;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class GamesProcessor {

    private static List<Game> games;
    private static List<String> categories;

    public GamesProcessor(){
        //games = new ArrayList<Game>();
    }

    public static List<Game> getGames() {
        return games;
    }

    public static List<String> getCategories() {
        return categories;
    }

    public static boolean gamesAreLoaded(){
        return games != null;
    }

    public static void loadGames(Context context){
        games = JSONHelper.importFromJSON(context);
        if(games != null){
            //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games);
            //listView.setAdapter(adapter);
            //TODO: write normal solution of categories
            categories = new ArrayList<>();
            categories.add(games.get(0).getCategories().get(0));
            categories.add(games.get(1).getCategories().get(0));
            Toast.makeText(context, "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean saveGames(Context context){
        boolean result = JSONHelper.exportToJSON(context, GamesProcessor.getGames());
        if(result){
            Toast.makeText(context, "Дані збережено", Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            Toast.makeText(context, "Не вдалося зберегти дані", Toast.LENGTH_LONG).show();
            return false;
        }
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
        } else if (type == SortType.POINTS_ASCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return (int) (o1.getQuantOfPoints() - o2.getQuantOfPoints());
                }
            });
        } else if (type == SortType.POINTS_DESCENDING) {
            games.sort(new Comparator<Game>() {
                @Override
                public int compare(Game o1, Game o2) {
                    return (int) (o2.getQuantOfPoints() - o1.getQuantOfPoints());
                }
            });
        }
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

    //think about effective filter
    public ArrayList<Game> filterGames(){
        ArrayList<Game> filteredGames = new ArrayList<>();
        /////////////////
        return filteredGames;
    }

    public ArrayList<Game> findGame(String str){
        ArrayList<Game> suitableGames = new ArrayList<>();
        /////////////////
        return suitableGames;
    }

    public Game getRecommendationOfTheDay(){
        Game game = new Game();
        //some algorithm
        return game;
    }


}
