package com.tonnfccard.helpers;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_ARRAY_ELEMENTS_ARE_NOT_DIGITS;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_ARRAY_TO_MAKE_DIGITAL_STR_MUST_NOT_BE_EMPTY;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_MALFORMED_JSON_MSG;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_PIN_FORMAT_INCORRECT;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_PIN_LEN_INCORRECT;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_STRING_IS_NOT_ASCII;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_STRING_IS_NULL;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class StringHelperTest {
    private static final StringHelper STRING_HELPER = StringHelper.getInstance();

    @Test
    public void isHexStringTest() {
        assertFalse(STRING_HELPER.isHexString(null));
        assertFalse(STRING_HELPER.isHexString(""));
        assertFalse(STRING_HELPER.isHexString("aa0"));
        assertFalse(STRING_HELPER.isHexString("S0ASJH"));
        assertTrue(STRING_HELPER.isHexString("0000AAFF"));
        assertTrue(STRING_HELPER.isHexString("0000AbFf"));
    }

    @Test
    public void isNumericStringTest() {
        assertTrue(STRING_HELPER.isHexString("0123456789"));
        assertFalse(STRING_HELPER.isHexString("0123456789A"));
        assertFalse(STRING_HELPER.isHexString(null));
    }

    @Test
    public void pinToHexTest() {
        try {
            String s = STRING_HELPER.pinToHex(null);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_PIN_FORMAT_INCORRECT);
        }

        try {
            String s = STRING_HELPER.pinToHex("0123456789A");
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_PIN_FORMAT_INCORRECT);
        }

        try {
            String s = STRING_HELPER.pinToHex("33333");
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_PIN_LEN_INCORRECT);
        }

        try {
            String s = STRING_HELPER.pinToHex("222");
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_PIN_LEN_INCORRECT);
        }

        assertEquals(STRING_HELPER.pinToHex("3456"), "33343536");
        assertEquals(STRING_HELPER.pinToHex("5555"), "35353535");
    }

    @Test
    public void asciiToHexTest() {
        try {
            String s = STRING_HELPER.asciiToHex(null);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_STRING_IS_NULL);
        }

        try {
            String s = STRING_HELPER.asciiToHex("whÿ");
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_STRING_IS_NOT_ASCII);
        }

        assertEquals(STRING_HELPER.asciiToHex("12A"),"313241");
        assertEquals(STRING_HELPER.asciiToHex("123"), "313233");
        assertEquals(STRING_HELPER.asciiToHex("5555"), STRING_HELPER.pinToHex("5555"));
        assertEquals(STRING_HELPER.asciiToHex("3456"), STRING_HELPER.pinToHex("3456"));

        String t = new String(new byte[]{0, 1, 48, 2, 49});
        System.out.println(t);
        assertEquals(STRING_HELPER.asciiToHex(t), "0001300231");

    }

    @Test
    public void makeDigitalStringTest() {
        try {
            String s = STRING_HELPER.makeDigitalString(null);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_ARRAY_TO_MAKE_DIGITAL_STR_MUST_NOT_BE_EMPTY);
        }
        catch (Exception e) {
            fail();
        }

        try {
            String s = STRING_HELPER.makeDigitalString(new byte[0]);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_ARRAY_TO_MAKE_DIGITAL_STR_MUST_NOT_BE_EMPTY);
        }
        catch (Exception e) {
            fail();
        }

        try {
            String s = STRING_HELPER.makeDigitalString(new byte[]{10, 0});
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), ERROR_MSG_ARRAY_ELEMENTS_ARE_NOT_DIGITS);
        }
        catch (Exception e) {
            fail();
        }

        assertEquals(STRING_HELPER.makeDigitalString(new byte[]{9, 0, 3}), "903");


    }




}