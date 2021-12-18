package com.example.myboardgames;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GamesProcessor {

    private static List<Game> games;

    public GamesProcessor(){
        //games = new ArrayList<Game>();
    }

    public static List<Game> getGames() {
        return games;
    }

    public static void setGames(List<Game> games) {
        GamesProcessor.games = games;
    }

    public static boolean gamesAreLoaded(){
        return games != null;
    }

    public static void loadGames(Context context){
        games = JSONHelper.importFromJSON(context);
        if(games != null){
            //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games);
            //listView.setAdapter(adapter);
            Toast.makeText(context, "Дані відновлено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Не вдалося відновити дані", Toast.LENGTH_LONG).show();
        }
    }

    public void saveGames(){

    }

    public void createGame(Game game){
        games.add(game);
    }

    public void chooseGame(Game game){
        game.setQuantOfTimesBeingChosen(game.getQuantOfTimesBeingChosen() + 1);
        //smth with graphical interface
    }

    //Maybe, not useful
    public void editGame(Game game){

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
