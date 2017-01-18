package com.woodgern.initializer;

import com.woodgern.automata.NonDeterministic.Nfa;
import com.woodgern.automata.NonDeterministic.State;
import com.woodgern.automata.NonDeterministic.Transition;

/**
 * Created by njwoodge on 10/01/17.
 */
public class RegularLangConverter {

    public static Nfa regexToNfa(java.lang.String regex) {
        NfaPair initial = element(regex.charAt(1));

        regex = regex.substring(1);

        Character lastCharacter = '\0';
        NfaPair recent = initial;
    }

    private static NfaPair RTNHelper(NfaPair pair, String expression) {

    }

    private static Expression getNextExpression(String expression) {
        for(Character ch : expression.toCharArray()) {
            if(ch == '|') {

            }
        }
    }

    private static int parseOutExpression(String expression) {
        int parenCount = 1;
        for(int i = 1; i < expression.length(); i++) {

        }
    }

    private static NfaPair or(NfaPair s, NfaPair t) {
        s.getEndState().setEndState(false);
        t.getEndState().setEndState(false);

        State newEnd = new State(true);
        State newStart = new State(false);

        Transition t1 = new Transition(newEnd, '\0');
        Transition t2 = new Transition(newEnd, '\0');
        s.getEndState().addTransition(t1);
        t.getEndState().addTransition(t2);

        Transition t3 = new Transition(s.getNfa().getStartState(), '\0');
        Transition t4 = new Transition(t.getNfa().getStartState(), '\0');
        newStart.addTransition(t3);
        newStart.addTransition(t4);

        return new NfaPair(new Nfa(newStart), newEnd);
    }

    private static NfaPair and(NfaPair s, NfaPair t) {
        s.getEndState().setEndState(false);

        Transition tr = new Transition(t.getNfa().getStartState(), '\0');
        s.getNfa().getStartState().addTransition(tr);
        return new NfaPair(s.getNfa(), t.getEndState());
    }

    private static NfaPair kleenes(NfaPair s) {
        s.getEndState().setEndState(false);

        State startState = new State(false);
        State endState = new State(true);

        Transition t1 = new Transition(endState, '\0');
        Transition t2 = new Transition(s.getNfa().getStartState(), '\0');

        startState.addTransition(t1);
        startState.addTransition(t2);
        s.getEndState().addTransition(t1);
        s.getEndState().addTransition(t2);

        return new NfaPair(new Nfa(startState), endState);
    }

    private static NfaPair element(Character c) {
        State start = new State(false);
        State end = new State(true);
        Transition t = new Transition(end, c);

        return new NfaPair(new Nfa(start), end);
    }

    static class Expression {
        private NfaPair result;
        private String remaining;
        private int type;
        Expression(NfaPair result, String remaining, int type) {
            this.result = result;
            this.remaining = remaining;
            this.type = type;
        }

        public NfaPair getResult() {
            return result;
        }

        public String getRemaining() {
            return remaining;
        }

        public int getType() {
            return type;
        }
    }

    static class NfaPair {
        private Nfa nfa;
        private State endState;
        NfaPair(Nfa nfa, State endState) {
            this.nfa = nfa;
            this.endState = endState;
        }

        public Nfa getNfa() {
            return nfa;
        }

        public State getEndState() {
            return endState;
        }
    }
}
