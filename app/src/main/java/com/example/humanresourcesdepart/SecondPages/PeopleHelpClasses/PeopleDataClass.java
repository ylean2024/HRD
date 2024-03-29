package com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses;

public class PeopleDataClass {
    public String name;
    public String surname;

    public String imagePath;
    public int age;
    public String position;
    public String email;

    public PeopleDataClass() {}

    public PeopleDataClass(String name, String surname, int age, String position, String email, String imagePath) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.position = position;
        this.email = email;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
