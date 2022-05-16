package com.example.myboardgames.helpers;

import android.content.Context;

import com.example.myboardgames.models.Game;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JSONHelper {

    /*private static final String FILE_GAMES_NAME = "games.json";
    private static final String FILE_CATEGORIES_NAME = "categories.json";

    private static boolean exportObjectsToJSON(Context context, List<Object> dataList, GameItems dataItems, String fileName) {
        Gson gson = new Gson();
        dataItems.setItems(dataList);
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean exportGamesToJSON(Context context, List<Game> gamesList) {
        GameItems<Game> dataItems = new GameItems();
        return exportObjectsToJSON(context, Collections.singletonList(gamesList), dataItems, FILE_GAMES_NAME);
    }

    public static boolean exportCategoriesToJSON(Context context, List<String> categoriesList) {
        GameItems<String> dataItems = new CategoryItems();
        return exportObjectsToJSON(context, Collections.singletonList(categoriesList), dataItems, FILE_CATEGORIES_NAME);
    }

    private static void inFinally(InputStreamReader streamReader, FileInputStream fileInputStream) {
        if (streamReader != null) {
            try {
                streamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Game> importGamesFromJSON(Context context) {
        //return importObjectsFromJSON(context, FILE_GAMES_NAME).getItems();
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(FILE_GAMES_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            //return gson.fromJson(streamReader, GameItems.class).getItems();
            GameItems<Game> dataItems = gson.fromJson(streamReader, GameItems.class);
            return dataItems.getItems();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            inFinally(streamReader, fileInputStream);
        }
        return null;
    }

    public static List<String> importCategoriesFromJSON(Context context) {
        //return importObjectsFromJSON(context, FILE_CATEGORIES_NAME).getItems();
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(FILE_CATEGORIES_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            //return gson.fromJson(streamReader, GameItems.class);
            GameItems<String> dataItems = gson.fromJson(streamReader, CategoryItems.class);
            return dataItems.getItems();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            inFinally(streamReader, fileInputStream);
        }
        return null;
    }

    private interface GameItems<O> {
        List<O> getItems();
        void setItems(List<O> items);
    }

    private static class GameItems implements GameItems<Game> {
        private List<Game> games;

        @Override
        public List<Game> getItems() {
            return games;
        }

        @Override
        public void setItems(List<Game> items) {
            this.games = items;
        }
    }

    private static class CategoryItems implements GameItems<String> {
        private List<String> categories;

        @Override
        public List<String> getItems() {
            return categories;
        }

        @Override
        public void setItems(List<String> items) {
            this.categories = items;
        }
    }*/

    private static final String FILE_GAMES_NAME = "games.json";
    private static final String FILE_CATEGORIES_NAME = "categories.json";

    private static void inFinally(InputStreamReader streamReader, FileInputStream fileInputStream) {
        if (streamReader != null) {
            try {
                streamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean exportGamesToJSON(Context context, List<Game> dataList) {
        Gson gson = new Gson();
        GameItems gameItems = new GameItems();
        gameItems.setGames(dataList);
        String jsonString = gson.toJson(gameItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_GAMES_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean exportCategoriesToJSON(Context context, List<String> dataList) {
        Gson gson = new Gson();
        CategoryItems categoryItems = new CategoryItems();
        categoryItems.setCategories(dataList);
        String jsonString = gson.toJson(categoryItems);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_CATEGORIES_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static List<Game> importGamesFromJSON(Context context) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(FILE_GAMES_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            GameItems gameItems = gson.fromJson(streamReader, GameItems.class);
            return gameItems.getGames();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            inFinally(streamReader, fileInputStream);
        }

        return null;
    }

    public static List<String> importCategoriesFromJSON(Context context) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(FILE_CATEGORIES_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            CategoryItems categoryItems = gson.fromJson(streamReader, CategoryItems.class);
            return categoryItems.getCategories();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            inFinally(streamReader, fileInputStream);
        }
        return null;
    }

    private static class GameItems {
        private List<Game> games;

        List<Game> getGames() {
            return games;
        }
        void setGames(List<Game> games) {
            this.games = games;
        }
    }

    private static class CategoryItems {
        private List<String> categories;

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }
    }
}

