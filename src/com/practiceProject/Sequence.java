package com.practiceProject;

import java.util.*;

public class Sequence implements Symbol.Seq {

    private String expansion;

    @Override
    public Iterator<Symbol> iterator() {

        List<Symbol> listeSequence = new LinkedList<>();
        char[] symbolsInExpansion = new char[expansion.length()];
        Iterator<Symbol> iterator;

        if(expansion.isEmpty()) {
            return null;
        }

        for(int i = 0; i < expansion.length(); i++) {

            symbolsInExpansion = expansion.toCharArray();
            listeSequence.add(new Symbol(symbolsInExpansion[i]));
        }
        iterator = listeSequence.iterator();
        return iterator;
    }

    @Override
    public void add(Symbol data) {

    }

    @Override
    public List<Symbol.Seq> getList() {
        List<Symbol.Seq> returnedList = new LinkedList<>();
        returnedList.add(this);
        return returnedList;
    }

    @Override
    public void getString(String s) {
        expansion = s;
    }

}
