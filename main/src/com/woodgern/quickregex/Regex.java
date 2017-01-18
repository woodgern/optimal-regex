package com.woodgern.quickregex;

import com.woodgern.automata.Deterministic.Dfa;

/**
 * Created by njwoodge on 09/01/17.
 */
public class Regex {

    private Dfa matcher;

    public Regex(String regex) {
        this.initializeMatcher();
    }

    public boolean matches(String s) {
        return false;
    }

    private void initializeMatcher() {
        matcher = new Dfa(null);
    }
}
