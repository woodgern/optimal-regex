package com.woodgern.initializer;

import com.woodgern.automata.Deterministic.Dfa;
import org.junit.Test;
import org.junit.Assert;
import com.woodgern.automata.NonDeterministic.*;

public class RegularLangConverterTest {
    @Test
    public void testCreateNfa() {
        String testRegex = "(abc|xyz)*";
        Nfa built = RegularLangConverter.regexToNfa(testRegex);
        Dfa converted = RegularLangConverter.nfaToDfa(built);

        boolean b = converted.matches("");
        Assert.assertTrue(b);
    }
}