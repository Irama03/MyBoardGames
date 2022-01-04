package com.example.myboardgames;

import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Game implements Serializable {

    private String name;
    private String description;
    private String photoPath;
    //you can open and close
    private String rules;
    //where the game is now
    private String place;
    private int smallestAge;
    private int biggestAge;
    private int smallestQuantOfPlayers;
    private int biggestQuantOfPlayers;
    private List<String> categories;
    //from 1 to 5
    private int quantOfPoints;
    private int quantOfTimesBeingChosen;
    //will be with star
    private boolean isFavorite;
    private Date dateOfAdding;
    private Date dateOfLastChoosing;

    public Game(){

    }

    public Game(String name, String description, String photoPath, String rules, String place,
                int smallestAge, int biggestAge, int smallestQuantOfPlayers,
                int biggestQuantOfPlayers, List<String> categories, int quantOfPoints,
                int quantOfTimesBeingChosen, boolean isFavorite, Date dateOfAdding,
                Date dateOfLastChoosing) {
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.rules = rules;
        this.place = place;
        this.smallestAge = smallestAge;
        this.biggestAge = biggestAge;
        this.smallestQuantOfPlayers = smallestQuantOfPlayers;
        this.biggestQuantOfPlayers = biggestQuantOfPlayers;
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

    public void setPlace(String rules) {
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

    public void setQuantOfTimesBeingChosen(int quantOfTimesBeingChosen) {
        this.quantOfTimesBeingChosen = quantOfTimesBeingChosen;
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

    public void setDateOfAdding(Date dateOfAdding) {
        this.dateOfAdding = dateOfAdding;
    }

    public Date getDateOfLastChoosing() {
        return dateOfLastChoosing;
    }

    public void setDateOfLastChoosing(Date dateOfLastChoosing) {
        this.dateOfLastChoosing = dateOfLastChoosing;
    }

    @Override
    public String toString(){
        return "Назва: " + name + "\nОпис: " + description + "\nЗображення: " + photoPath +
                "\nПравила: " + rules + "\nМісцезнаходження: " + place +
                "\nНайменший вік гравців: " + smallestAge +
                "\nНайбільший вік гравців: " + biggestAge +
                "\nНайменша кількість гравців: "+ smallestQuantOfPlayers +
                "\nНайбільша кількість гравців: "+ biggestQuantOfPlayers +
                "\nКатегорії: " + categories + "\nКількість балів: " + quantOfPoints +
                "\nСкільки разів гру обирали: " + quantOfTimesBeingChosen +
                "\nЧи є улюбленою: " + isFavorite +
                "\nДата додання: " + (dateOfLastChoosing == null ? "no date" : DateTimeUtils.formatWithStyle(dateOfAdding, DateTimeStyle.LONG)) +
                "\nДата останнього обрання: " + (dateOfLastChoosing == null ? "no date" : DateTimeUtils.formatWithStyle(dateOfLastChoosing, DateTimeStyle.LONG));
    }

}
