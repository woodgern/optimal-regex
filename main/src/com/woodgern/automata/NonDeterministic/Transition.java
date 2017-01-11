package com.woodgern.automata.NonDeterministic;

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
}
