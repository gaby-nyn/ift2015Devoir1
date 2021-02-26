package com.practiceProject;

import com.practiceProject.Interface.Turtle;
import com.practiceProject.Model.State;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;

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
        Symbol.Seq sequence = new Sequence(expansion, alphabet);
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
        this.axiom = new Sequence(str, alphabet);
    }

    //Méthode qui retourne valeur d'axiome
    @Override
    public Symbol.Seq getAxiom() {
        return this.axiom;
    }

    //Méthode qui retourne règle aléatoire
    @Override
    public Symbol.Seq rewrite(Symbol sym) {
        List<Symbol.Seq> symbolRule = rules.get(sym);
        if(symbolRule == null) {
            return null;
        }
        else if (symbolRule.size() == 1) {
            return symbolRule.get(0);
        }
        int indexRandomRule = (int) (Math.random() * (symbolRule.size()-1));
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

    //Method that applies rules to rounds of rewriting
    @Override
    public Symbol.Seq applyRules(Symbol.Seq seq, int n) {
        Symbol.Seq returnedSequence;
        Iterator<Symbol> iterator;
        iterator = seq.iterator();
        returnedSequence = iterateSequence(iterator);
        if(n > 1) {
            List<String> toAdd = new ArrayList<>();
            for(int i = 1; i < n; i++) {
                Iterator<Symbol> returnedSequenceIterator = returnedSequence.iterator();
                Symbol.Seq iteratedSequence = iterateSequence(returnedSequenceIterator);
                for(String s : iteratedSequence.getSequenceList()){
                    toAdd.add(s);
                }
            }
            for (String s : toAdd) {
                returnedSequence.addSequence(s);
            }
        }
        return returnedSequence;
    }

    @Override
    public Rectangle2D tell(Turtle turtle, Symbol.Seq seq, int rounds) {
        Symbol.Seq sequence;
        Iterator<Symbol> iterator;
        Rectangle2D returnedRectangle;
        TurtleImpl turtleImplemented = (TurtleImpl) turtle;
        Stack<State> stateStack;
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;

        //If no rounds of rewriting, just
        if(rounds == 0) {
            sequence = seq;
            iterator = sequence.iterator();
            while(iterator.hasNext()) {
                tell(turtle, iterator.next());
            }
            stateStack = turtleImplemented.getStateStack();
            for(State state : stateStack) {
                if(maxX < state.getPosition().getX()) {
                    maxX = state.getPosition().getX();
                }
                else if(maxX > state.getPosition().getX() && minX < state.getPosition().getX()) {
                    minX = state.getPosition().getX();
                }
                if(maxY < state.getPosition().getY()) {
                    maxY = state.getPosition().getY();
                }
                else if(maxY > state.getPosition().getY() && minY < state.getPosition().getY()) {
                    minY = state.getPosition().getY();
                }
            }
            returnedRectangle = new Rectangle2D.Double(stateStack.get(0).getPosition().getX(), stateStack.get(0).getPosition().getY(), maxX-minX, maxY-minY);
        }
        else {
            sequence = applyRules(seq, 1);
            iterator = sequence.iterator();
            while(iterator.hasNext()) {
                tell(turtle, iterator.next());
            }
            returnedRectangle = tell(turtle, sequence, rounds-1);
        }

        return returnedRectangle;
    }

    //Method that reads JSON file and assigns data where they have to go (INIT)
    public static LSystem readJsonFile(String file, Turtle turtle) throws LSystemJsonException {
        LSystem system = new LSystem();
        FileReader fileReader;
        JSONObject input;
        try {
            fileReader = new FileReader(file);
            input = new JSONObject(new JSONTokener(fileReader));
        } catch (FileNotFoundException e) {
            throw new LSystemJsonException("No file found.");
        }
        //Verification for keys
        validateFile(input);

        //Initialize system alphabet
        JSONArray alphabet = input.optJSONArray("alphabet");
        //Verification for alphabet
        validateAlphabet(alphabet);
        for (int i = 0; i < alphabet.length(); i++) {
            String letter = alphabet.getString(i);
            system.addSymbol(letter.charAt(0));
        }

        //Initialize system axiom
        String axiom = input.optString("axiom");
        //Verification for axiom
        validateAxiom(axiom, system.alphabet);
        system.setAxiom(axiom);

        //Initialize system rules
        JSONObject rules = input.optJSONObject("rules");
        //Verification for rules
        validateRules(rules, system.alphabet);
        for (String key : rules.keySet()) {
            JSONArray rulesArray = rules.getJSONArray(String.valueOf(key));
            for (int i = 0; i < rulesArray.length(); i++) {
                String rule = rulesArray.getString(i);
                system.addRule(system.alphabet.get(key.charAt(0)), rule);
            }
        }

        //Initialize system actions
        JSONObject actions = input.optJSONObject("actions");
        //Verification for actions
        validateActions(actions, system.alphabet);
        for (char key : system.alphabet.keySet()) {
            Symbol symbol = system.alphabet.get(key);
            String action = actions.getString(String.valueOf(key));
            system.setAction(symbol, action);
        }

        //Initialize turtle for L-System
        JSONObject parameters = input.optJSONObject("parameters");
        //Validate parameters
        validateParameters(parameters);
        turtle.setUnits(parameters.getDouble("step"), parameters.getDouble("angle"));
        JSONArray coordinate = parameters.getJSONArray("start");
        Point2D point = new Point2D.Double(coordinate.getDouble(0), coordinate.getDouble(1));
        double angle = coordinate.getDouble(2);
        turtle.init(point, angle);
        return system;
    }

    //Method that iterates through iterator given
    public Symbol.Seq iterateSequence(Iterator<Symbol> iterator) {
        Symbol.Seq returnedSequence = null;
        List<String> toAdd = new ArrayList<>();
        while(iterator.hasNext()) {
            Symbol symbol = iterator.next();
            Symbol.Seq rewrittenSequence;
            if (returnedSequence == null) {
                returnedSequence = rewrite(symbol);
                if (returnedSequence == null) {
                    returnedSequence = new Sequence(symbol.toString(), alphabet);
                }
            } else {
                rewrittenSequence = rewrite(symbol);
                if (rewrittenSequence == null) {
                    rewrittenSequence = new Sequence(symbol.toString(), alphabet);
                }
                for (String s : rewrittenSequence.getSequenceList()) {
                    toAdd.add(s);
                }
            }
        }
        for (String s : toAdd) {
            returnedSequence.addSequence(s);
        }
        return returnedSequence;
    }

    //Validates files format
    private static void validateFile(JSONObject jsonObject) throws LSystemJsonException {
        if (!jsonObject.has("axiom")) {
            throw new LSystemJsonException("JSON file doesn't contain an axiom.");
        }
        if (!jsonObject.has("alphabet")) {
            throw new LSystemJsonException("JSON file doesn't contain an alphabet.");
        }
        if (!jsonObject.has("rules")) {
            throw new LSystemJsonException("JSON file doesn't contain rules.");
        }
        if (!jsonObject.has("actions")) {
            throw new LSystemJsonException("JSON file doesn't contain actions.");
        }
        if (!jsonObject.has("parameters")) {
            throw new LSystemJsonException("JSON file doesn't contain parameters.");
        }
    }

    //Validates axiom format
    private static void validateAxiom(String axiom, Map<Character, Symbol> alphabet) throws LSystemJsonException{
        if (axiom == null) {
            throw new LSystemJsonException("Sequence given for axiom given is not a string.");
        }
        else if (axiom.isEmpty()) {
            throw new LSystemJsonException("Axiom string is empty.");
        }
        for(char c : axiom.toCharArray()) {
            if (!alphabet.containsKey(c)) {
                throw new LSystemJsonException("Symbol given for action is not in alphabet.");
            }
        }
    }

    //Validates alphabet format
    private static void validateAlphabet(JSONArray alphabet) throws LSystemJsonException{
        if (alphabet == null) {
            throw new LSystemJsonException("Array given for alphabet is null.");
        }
        else if (alphabet.isEmpty()) {
            throw new LSystemJsonException("Alphabet array is empty.");
        }
    }

    //Validates rules format
    private static void validateRules(JSONObject rules, Map<Character, Symbol> alphabet) throws LSystemJsonException {

        if (rules == null) {
            throw new LSystemJsonException("Rules given for L-System is not a JSON Object.");
        }
        for (String key : rules.keySet()) {
            JSONArray arrayRule = rules.optJSONArray(key);
            if (key.length() != 1) {
                throw new LSystemJsonException("Invalid symbol format for rules.");
            }
            else if (!alphabet.containsKey(key.charAt(0))) {
                throw new LSystemJsonException("Symbol given for rule is not in alphabet.");
            }
            else if (arrayRule == null) {
                throw new LSystemJsonException("No array with symbol.");
            }
        }
    }

    private static void validateActions(JSONObject actions, Map<Character, Symbol> alphabet) throws LSystemJsonException {
        if (actions == null) {
            throw new LSystemJsonException("Actions given for L-System is not a JSON Object.");
        }
        for (String key : actions.keySet()) {
            String action = actions.optString(key);
            if (key.length() != 1) {
                throw new LSystemJsonException("Invalid symbol format for action.");
            }
            else if (!alphabet.containsKey(key.charAt(0))) {
                throw new LSystemJsonException("Symbol given for action is not in alphabet.");
            }
            else if (action == null) {
                throw new LSystemJsonException("No string with symbol.");
            }
        }
    }

    private static void validateParameters(JSONObject parameters) throws LSystemJsonException {
        Double step = parameters.optDouble("step");
        Double angle = parameters.optDouble("angle");
        JSONArray startCoordinate = parameters.optJSONArray("start");

        if (parameters == null) {
            throw new LSystemJsonException("Parameters given for L-System is not a JSON Object.");
        }
        else if (!parameters.has("step")) {
            throw new LSystemJsonException("Parameters doesn't contain steps.");
        }
        else if (step.isNaN()) {
            throw new LSystemJsonException("Step doesn't contain any value.");
        }
        else if (!parameters.has("angle")) {
            throw new LSystemJsonException("Parameters doesn't contain angle.");
        }
        else if (angle.isNaN()) {
            throw new LSystemJsonException("Angle doesn't contain any value.");
        }
        else if (!parameters.has("start")) {
            throw new LSystemJsonException("Parameters doesn't contain a start.");
        }
        else if (startCoordinate == null) {
            throw new LSystemJsonException("Start doesn't contain an array.");
        }
        for (int i = 0; i < startCoordinate.length(); i++) {
            Double point = startCoordinate.optDouble(i);
            if (point.isNaN()) {
                throw new LSystemJsonException("Invalid value in parameters start array.");
            }
        }
    }
}
