package com.practiceProject;

import com.practiceProject.Interface.Turtle;

import java.io.File;
import java.io.FileNotFoundException;
import org.json.*;
import java.awt.*;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        try {
            String jsonFileName = args[0];
            int rounds = Integer.parseInt(args[1]);

            Turtle turtle = new TurtleImpl();
            LSystem system = LSystem.readJsonFile(jsonFileName, turtle);
            system.tell(turtle, system.getAxiom(), rounds);

            System.out.println("stroke");
            System.out.println("%%Trailer");
        } catch (LSystemJsonException e) {
            System.out.println(e.getMessage());
        }
    }
}
