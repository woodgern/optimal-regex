package com.woodgern.initializer;

import org.junit.Test;
import org.junit.Assert;
import com.woodgern.automata.NonDeterministic.*;

public class RegularLangConverterTest {
    @Test
    public void testCreateNfa() {
        String testRegex = "((aa*bb*)|(catsinthecradle))*";
        Nfa built = RegularLangConverter.regexToNfa(testRegex);

        boolean b = built.matches("aaaaaabbbbbcatsinthecradle");
        Assert.assertTrue(b);
    }
}