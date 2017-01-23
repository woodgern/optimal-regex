package com.woodgern.automata.Deterministic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class State {

    private List<Transition> transitionList;
    private boolean isEndState;
    private String name;

    public State(boolean isEndState, String name) {
        this.transitionList = new ArrayList<>();
        this.isEndState = isEndState;
        this.name = name;
    }

    public State getState(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).map(tr -> tr.getState()).findAny().orElse(null);
    }

    public boolean isEndState() {
        return isEndState;
    }

    public void addTransition(Transition t) {
        transitionList.add(t);
    }

    public List<Transition> getTransitions() {
        return transitionList;
    }

    public void setIsEndState(boolean state) {
        isEndState = state;
    }

    public String toString() {
        return name;
    }
}
