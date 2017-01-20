package com.woodgern.automata.NonDeterministic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class Nfa {

    private State startState;

    public Nfa(State startState) {
        this.startState = startState;
    }

    public boolean matches(String s) {
        List<State> curStates = new ArrayList<>();
        curStates.add(startState);
        List<State> nextStates = new ArrayList<>();
        System.out.println("StartState: " + startState);
        System.out.println("Transitions: " + startState.getTransitionList());

        for (Character in : s.toCharArray()) {
            spinUpEpsilons(curStates);

            System.out.println("After epsilon tick: " + curStates);

            for(State state : curStates) {
                nextStates.addAll(state.getStates(in));
            }
            if(nextStates.size() == 0) {
                return false;
            }
            curStates = new ArrayList<>(nextStates);
            nextStates.clear();
            System.out.println("After char \'" + in + "\' tick: " + curStates);
        }
        spinUpEpsilons(curStates);
        System.out.println("At end: " + curStates);
        return curStates.stream().anyMatch(state -> state.isEndState());
    }

    private List<State> spinUpEpsilons(List<State> curStates) {
        List<State> epsilonStates = new ArrayList<>();
        List<State> nextStateTick = new ArrayList<>();
        List<State> curStateTick = new ArrayList<>(curStates);
        do {
            nextStateTick.clear();
            for(State state : curStateTick) {
                nextStateTick.addAll(state.getEpsilonStates());
            }
            nextStateTick = nextStateTick.stream().filter(state -> !epsilonStates.contains(state)).collect(Collectors.toList());
            epsilonStates.addAll(nextStateTick);
            curStateTick = new ArrayList<>(nextStateTick);
        } while (nextStateTick.size() != 0);
        curStates.addAll(epsilonStates);

        return  curStates;
    }

    public State getStartState() {
        return startState;
    }
}
