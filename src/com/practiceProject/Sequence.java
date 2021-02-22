package com.practiceProject;

import java.util.*;

public class Sequence implements Symbol.Seq {

    private List<String> sequences;
    private Map<Character, Symbol> alphabet;

    public Sequence(String sequence, Map<Character, Symbol> alphabet){
        this.sequences = new LinkedList<>();
        this.alphabet = new HashMap<>();
        this.sequences.add(sequence);
        this.alphabet = alphabet;
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
                listeSequence.add(alphabet.get(symbolsInSequence[i]));
            }
        }

        iterator = listeSequence.iterator();
        return iterator;
    }

    @Override
    public List<String> getSequenceList() {
        return this.sequences;
    }

    @Override
    public void addSequence(String sequence) {
        this.sequences.add(sequence);
    }

}
