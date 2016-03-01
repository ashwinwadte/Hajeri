package com.example.ashwin.hajeri.model;

/**
 * Created by Ashwin on 11/9/2015.
 */
public class StudentModel {
    int id;
    String GRNo;
    String lastName;
    String firstName;
    String middleName;
    String rollNo;
    String division;
    String year;
    String className;

    public StudentModel() {
    }

    public StudentModel(String GRNo, String lastName, String firstName, String middleName, String rollNo, String division, String year, String className) {
        this.GRNo = GRNo;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.rollNo = rollNo;
        this.division = division;
        this.year = year;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGRNo() {
        return GRNo;
    }

    public void setGRNo(String GRNo) {
        this.GRNo = GRNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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
}
