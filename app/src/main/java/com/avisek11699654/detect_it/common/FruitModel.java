package com.avisek11699654.detect_it.common;

public class FruitModel {

    private int id;
    private String name;
    private String confidecne;

    public FruitModel(String name, String confidecne) {
        this.name = name;
        this.confidecne = confidecne;
    }
    public FruitModel(int id, String name, String confidecne) {
        this.id = id;
        this.name = name;
        this.confidecne = confidecne;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public  String getConfidecne() {
        return confidecne;
    }

}
