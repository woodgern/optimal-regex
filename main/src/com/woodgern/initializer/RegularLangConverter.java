package com.woodgern.initializer;

import com.woodgern.automata.NonDeterministic.Nfa;
import com.woodgern.automata.NonDeterministic.State;
import com.woodgern.automata.NonDeterministic.Transition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by njwoodge on 10/01/17.
 */
public class RegularLangConverter {
    static int stateCounter = 0;

    public static Nfa regexToNfa(java.lang.String regex) {
        NfaPair n = buildExpression(regex);
        return n.getNfa();
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
        exps.add(expression.substring(expStart, expression.length()));
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
                exps.add(expression.substring(expStart, i + 1));
                expStart = i + 1;
            }
        }
        exps.add(expression.substring(expStart, expression.length()));
        return exps;
    }

    private static NfaPair or(NfaPair s, NfaPair t) {
        System.out.println("OR");
        s.getEndState().setEndState(false);
        t.getEndState().setEndState(false);

        State newEnd = new State(true, stateCounter);stateCounter++;
        State newStart = new State(false, stateCounter);stateCounter++;

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
        System.out.println("AND");
        s.getEndState().setEndState(false);

        Transition tr = new Transition(t.getNfa().getStartState(), '\0');
        s.getNfa().getStartState().addTransition(tr);
        return new NfaPair(s.getNfa(), t.getEndState());
    }

    private static NfaPair kleenes(NfaPair s) {
        System.out.println("KLEENES");
        s.getEndState().setEndState(false);

        State startState = new State(false, stateCounter);stateCounter++;
        State endState = new State(true, stateCounter);stateCounter++;

        Transition t1 = new Transition(endState, '\0');
        Transition t2 = new Transition(s.getNfa().getStartState(), '\0');

        startState.addTransition(t1);
        startState.addTransition(t2);
        s.getEndState().addTransition(t1);
        s.getEndState().addTransition(t2);

        return new NfaPair(new Nfa(startState), endState);
    }

    private static NfaPair element(Character c) {
        System.out.println("ELEMENT: " + c);
        State start = new State(false, stateCounter);stateCounter++;
        State end = new State(true, stateCounter);stateCounter++;
        Transition t = new Transition(end, c);
        start.addTransition(t);

        return new NfaPair(new Nfa(start), end);
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
