package com.woodgern.initializer;

import org.junit.Test;
import org.junit.Assert;
import com.woodgern.automata.NonDeterministic.*;

public class RegularLangConverterTest {
    @Test
    public void testCreateNfa() {
        String testRegex = "(a|b)";
        Nfa built = RegularLangConverter.regexToNfa(testRegex);

        Assert.assertTrue(built.matches("a"));
    }
}