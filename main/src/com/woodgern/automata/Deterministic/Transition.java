package com.woodgern.automata.Deterministic;

/**
 * Created by njwoodge on 10/01/17.
 */
public class Transition {

        State state;
        Character ch;

        public Transition(State state, Character ch) {
            this.state = state;
            this.ch = ch;
        }

        public State getState() {
            return state;
        }

        public Character getChar() {
            return ch;
        }

        public String toString() {
            return "To " + state + " on " + ch;
        }
}
