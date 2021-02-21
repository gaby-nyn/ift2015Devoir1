package com.practiceProject;

import com.practiceProject.Interface.Turtle;

import java.io.FileNotFoundException;
import org.json.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        JSONObject fileTest = new JSONObject();

        JSONArray alphabet = new JSONArray(new String[]{"F", "[", "]", "+", "-"});
        fileTest.put("alphabet", alphabet);

        JSONArray rules = new JSONArray(new String[]{"F[+F]F[-F]F","F[+F]F","F[+F]F[-F]F"});
        JSONObject symbolObject = new JSONObject();
        symbolObject.put("F", rules);
        fileTest.put("rules", symbolObject);

        fileTest.put("axiom", "F");

        JSONObject actionData = new JSONObject();
        actionData.put("F", "draw");
        actionData.put("[","push");
        actionData.put("]","pop");
        actionData.put("+","turnL");
        actionData.put("-","turnR");
        fileTest.put("actions", actionData);

        JSONObject paramData = new JSONObject();
        JSONArray start = new JSONArray(new double[]{0,0,90});
        paramData.put("step", 2);
        paramData.put("angle", 22.5);
        paramData.put("start", start);
        fileTest.put("parameters", paramData);

        String jsonFile = fileTest.toString();
        Turtle turtle = new TurtleImpl();
        System.out.println(jsonFile);

        LSystem system = LSystem.readJsonFile(fileTest, turtle);
    }
}
