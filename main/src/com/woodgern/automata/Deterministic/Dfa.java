package com.woodgern.automata.Deterministic;

public class Dfa {

    private State startState;

    public Dfa(State startState) {
        this.startState = startState;
    }

    public boolean matches(String s) {
        State curState = startState;

        for(Character in : s.toCharArray()) {
            curState = curState.getState(in);
            if(curState == null) {
                return false;
            }
        }
        return curState.isEndState();
    }
}