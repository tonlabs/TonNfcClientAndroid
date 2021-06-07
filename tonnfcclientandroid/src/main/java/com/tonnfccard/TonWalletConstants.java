package com.tonnfccard;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The most import constants are collected here.
 */

public final class TonWalletConstants {

    /**
     * Messages for json
     */
    public static final String SUCCESS_STATUS = "ok";
    public static final String FAIL_STATUS = "fail";
    public static final String DONE_MSG = "done";
    public static final String FALSE_MSG = "false";
    public static final String TRUE_MSG = "true";
    public static final String GENERATED_MSG = "generated";
    public static final String NOT_GENERATED_MSG = "not generated";
    public static final String HMAC_KEYS_DOES_NOT_FOUND_MSG = "HMAC-SHA256 keys are not found in Android keystore.";

    /**
     * Applet states
     */
    public static final byte INSTALLED_STATE = (byte) 0x07;
    public static final byte PERSONALIZED_STATE = (byte) 0x17;
    public static final byte WAITE_AUTHORIZATION_STATE = (byte) 0x27;
    public static final byte DELETE_KEY_FROM_KEYCHAIN_STATE = (byte) 0x37;
    public static final byte BLOCKED_STATE = (byte) 0x47;
    public static final String INSTALLED_STATE_MSG =  "TonWalletApplet is invalid (is not personalized)";
    public static final String PERSONALIZED_STATE_MSG = "TonWalletApplet is personalized.";
    public static final String WAITE_AUTHENTICATION_MSG =  "TonWalletApplet waits two-factor authentication.";
    public static final String DELETE_KEY_FROM_KEYCHAIN_MSG = "TonWalletApplet is personalized and waits finishing key deleting from keychain.";
    public static final String BLOCKED_MSG = "TonWalletApplet is blocked.";

    public static final Map<Byte, String> STATE_MAP =  new LinkedHashMap<Byte, String>() {{
        put(INSTALLED_STATE, INSTALLED_STATE_MSG);
        put(PERSONALIZED_STATE, PERSONALIZED_STATE_MSG);
        put(WAITE_AUTHORIZATION_STATE, WAITE_AUTHENTICATION_MSG);
        put(DELETE_KEY_FROM_KEYCHAIN_STATE, DELETE_KEY_FROM_KEYCHAIN_MSG);
        put(BLOCKED_STATE, BLOCKED_MSG);
    }};

    public final static String DEFAULT_SERIAL_NUMBER = "504394802433901126813236";
    public final static String EMPTY_SERIAL_NUMBER = "empty";

    // The size of data portion to send into card/to get from the card
    public static final short DATA_PORTION_MAX_SIZE = 128;

    // Length of ed25519 public key
    public static final byte PUBLIC_KEY_LEN = 32;

    // Length of ed25519 signature
    public static final byte SIG_LEN = 0x40;

    // Default PIN code "5555" is set for all cards
    public static final byte[] DEFAULT_PIN = new byte[]{0x35, 0x35, 0x35, 0x35};

    public static String DEFAULT_PIN_STR = "5555";

    // Size of card PIN code
    public static final byte PIN_SIZE = (byte) 4;

    // Maximum number of attempts to enter PIN, after achieving this limit the seed for ed25519 will be blocked
    public final static byte MAX_PIN_TRIES = (byte) 10;

    // The maximum size (in bytes) of data to be signed by ed25519 by functions verifyPinAndSignForDefaultHdPath, signForDefaultHdPath
    public static final short DATA_FOR_SIGNING_MAX_SIZE = (short) 189;

    // The maximum size (in bytes) of data to be signed by ed25519 by functions verifyPinAndSign, sign
    public static final short DATA_FOR_SIGNING_MAX_SIZE_FOR_CASE_WITH_PATH = (short) 178;

    // Size (in bytes) of SHA256 hash
    public final static byte SHA_HASH_SIZE = (short) 32;

    // Size (in bytes) of card's authentication password
    public static short PASSWORD_SIZE = 128;

    // Size (in bytes) of card's authentication initial vector
    public static short IV_SIZE = 16;

    // Size (in bytes) of card's authentication common secret
    public static final short COMMON_SECRET_SIZE = 32;

    // Size (in bytes) of sault generated by the card
    public static final byte SAULT_LENGTH = (short) 32;

    // Size (in bytes) of HMACSHA256 signture
    public final static short HMAC_SHA_SIG_SIZE = (short) 32;

    // Maximum number of keys that can be stored by card's keychain
    public static final short MAX_NUMBER_OF_KEYS_IN_KEYCHAIN = 1023;

    // Maximum size (in bytes) of key in card's keychain
    public static final short MAX_KEY_SIZE_IN_KEYCHAIN = 8192;

    // Total card's keychain internal storage size (in bytes)
    public static final short KEY_CHAIN_SIZE = 32767;

    // Maximum size (in bytes) of hdIndex represented as byte array, where each array element stores one decimal digit
    public final static short MAX_HD_INDEX_SIZE = (short) 10;

    // Maximum number of attempts to verify HMACSHA256 signature for APDU command, after achieving this limit applet will be blocked
    public static final short MAX_HMAC_FAIL_TRIES = 20;

    // Size (in bytes) of key index (in card keychain) represented as byte array
    public static final short KEYCHAIN_KEY_INDEX_LEN = 2;

    // The size of recovery data portion (in bytes) to send into card/to get from the card
    public static final short DATA_RECOVERY_PORTION_MAX_SIZE = 250;

    // The maximum size (in bytes) of recovery data
    public static final short RECOVERY_DATA_MAX_SIZE = 2048;

    // Size of card serial number
    public final static short SERIAL_NUMBER_SIZE = (short) 24;


    /**
     *  Json fields names
     */
    public static final String STATUS_FIELD = "status";
    public static final String ERROR_CODE_FIELD = "code";
    public static final String ERROR_TYPE_FIELD = "errorType";
    public static final String ERROR_TYPE_ID_FIELD = "errorTypeId";
    public static final String MESSAGE_FIELD = "message";
    public static final String CARD_INSTRUCTION_FIELD = "cardInstruction";
    public static final String APDU_FIELD = "apdu";
    public static final String SERIAl_NUMBERS_FIELD = "serial_number_field";

}
