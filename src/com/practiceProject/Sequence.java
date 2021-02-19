package com.practiceProject;

import java.util.*;

public class Sequence implements Symbol.Seq {

    private String sequence;

    public Sequence(String sequence){
        this.sequence = sequence;
    }

    @Override
    public Iterator<Symbol> iterator() {

        List<Symbol> listeSequence = new LinkedList<>();
        char[] symbolsInSequence;
        Iterator<Symbol> iterator;

        if(sequence.isEmpty()) {
            return null;
        }

        for(int i = 0; i < sequence.length(); i++) {
            symbolsInSequence = sequence.toCharArray();
            listeSequence.add(new Symbol(symbolsInSequence[i]));
        }
        iterator = listeSequence.iterator();
        return iterator;
    }

}
