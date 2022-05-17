package com.example.myboardgames.models;

import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Game implements Serializable {

    private String name;
    private String description;
    private String photoPath;
    private String rules;
    //where the game is now
    private String place;
    private int smallestAge;
    private int biggestAge;
    private int smallestQuantOfPlayers;
    private int biggestQuantOfPlayers;
    private String playingTime;
    private List<String> categories;
    //from 1 to 5
    private int quantOfPoints;
    //with heart
    private boolean isFavorite;
    private int quantOfTimesBeingChosen;
    private Date dateOfAdding;
    private Date dateOfLastChoosing;

    public Game(){

    }

    public Game(String name, String description, String photoPath, String rules, String place,
                int smallestAge, int biggestAge, int smallestQuantOfPlayers,
                int biggestQuantOfPlayers, String playingTime, List<String> categories,
                int quantOfPoints, int quantOfTimesBeingChosen, boolean isFavorite,
                Date dateOfAdding, Date dateOfLastChoosing) {
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.rules = rules;
        this.place = place;
        this.smallestAge = smallestAge;
        this.biggestAge = biggestAge;
        this.smallestQuantOfPlayers = smallestQuantOfPlayers;
        this.biggestQuantOfPlayers = biggestQuantOfPlayers;
        this.playingTime = playingTime;
        this.categories = categories;
        this.quantOfPoints = quantOfPoints;
        this.quantOfTimesBeingChosen = quantOfTimesBeingChosen;
        this.isFavorite = isFavorite;
        this.dateOfAdding = dateOfAdding;
        this.dateOfLastChoosing = dateOfLastChoosing;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public List<String> getCategories() {
        return categories;
    }

    public String getCategoriesToString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = categories.iterator();
        if (it.hasNext()) sb.append(it.next());
        while (it.hasNext()) {
            sb.append(", ");
            sb.append(it.next());
        }
        return sb.toString();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public int getQuantOfPoints() {
        return quantOfPoints;
    }

    public void setQuantOfPoints(int quantOfPoints) {
        this.quantOfPoints = quantOfPoints;
    }

    public int getQuantOfTimesBeingChosen() {
        return quantOfTimesBeingChosen;
    }

    public void increaseQuantOfTimesBeingChosen() {
        quantOfTimesBeingChosen = quantOfTimesBeingChosen + 1;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Date getDateOfAdding() {
        return dateOfAdding;
    }

    public Date getDateOfLastChoosing() {
        return dateOfLastChoosing;
    }

    public void setDateOfLastChoosing(Date dateOfLastChoosing) {
        this.dateOfLastChoosing = dateOfLastChoosing;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Game game = (Game) obj;
        return name.equals(game.getName());
    }

    @Override
    public String toString(){
        return "Назва: " + name + "\nОпис: " + description + "\nЗображення: " + photoPath +
                "\nПравила: " + rules + "\nМісцезнаходження: " + place +
                "\nНайменший вік гравців: " + smallestAge +
                "\nНайбільший вік гравців: " + biggestAge +
                "\nНайменша кількість гравців: "+ smallestQuantOfPlayers +
                "\nНайбільша кількість гравців: "+ biggestQuantOfPlayers +
                "\nПриблизний час гри: " + playingTime +
                "\nКатегорії: " + getCategoriesToString() + "\nКількість балів: " + quantOfPoints +
                "\nСкільки разів гру обирали: " + quantOfTimesBeingChosen +
                "\nЧи є улюбленою: " + isFavorite +
                "\nДата додавання: " + (dateOfLastChoosing == null ? "невідомо" : DateTimeUtils.formatWithStyle(dateOfAdding, DateTimeStyle.LONG)) +
                "\nДата останнього обрання: " + (dateOfLastChoosing == null ? "немає" : DateTimeUtils.formatWithStyle(dateOfLastChoosing, DateTimeStyle.LONG));
    }

}
