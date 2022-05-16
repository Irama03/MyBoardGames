package com.example.myboardgames.models;

import com.example.myboardgames.database.AppDatabase;

import java.io.Serializable;
import java.util.UUID;

public class SharedGame implements UniqueObject, Serializable {

    private String id;
    private String name;
    private String description;
    //private String photoPath;
    private String rules;
    private int smallestAge;
    private int biggestAge;
    private int smallestQuantOfPlayers;
    private int biggestQuantOfPlayers;
    private String playingTime;
    private String categories;
    private String recommenderId;
    private String receiverId;

    public SharedGame(){

    }

    public SharedGame(String id, String name, String description, String rules,
                int smallestAge, int biggestAge, int smallestQuantOfPlayers,
                int biggestQuantOfPlayers, String playingTime, String categories,
                String recommenderId, String receiverId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.smallestAge = smallestAge;
        this.biggestAge = biggestAge;
        this.smallestQuantOfPlayers = smallestQuantOfPlayers;
        this.biggestQuantOfPlayers = biggestQuantOfPlayers;
        this.playingTime = playingTime;
        this.categories = categories;
        this.recommenderId = recommenderId;
        this.receiverId = receiverId;
    }

    public SharedGame(Game game, String recommenderId, String receiverId) {
        this.id = UUID.randomUUID().toString();
        this.name = game.getName();
        this.description = game.getDescription();
        this.rules = game.getRules();
        this.smallestAge = game.getSmallestAge();
        this.biggestAge = game.getBiggestAge();
        this.smallestQuantOfPlayers = game.getSmallestQuantOfPlayers();
        this.biggestQuantOfPlayers = game.getBiggestQuantOfPlayers();
        this.playingTime = game.getPlayingTime();
        this.categories = game.getCategoriesToString();
        this.recommenderId = recommenderId;
        this.receiverId = receiverId;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getSmallestAge() {
        return smallestAge;
    }

    public void setSmallestAge(int smallestAge) {
        this.smallestAge = smallestAge;
    }

    public int getBiggestAge() {
        return biggestAge;
    }

    public void setBiggestAge(int biggestAge) {
        this.biggestAge = biggestAge;
    }

    public int getSmallestQuantOfPlayers() {
        return smallestQuantOfPlayers;
    }

    public void setSmallestQuantOfPlayers(int smallestQuantOfPlayers) {
        this.smallestQuantOfPlayers = smallestQuantOfPlayers;
    }

    public int getBiggestQuantOfPlayers() {
        return biggestQuantOfPlayers;
    }

    public void setBiggestQuantOfPlayers(int biggestQuantOfPlayers) {
        this.biggestQuantOfPlayers = biggestQuantOfPlayers;
    }

    public String getPlayingTime() {
        return playingTime;
    }

    public void setPlayingTime(String playingTime) {
        this.playingTime = playingTime;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getRecommenderId() {
        return recommenderId;
    }

    public void setRecommenderId(String recommenderId) {
        this.recommenderId = recommenderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String whichGroup() {
        return AppDatabase.SHARED_GAMES_GROUP_KEY;
    }
}
