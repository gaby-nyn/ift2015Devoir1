package com.practiceProject;

import com.practiceProject.Interface.Turtle;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String jsonFile = "";
        Turtle turtle = new TurtleImpl();
        LSystem system = LSystem.readJsonFile(jsonFile, turtle);
    }
}
