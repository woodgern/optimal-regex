package com.woodgern.automata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by njwoodge on 10/01/17.
 */
public class DfaState {

    private List<Transition> transitionList;
    private boolean isEndState;
    private String name;

    public DfaState(boolean isEndState, String name) {
        this.transitionList = new ArrayList<>();
        this.isEndState = isEndState;
        this.name = name;
    }

    public DfaState getState(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).map(tr -> (DfaState)tr.getState()).findAny().orElse(null);
    }

    public boolean isEndState() {
        return isEndState;
    }

    public void addTransition(Transition t) {
        transitionList.add(t);
    }

    public void setIsEndState(boolean state) {
        isEndState = state;
    }

    public String toString() {
        return name;
    }
}
