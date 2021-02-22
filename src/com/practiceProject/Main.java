package com.practiceProject;

import com.practiceProject.Interface.Turtle;

import java.io.File;
import java.io.FileNotFoundException;
import org.json.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String jsonFileName = args[0];
        int rounds = Integer.parseInt(args[1]);

        Turtle turtle = new TurtleImpl();
        LSystem system = LSystem.readJsonFile(jsonFileName, turtle);
        system.tell(turtle, system.getAxiom(), rounds);

        System.out.println("stroke");
        System.out.println("%%Trailer");

    }
}
