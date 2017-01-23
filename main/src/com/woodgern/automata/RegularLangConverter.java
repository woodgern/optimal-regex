package com.woodgern.automata;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by njwoodge on 10/01/17.
 */
public class RegularLangConverter {

    private static int stateCounter = 0;

    public static Nfa getNfa(String regex) {
        NfaPair n = buildExpression(regex);
        return n.getNfa();
    }

    public static Dfa getDfa(String regex) {
        return getDfa(getNfa(regex));
    }

    public static Dfa getDfa(Nfa n) {
        Set<Character> alphabet = n.getAlphabet();
        List<TableEntry> table = new ArrayList<>();

        List<NfaState> init = new ArrayList<>();
        init.add(n.getStartState());
        List<NfaState> initialSet = closure(init);
        DfaState initial = toDfaState(initialSet);

        Optional<TableEntry> t0 = Optional.of(new TableEntry(initial, initialSet));
        do {
            TableEntry t = t0.get();
            t.mark();
            for(Character ch : alphabet) {
                List<NfaState> u = closure(move(t.getStates(), ch));
                DfaState dfaState = toDfaState(u);
                Optional<TableEntry> duplicateEntry = table.stream().filter(te -> te.getState().toString().equals(dfaState.toString())).findFirst();
                if (!dfaState.toString().equals("") && !duplicateEntry.isPresent()) {
                    TableEntry tn = new TableEntry(dfaState, u);
                    table.add(tn);
                }

                DfaState transitionState = dfaState;
                if (duplicateEntry.isPresent()) {
                    transitionState = duplicateEntry.get().getState();
                }

                if (!dfaState.toString().equals("")) {
                    com.woodgern.automata.Transition transition = new com.woodgern.automata.Transition(transitionState, ch);
                    t.getState().addTransition(transition);
                }
            }
        } while((t0 = table.stream().filter(te -> !te.isMarked()).findFirst()).isPresent());

        return new Dfa(initial);
    }

    // Private methods

    private static DfaState toDfaState(List<NfaState> states) {
        boolean isEndState = states.stream().anyMatch(st -> st.isEndState());
        String name = "";
        List<String> names = states.stream().map(st -> st.toString()).collect(Collectors.toList());
        names.sort(Comparator.naturalOrder());
        for(String st : names) {
            name += st + "|";
        }
        return new DfaState(isEndState, name);
    }

    private static List<NfaState> move(List<NfaState> curStates, Character in) {
        List<NfaState> reachable = new ArrayList<>();
        for(NfaState state : curStates) {
            reachable.addAll(state.getStates(in));
        }
        return reachable;
    }

