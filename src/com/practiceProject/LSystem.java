package com.practiceProject;

import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import org.json.*;

public class LSystem extends AbstractLSystem{

    //Variables dans LSystem
    private Map<Character, Symbol> alphabet;
    private Map<Symbol,List<Symbol.Seq>> rules;
    private Map<Symbol,String> actions;
    private Sequence axiom;


    //Initialisation du LSystem
    private LSystem() {
        alphabet = new HashMap<>();
        rules = new HashMap<>();
        actions = new HashMap<>();
    }

    //Méthode pour ajouter un symbole dans alphabet du système
    @Override
    public Symbol addSymbol(char sym) {
        Symbol symbol = new Symbol(sym);
        alphabet.put(sym, symbol);
        return symbol;
    }

    //Méthode pour ajouter une règle associer à un symbole
    @Override
    public void addRule(Symbol sym, String expansion) {
        Symbol.Seq sequence = new Sequence(expansion);
        List<Symbol.Seq> symbolRule = this.rules.get(sym);
        if(symbolRule == null) {
            symbolRule = new LinkedList<>();
            this.rules.put(sym, symbolRule);
        }
        symbolRule.add(sequence);
    }

    //Méthode pour ajouter les actions
    @Override
    public void setAction(Symbol sym, String action) {
        actions.put(sym,action);
    }

    //Méthode pour définir axiome
    @Override
    public void setAxiom(String str) {
        this.axiom = new Sequence(str);
    }

    //Méthode qui retourne valeur d'axiome
    @Override
    public Symbol.Seq getAxiom() {
        return this.axiom;
    }

    //Méthode qui retourne règle aléatoire
    @Override
    public Symbol.Seq rewrite(Symbol sym) {
        List<Symbol.Seq> symbolRule = this.rules.get(sym);
        int indexRandomRule = (int) (Math.random() * (symbolRule.size()-1));
        if(symbolRule == null) {
            return null;
        }
        else if (symbolRule.size() == 1) {
            return symbolRule.get(0);
        }
        return symbolRule.get(indexRandomRule);
    }

    //Méthode qui dit à la tortue quelle action exécuter
    @Override
    public void tell(Turtle turtle, Symbol sym) {
        String action = this.actions.get(sym);
        //À modifier selon actions de tortues
        switch (action) {
            case "draw":
                turtle.draw();
                break;
            case "move":
                turtle.move();
                break;
            case "push":
                turtle.push();
                break;
            case "pop":
                turtle.pop();
                break;
            case "turnL":
                turtle.turnL();
                break;
            case "turnR":
                turtle.turnR();
                break;
        }
    }

    @Override
    public Symbol.Seq applyRules(Symbol.Seq seq, int n) {
        return null;
    }

    @Override
    public Rectangle2D tell(Turtle turtle, Symbol sym, int rounds) {
        return null;
    }

    public static LSystem readJsonFile(String file, Turtle turtle) throws FileNotFoundException {
        LSystem system = new LSystem();
        JSONObject input = new JSONObject(new JSONTokener(new FileReader(file)));

        //Initialize system axiom
        String axiom = input.getString("axiom");
        system.setAxiom(axiom);

        //Initialize system alphabet
        JSONArray alphabet = input.getJSONArray("alphabet");
        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.getString(i);
            system.addSymbol(letter.charAt(0));
        }

        //Initialize system rules
        JSONObject rules = input.getJSONObject("rules");
        for (char key : system.alphabet.keySet()) {
            Symbol symbol = system.alphabet.get(key);
            JSONArray rulesArray = rules.getJSONArray(String.valueOf(key));
            if(rulesArray != null) {
                for (int i = 0; i < rulesArray.length(); i++) {
                    String rule = rulesArray.getString(i);
                    system.addRule(symbol, rule);
                }
            }
        }

        //Initialize system actions
        JSONObject actions = input.getJSONObject("actions");
        for (char key : system.alphabet.keySet()) {
            Symbol symbol = system.alphabet.get(key);
            String action = actions.getString(String.valueOf(key));
            system.setAction(symbol, action);
        }

        //TODO: GET INPUT TURTLE, GET PARAMETERS THAT WILL INITIALIZE THE TURTLE
        //Initialize turtle for L-System
        JSONObject parameters = input.getJSONObject("parameters");
        //turtle.setUnits(parameters.getDouble("step"), parameters.getDouble("angle"));            TO BE CHANGED IF NEEDED
        //line for setting turtle start point

        return system;
    }
}
