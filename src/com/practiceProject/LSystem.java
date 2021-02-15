package com.practiceProject;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class LSystem extends AbstractLSystem{

    //Variables dans LSystem
    private Map<Character, Symbol> alphabet;
    private Map<Symbol,List<Symbol.Seq>> rules;

    //Initialisation du LSystem
    public LSystem() {
        alphabet = new HashMap<>();
        rules = new HashMap<>();
    }

    //Méthode pour ajouter un symbole dans alphabet du système
    @Override
    public Symbol addSymbol(char sym) {
        Symbol symbol = new Symbol(sym);
        alphabet.put(sym, symbol);
        return symbol;
    }


    @Override
    public void addRule(Symbol sym, String expansion) {
        Sequence sequence = new Sequence();
        sequence.getString(expansion);
        List<Symbol.Seq> symbolRule = new LinkedList<>();
        symbolRule = sequence.getList();
        rules.put(sym, symbolRule);
    }

    @Override
    public void setAction(Symbol sym, String action) {

    }

    @Override
    public void setAxiom(String str) {

    }

    @Override
    public Symbol.Seq getAxiom() {
        return null;
    }

    @Override
    public Symbol.Seq rewrite(Symbol sym) {
        return null;
    }

    @Override
    public void tell(Turtle turtle, Symbol.Seq seq) {

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
