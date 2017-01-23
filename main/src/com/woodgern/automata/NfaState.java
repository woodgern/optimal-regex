package com.woodgern.automata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class NfaState {

    private List<Transition> transitionList;
    private boolean isEndState;
    private String name;

    public NfaState(boolean isEndState, String name) {
        this.isEndState = isEndState;
        this.name = name;
        transitionList = new ArrayList<>();
    }

    public List<NfaState> getStates(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).map(t -> (NfaState)t.getState()).collect(Collectors.toList());
    }

    public List<NfaState> getEpsilonStates() {
        return transitionList.stream().filter(t -> t.getChar() == '\0').map(t -> (NfaState)t.getState()).collect(Collectors.toList());
    }

    public boolean isEndState() {
        return isEndState;
    }

    public void addTransition(Transition t) {
        transitionList.add(t);
    }

    public void setEndState(boolean state) {
        isEndState = state;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
