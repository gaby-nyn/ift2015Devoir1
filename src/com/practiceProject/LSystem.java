package com.practiceProject;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class LSystem extends AbstractLSystem{

    //Variables dans LSystem
    private Map<Character, Symbol> alphabet;
    private Map<Symbol,List<Symbol.Seq>> rules;
    private Map<Symbol,String> actions;
    private Sequence axiom;

    //Initialisation du LSystem
    public LSystem() {
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
            case "turnL":
                turtle.turnL();
            case "turnR":
                turtle.turnR();
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
}
