package com.woodgern.automata.Deterministic;

public class Dfa {

    private State startState;

    public Dfa(State startState) {
        this.startState = startState;
    }

    public boolean matches(String s) {
        State curState = startState;
        System.out.println("Start state: " + startState);
        System.out.println("Transitions: " + startState.getTransitions());
        for(Character in : s.toCharArray()) {
            curState = curState.getState(in);
            if(curState == null) {
                return false;
            }
            System.out.println("After \'" + in + "\' now in " + curState + " with transitions: " + curState.getTransitions());
        }
        return curState.isEndState();
    }
}