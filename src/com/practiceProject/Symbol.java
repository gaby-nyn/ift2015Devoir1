package com.practiceProject;
import java.util.Iterator;
import java.util.List;

public class Symbol {
    private final char value;
    public Symbol(char c)
    {
        this.value = c;
    }

    @Override
    public String toString()
    {
        return Character.toString(value);
    }

    /**
     * Common interface to a string of symbols.
     *
     */
    public interface Seq extends Iterable<Symbol> {

        @Override
        Iterator<Symbol> iterator();
    }
}
