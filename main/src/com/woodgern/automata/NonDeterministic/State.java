package com.woodgern.automata.NonDeterministic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class State {

    private List<Transition> transitionList;
    private boolean isEndState;
    private int name;

    public State(boolean isEndState, int name) {
        this.isEndState = isEndState;
        this.name = name;
        transitionList = new ArrayList<>();
    }

    public List<State> getStates(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).map(t -> t.getState()).collect(Collectors.toList());
    }

    public List<State> getEpsilonStates() {
        return transitionList.stream().filter(t -> t.getChar() == '\0').map(t -> t.getState()).collect(Collectors.toList());
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

    public List<Transition> getTransitionList() {
        return transitionList;
    }

    public int getName() {
        return name;
    }

    public String toString() {
        return name + "";
    }
}
