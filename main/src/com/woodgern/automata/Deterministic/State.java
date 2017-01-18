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

    public State(boolean isEndState) {
        this.transitionList = new ArrayList<>();
        this.isEndState = isEndState;
    }

    public State getState(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).findAny().orElse(null).getState();
    }

    public boolean isEndState() {
        return isEndState;
    }

    public void addTransition(Transition t) {
        transitionList.add(t);
    }

    public void setisEndState(boolean state) {
        isEndState = state;
    }
}
