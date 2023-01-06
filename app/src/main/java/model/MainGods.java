package model;

import java.util.ArrayList;

public class MainGods {
    public String getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = god;
    }

    String god;
    public String getGodName() {
        return godName;
    }

    public void setGodName(String godName) {
        this.godName = godName;
    }

    public ArrayList<GodImages> getGodImages() {
        return godImages;
    }

    public void setGodImages(ArrayList<GodImages> godImages) {
        this.godImages = godImages;
    }

    public MainGods(String godName, ArrayList<GodImages> godImages) {
        this.godName = godName;
        this.godImages = godImages;
    }

    public MainGods() {
    }

    private String godName;
    private ArrayList<GodImages> godImages;
}
