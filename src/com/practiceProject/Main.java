package com.practiceProject;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String jsonFile = "";
        LSystem system = LSystem.getLSystemFromJsonFile(jsonFile);
    }
}
