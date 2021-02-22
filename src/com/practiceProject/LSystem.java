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
    public static LSystem readJsonFile(String file, Turtle turtle) throws FileNotFoundException {
        LSystem system = new LSystem();
        FileReader fileReader = new FileReader(file);
        JSONObject input = new JSONObject(new JSONTokener(fileReader));

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
            char verifInAlphabet = rules.keys().next().charAt(0);
            if(key == verifInAlphabet) {
                JSONArray rulesArray = rules.getJSONArray(String.valueOf(key));
                if (rulesArray != null) {
                    for (int i = 0; i < rulesArray.length(); i++) {
                        String rule = rulesArray.getString(i);
                        system.addRule(system.alphabet.get(key), rule);
                    }
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

        //Initialize turtle for L-System
        JSONObject parameters = input.getJSONObject("parameters");
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

    public Symbol.Seq iterateSequenceABC(Iterator<Symbol> iterator) {
        Symbol.Seq returnedSequence = null;
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



                if (returnedSequence == null) {
                    returnedSequence = new Sequence(symbol.toString(), alphabet);
                } else {
                    rewrittenSequence = (Sequence) rewrite(symbol);
                    for(String s : rewrittenSequence.getSequenceList()){
                        returnedSequence.addSequence(s);
                    }
                }
            }
        }
        return returnedSequence;
    }
}
