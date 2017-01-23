package com.woodgern.automata;

public class Dfa {

    private DfaState startState;

    Dfa(DfaState startState) {
        this.startState = startState;
    }

    public boolean matches(String s) {
        DfaState curState = startState;

        for(Character in : s.toCharArray()) {
            curState = curState.getState(in);
            if(curState == null) {
                return false;
            }
        }
        return curState.isEndState();
    }
}