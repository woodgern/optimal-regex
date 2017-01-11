package com.woodgern.automata.NonDeterministic;

import java.util.Arrays;
import java.util.List;

/**
 * Created by njwoodge on 10/01/17.
 */
public class Nfa {

    private State startState;

    public Nfa(State startState) {
        this.startState = startState;
    }

    public boolean matches(String s) {
        List<State> curStates = Arrays.asList(startState);

        for(Character in : s.toCharArray()) {
            for(State state : curStates) {
                curStates.remove(state);
                curStates.addAll(state.getStates(in));
            }
            if(curStates.size() == 0) {
                return false;
            }
        }
        return curStates.stream().anyMatch(state -> state.isEndState());
    }
}
