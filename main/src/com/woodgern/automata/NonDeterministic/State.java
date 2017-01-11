package com.woodgern.automata.NonDeterministic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class State {

    private List<Transition> transitionList;
    private boolean isEndState;

    public State(List<Transition> transitionList, boolean isEndState) {
        this.transitionList = transitionList;
        this.isEndState = isEndState;
    }

    public List<State> getStates(Character ch) {
        return transitionList.stream().filter(t -> t.getChar().equals(ch)).map(t -> t.getState()).collect(Collectors.toList());
    }

    public boolean isEndState() {
        return isEndState;
    }
}
