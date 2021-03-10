package com.tonnfccard.smartcard;

import androidx.annotation.RestrictTo;

import com.tonnfccard.utils.ByteArrayUtil;

import java.util.HashMap;
import java.util.Map;

import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_APDU_DATA_FIELD_IS_NULL;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_LABEL_BYTES_SIZE_INCORRECT;
import static com.tonnfccard.helpers.ResponsesConstants.ERROR_MSG_PIN_BYTES_SIZE_INCORRECT;
import static com.tonnfccard.TonWalletConstants.PIN_SIZE;
import static com.tonnfccard.smartcard.CommonConstants.LE;
import static com.tonnfccard.smartcard.CommonConstants.SELECT_CLA;
import static com.tonnfccard.smartcard.CommonConstants.SELECT_INS;
import static com.tonnfccard.smartcard.CommonConstants.SELECT_P1;
import static com.tonnfccard.smartcard.CommonConstants.SELECT_P2;

/**
 * Here there are all objects representing APDU commands of CoinManager
 */

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class CoinManagerApduCommands {

    private final static ByteArrayUtil BYTE_ARRAY_HELPER = ByteArrayUtil.getInstance();

    public final static Integer LABEL_LENGTH = 32;

    public final static String POSITIVE_ROOT_KEY_STATUS = "5A";
    public final static String CURVE_TYPE_SUFFIX = "0102";

    public final static String GET_ROOT_KEY_STATUS_DATA = "DFFF028105";
    // public final static String GET_APPS_DATA = "DFFF028106";
    public final static String GET_PIN_RTL_DATA = "DFFF028102";
    public final static String GET_PIN_TLT_DATA = "DFFF028103";
    public final static String RESET_WALLET_DATA = "DFFE028205";
    public final static String GET_AVAILABLE_MEMORY_DATA = "DFFE028146";
    public final static String GET_APPLET_LIST_DATA = "DFFF028106";
    public final static String GET_SE_DATA = "DFFF028109";
    public final static String GET_CSN_DATA = "DFFF028101";
    public final static String GET_DEVICE_LABEL_DATA = "DFFF028104";
    public final static String CHANGE_PIN_DATA = "DFFE0D82040A";
    public final static String GENERATE_SEED_DATA = "DFFE08820305";
    public final static String SET_DEVICE_LABEL_DATA = "DFFE238104";

    public final static byte COIN_MANAGER_CLA = (byte) 0x80;
    public final static byte COIN_MANAGER_INS = (byte) 0xCB;
    public final static byte COIN_MANAGER_P1 = (byte) 0x80;
    public final static byte COIN_MANAGER_P2 = (byte) 0x00;


    public final static CAPDU SELECT_COIN_MANAGER_APDU = new CAPDU(SELECT_CLA, SELECT_INS, SELECT_P1, SELECT_P2, LE); // "00A40400"
    public final static CAPDU GET_ROOT_KEY_STATUS_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_ROOT_KEY_STATUS_DATA), LE); // "80CB800005DFFF028105"
    //public final static CAPDU GET_APPS_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1,  COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_APPS_DATA), LE); // "80CB800005DFFF028106"
    public final static CAPDU GET_PIN_RTL_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_PIN_RTL_DATA), LE); // "80CB800005DFFF028102" get remaining retry times of PIN
    public final static CAPDU GET_PIN_TLT_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_PIN_TLT_DATA), LE); // "80CB800005DFFF028103" get retry maximum times of PIN
    public final static CAPDU RESET_WALLET_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(RESET_WALLET_DATA), LE);
    public final static CAPDU GET_AVAILABLE_MEMORY_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_AVAILABLE_MEMORY_DATA), LE);
    public final static CAPDU GET_APPLET_LIST_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_APPLET_LIST_DATA), LE);
    public final static CAPDU GET_SE_VERSION_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_SE_DATA), LE);
    public final static CAPDU GET_CSN_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_CSN_DATA), LE);
    public final static CAPDU GET_DEVICE_LABEL_APDU = new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, BYTE_ARRAY_HELPER.bytes(GET_DEVICE_LABEL_DATA), LE);

    private static Map<String, String> coinManagerCommandsNames = new HashMap<>();

    public final static String GET_ROOT_KEY_STATUS_APDU_NAME = "GET_ROOT_KEY_STATUS";
    public final static String GET_APPS_APDU_NAME = "GET_APPS";
    public final static String GET_PIN_RTL_APDU_NAME = "GET_PIN_RTL";
    public final static String GET_PIN_TLT_APDU_NAME = "GET_PIN_TLT";
    public final static String RESET_WALLET_APDU_NAME = "RESET_WALLET";
    public final static String GET_AVAILABLE_MEMORY_APDU_NAME = "GET_AVAILABLE_MEMORY";
    public final static String GET_APPLET_LIST_APDU_NAME = "GET_APPLET_LIST";
    public final static String GET_SE_VERSION_APDU_NAME = "GET_SE_VERSION_APDU";
    public final static String GET_CSN_APDU_NAME = "GET_CSN_APDU";
    public final static String GET_DEVICE_LABEL_APDU_NAME = "GET_DEVICE_LABEL_APDU";
    public final static String CHANGE_PIN_APDU_NAME = "CHANGE_PIN";
    public final static String GENERATE_SEED_APDU_NAME = "GENERATE_SEED";
    public final static String SET_DEVICE_LABEL_APDU_NAME = "SET_DEVICE_LABEL";

    static {
        coinManagerCommandsNames.put(GET_ROOT_KEY_STATUS_DATA, GET_ROOT_KEY_STATUS_APDU_NAME);
        // coinManagerCommandsNames.put(GET_APPS_DATA, GET_APPS_APDU_NAME);
        coinManagerCommandsNames.put(GET_PIN_RTL_DATA, GET_PIN_RTL_APDU_NAME);
        coinManagerCommandsNames.put(GET_PIN_TLT_DATA, GET_PIN_TLT_APDU_NAME);
        coinManagerCommandsNames.put(RESET_WALLET_DATA, RESET_WALLET_APDU_NAME);
        coinManagerCommandsNames.put(GET_AVAILABLE_MEMORY_DATA, GET_AVAILABLE_MEMORY_APDU_NAME);
        coinManagerCommandsNames.put(GET_SE_DATA, GET_SE_VERSION_APDU_NAME);
        coinManagerCommandsNames.put(GET_CSN_DATA, GET_CSN_APDU_NAME);
        coinManagerCommandsNames.put(GET_DEVICE_LABEL_DATA, GET_DEVICE_LABEL_APDU_NAME);
        coinManagerCommandsNames.put(CHANGE_PIN_DATA, CHANGE_PIN_APDU_NAME);
        coinManagerCommandsNames.put(GENERATE_SEED_DATA, GENERATE_SEED_APDU_NAME);
        coinManagerCommandsNames.put(SET_DEVICE_LABEL_DATA, SET_DEVICE_LABEL_APDU_NAME);
        coinManagerCommandsNames.put(GET_APPLET_LIST_DATA, GET_APPLET_LIST_APDU_NAME);
    }


    // Get the name of CoinManager APDU command using its data field
    public static String getCoinManagerApduCommandName(String apduDataField) {
        if (apduDataField == null)
            throw new IllegalArgumentException(ERROR_MSG_APDU_DATA_FIELD_IS_NULL);
        for (String key : coinManagerCommandsNames.keySet()) {
            if (apduDataField.startsWith(key)) return coinManagerCommandsNames.get(key);
        }
        return null;
    }

    //Prepare CHANGE_PIN APDU command object
    //example: User entered oldpin=5555 and newpin=6666. Here we transform it into strings 35353535 and 36363636 and wrap into apdu command
    public static CAPDU getChangePinAPDU(byte[] oldPinBytes, byte[] newPinBytes) {
        checkPin(oldPinBytes);
        checkPin(newPinBytes);
        byte[] data = BYTE_ARRAY_HELPER.bConcat(BYTE_ARRAY_HELPER.bytes(CHANGE_PIN_DATA), new byte[]{PIN_SIZE}, oldPinBytes, new byte[]{PIN_SIZE}, newPinBytes);
        return new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, data, LE);
    }

    //Prepare GENERATE_SEED APDU command object
    //80CB8000DFFE08820305043535353500
    public static CAPDU getGenerateSeedAPDU(byte[] pinBytes) {
        checkPin(pinBytes);
        byte[] data = BYTE_ARRAY_HELPER.bConcat(BYTE_ARRAY_HELPER.bytes(GENERATE_SEED_DATA), new byte[]{PIN_SIZE}, pinBytes);
        return new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, data, LE);
    }

    //Prepare SET_DEVICE_LABEL APDU command object
    public static CAPDU getSetDeviceLabelAPDU(byte[] labelBytes) {
        if (labelBytes == null || labelBytes.length != LABEL_LENGTH) {
            throw new IllegalArgumentException(ERROR_MSG_LABEL_BYTES_SIZE_INCORRECT);
        }
        byte[] data = BYTE_ARRAY_HELPER.bConcat(BYTE_ARRAY_HELPER.bytes(SET_DEVICE_LABEL_DATA), new byte[]{0x20}, labelBytes);
        return new CAPDU(COIN_MANAGER_CLA, COIN_MANAGER_INS, COIN_MANAGER_P1, COIN_MANAGER_P2, data, LE);
    }

    private static void checkPin(byte[] pinBytes) {
        if (pinBytes == null || pinBytes.length != PIN_SIZE)
            throw new IllegalArgumentException(ERROR_MSG_PIN_BYTES_SIZE_INCORRECT);
    }
}
