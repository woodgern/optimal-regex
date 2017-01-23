package com.woodgern.automata;

import com.woodgern.automata.DfaState;

/**
 * Created by njwoodge on 10/01/17.
 */
public class Transition {

        Object state;
        Character ch;

        public Transition(Object state, Character ch) {
            this.state = state;
            this.ch = ch;
        }

        public Object getState() {
            return state;
        }

        public Character getChar() {
            return ch;
        }

        public String toString() {
            return "To " + state + " on " + ch;
        }
}
