package com.example.ashwin.hajeri.model;

/**
 * Created by Ashwin on 11/9/2015.
 */
public class ClassListModel {
    int id;
    String strength;
    String year;
    String className;
    String classType;
    String division;
    String batch;

    public ClassListModel() {
    }

    public ClassListModel(String strength, String year, String className, String classType, String division, String batch) {
        this.strength = strength;
        this.year = year;
        this.className = className;
        this.classType = classType;
        this.division = division;
        this.batch = batch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
