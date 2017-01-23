package com.woodgern.automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class Nfa {

    private NfaState startState;
    private Set<Character> alphabet;

    Nfa(NfaState startState) {
        this.startState = startState;
        alphabet = new HashSet<>();
    }

    public boolean matches(String s) {
        List<NfaState> curStates = new ArrayList<>();
        curStates.add(startState);
        List<NfaState> nextStates;

        for (Character in : s.toCharArray()) {
            closure(curStates);

            nextStates = move(curStates, in);
            if(nextStates.size() == 0) {
                return false;
            }
            curStates = new ArrayList<>(nextStates);
        }
        closure(curStates);
        return curStates.stream().anyMatch(state -> state.isEndState());
    }

    private List<NfaState> move(List<NfaState> curStates, Character in) {
        List<NfaState> reachable = new ArrayList<>();
        for(NfaState state : curStates) {
            reachable.addAll(state.getStates(in));
        }
        return reachable;
    }

    private List<NfaState> closure(List<NfaState> curStates) {
        List<NfaState> epsilonStates = new ArrayList<>();
        List<NfaState> nextStateTick = new ArrayList<>();
        List<NfaState> curStateTick = new ArrayList<>(curStates);
        do {
            nextStateTick.clear();
            for(NfaState state : curStateTick) {
                nextStateTick.addAll(state.getEpsilonStates());
            }
            nextStateTick = nextStateTick.stream().filter(state -> !epsilonStates.contains(state)).collect(Collectors.toList());
            epsilonStates.addAll(nextStateTick);
            curStateTick = new ArrayList<>(nextStateTick);
        } while (nextStateTick.size() != 0);
        curStates.addAll(epsilonStates);

        return  curStates;
    }

    NfaState getStartState() {
        return startState;
    }

    Set<Character> getAlphabet() {
        return alphabet;
    }

    void addToAlphabet(char c) {
        alphabet.add(c);
    }

    void combineAlphabets(Set<Character> a) {
        alphabet.addAll(a);
    }
}
