package com.woodgern.automata;

import org.junit.Test;
import org.junit.Assert;

public class RegularLangConverterTest {
    @Test
    public void testCreateDfa() {
        String testRegex = "(abc|xyz)*";
        Dfa d = RegularLangConverter.getDfa(testRegex);

        boolean b = d.matches("abcxyzxyzabc");
        Assert.assertTrue(b);
    }
}