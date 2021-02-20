package com.practiceProject;

import java.util.*;

public class Sequence implements Symbol.Seq {

    private List<String> sequences;

    public Sequence(String sequence){
        this.sequences = new LinkedList<>();
        this.sequences.add(sequence);
    }

    @Override
    public Iterator<Symbol> iterator() {

        List<Symbol> listeSequence = new LinkedList<>();
        char[] symbolsInSequence;
        Iterator<Symbol> iterator;

        if(sequences.isEmpty()) {
            return null;
        }
        for(String s : sequences) {
            symbolsInSequence = s.toCharArray();
            for(int i = 0; i < s.length(); i++) {
                listeSequence.add(new Symbol(symbolsInSequence[i]));
            }
        }

        iterator = listeSequence.iterator();
        return iterator;
    }

    public List<String> getSequenceList() {
        return this.sequences;
    }

    public void addSequence(String sequence) {
        this.sequences.add(sequence);
    }

}