    private static List<NfaState> closure(List<NfaState> curStates) {
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

    private static NfaPair buildExpression(String expression) {
        List<String> exps;
        if (expression.length() == 1 && !isForbidden(expression)) {
            return element(expression.charAt(0));
        } else if (expression.length() == 2 && expression.charAt(1) == '*' && !isForbidden(expression.substring(0, 1))) {
            return kleenes(buildExpression(expression.substring(0, 1)));
        } else if(expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == '*' && isParentheses(expression.substring(0, expression.length() - 1))) {
            return kleenes(buildExpression(expression.substring(1, expression.length() - 2)));
        } else if (expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')' && isParentheses(expression)) {
            return buildExpression(expression.substring(1, expression.length() - 1));
        } else if ((exps = parseByOr(expression)).size() > 1) {
            NfaPair aggregatedNfa = null;
            for (String exp : exps) {
                if(aggregatedNfa == null) {
                    aggregatedNfa = buildExpression(exp);
                } else {
                    aggregatedNfa = or(aggregatedNfa, buildExpression(exp));
                }
            }
            return aggregatedNfa;
        } else if ((exps = parseByAnd(expression)).size() > 1) {
            NfaPair aggregatedNfa = null;
            for (String exp : exps) {
                if(aggregatedNfa == null) {
                    aggregatedNfa = buildExpression(exp);
                } else {
                    aggregatedNfa = and(aggregatedNfa, buildExpression(exp));
                }
            }
            return aggregatedNfa;
        } else {
            return null;
            //ERROR IN SYNTAX. MAKE AN EXCEPTION FOR THIS OR USE A GOOD ONE
        }
    }

    private static boolean isForbidden(String expression) {
        List<Character> forbidden = new ArrayList<>(); //Make property file or something
        forbidden.add('|');
        forbidden.add('(');
        forbidden.add(')');
        forbidden.add('*');

        Character ch = expression.charAt(0);
        return forbidden.contains(ch);
    }

    private static boolean isParentheses(String expression) {
        int paren = 0;
        for (int i = 0; i < expression.length();i++) {
            Character ch = expression.charAt(i);
            if (ch == '(') {
                paren++;
            } else if (ch == ')') {
                paren--;
            }

            if (paren == 0 && i != expression.length() - 1) {
                return false;
            }
        }
        return paren == 0;
    }

    private static List<String> parseByOr(String expression) {
        List<String> exps = new ArrayList<>();
        int expStart = 0;
        for(int i = 0; i < expression.length();i++) {
            Character ch = expression.charAt(i);
            if(ch == '|') {
                exps.add(expression.substring(expStart, i));
                expStart = i + 1;
            } else if(ch == '(') {
                int paren = 1;
                for(i++;i < expression.length();i++) {
                    Character c = expression.charAt(i);
                    if (c == '(') {
                        paren++;
                    } else if (c == ')') {
                        paren--;
                    }
                    if(paren == 0) {
                        break;
                    }
                }

            }
        }
        if (expStart != expression.length()) {
            exps.add(expression.substring(expStart, expression.length()));
        }
        return exps;
    }

    private static List<String> parseByAnd(String expression) {
        List<String> exps = new ArrayList<>();
        int expStart = 0;
        for(int i = 0; i < expression.length();i++) {
            Character ch = expression.charAt(i);
            if(ch == '(') {
                int paren = 1;
                for(i++;i < expression.length();i++) {
                    Character c = expression.charAt(i);
                    if (c == '(') {
                        paren++;
                    } else if (c == ')') {
                        paren--;
                    }
                    if(paren == 0) {
                        break;
                    }
                }

            } else {
                if(i + 1 != expression.length() && expression.charAt(i + 1) == '*') {
                    exps.add(expression.substring(expStart, i + 2));
                    expStart = i + 2;
                    i++;
                } else {
                    exps.add(expression.substring(expStart, i + 1));
                    expStart = i + 1;
                }
            }
        }
        if (expStart != expression.length()) {
            exps.add(expression.substring(expStart, expression.length()));
        }
        return exps;
    }

    private static NfaPair or(NfaPair s, NfaPair t) {
        s.getEndState().setEndState(false);
        t.getEndState().setEndState(false);

        NfaState newEnd = new NfaState(true, String.valueOf(stateCounter++));
        NfaState newStart = new NfaState(false, String.valueOf(stateCounter++));

        Transition t1 = new Transition(newEnd, '\0');
        Transition t2 = new Transition(newEnd, '\0');
        s.getEndState().addTransition(t1);
        t.getEndState().addTransition(t2);

        Transition t3 = new Transition(s.getNfa().getStartState(), '\0');
        Transition t4 = new Transition(t.getNfa().getStartState(), '\0');
        newStart.addTransition(t3);
        newStart.addTransition(t4);

        Nfa n = new Nfa(newStart);
        n.combineAlphabets(s.getNfa().getAlphabet());
        n.combineAlphabets(t.getNfa().getAlphabet());
        return new NfaPair(n, newEnd);
    }

    private static NfaPair and(NfaPair s, NfaPair t) {
        s.getEndState().setEndState(false);

        Transition tr = new Transition(t.getNfa().getStartState(), '\0');
        s.getEndState().addTransition(tr);

        s.getNfa().combineAlphabets(t.getNfa().getAlphabet());
        return new NfaPair(s.getNfa(), t.getEndState());
    }

    private static NfaPair kleenes(NfaPair s) {
        s.getEndState().setEndState(false);

        NfaState startState = new NfaState(false, String.valueOf(stateCounter++));
        NfaState endState = new NfaState(true, String.valueOf(stateCounter++));

        Transition t1 = new Transition(endState, '\0');
        Transition t2 = new Transition(s.getNfa().getStartState(), '\0');

        startState.addTransition(t1);
        startState.addTransition(t2);
        s.getEndState().addTransition(t1);
        s.getEndState().addTransition(t2);

        Nfa n = new Nfa(startState);
        n.combineAlphabets(s.getNfa().getAlphabet());
        return new NfaPair(n, endState);
    }

    private static NfaPair element(Character c) {
        NfaState start = new NfaState(false, String.valueOf(stateCounter++));
        NfaState end = new NfaState(true, String.valueOf(stateCounter++));
        Transition t = new Transition(end, c);
        start.addTransition(t);

        Nfa n = new Nfa(start);
        n.addToAlphabet(c);
        return new NfaPair(n, end);
    }

    static class NfaPair {
        private Nfa nfa;
        private NfaState endState;
        NfaPair(Nfa nfa, NfaState endState) {
            this.nfa = nfa;
            this.endState = endState;
        }

        Nfa getNfa() {
            return nfa;
        }

        NfaState getEndState() {
            return endState;
        }
    }

    static class TableEntry {
        private DfaState state;
        private boolean isMarked = false;
        private List<NfaState> states;
        TableEntry(DfaState state, List<NfaState> states) {
            this.state = state;
            this.states = states;
        }

        DfaState getState() {
            return state;
        }

        boolean isMarked() {
            return isMarked;
        }

        void mark() {
            isMarked = true;
        }

        List<NfaState> getStates() {
            return states;
        }
    }
}
