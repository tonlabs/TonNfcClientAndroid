package com.tonnfccard;

import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;

import com.tonnfccard.helpers.ExceptionHelper;
import com.tonnfccard.helpers.HmacHelper;
import com.tonnfccard.helpers.JsonHelper;
import com.tonnfccard.helpers.StringHelper;
import com.tonnfccard.nfc.NfcApduRunner;
import com.tonnfccard.utils.ByteArrayUtil;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.security.KeyStore;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.tonnfccard.TonWalletConstants.SHA_HASH_SIZE;
import static com.tonnfccard.nfc.NfcApduRunner.TIME_OUT;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TonWalletApiTest {
    protected final ExceptionHelper EXCEPTION_HELPER = ExceptionHelper.getInstance();
    protected final StringHelper STRING_HELPER = StringHelper.getInstance();
    protected final ByteArrayUtil BYTE_ARRAY_HELPER = ByteArrayUtil.getInstance();
    protected final JsonHelper JSON_HELPER = JsonHelper.getInstance();
    protected static HmacHelper HMAC_HELPER = HmacHelper.getInstance();
    protected static boolean androidKeyStoreIsMocked = false;

    protected IsoDep prepareTagMock() throws Exception {
        IsoDep tag = mock(IsoDep.class);
        when(tag.isConnected()).thenReturn(false);
        Mockito.doNothing().when(tag).connect();
        Mockito.doNothing().when(tag).setTimeout(TIME_OUT);
        return tag;
    }

    protected NfcApduRunner prepareNfcApduRunnerMock(NfcApduRunner nfcApduRunner) {
        NfcAdapter nfcAdapterMock = mock(NfcAdapter.class);
        when(nfcAdapterMock.isEnabled()).thenReturn(true);
        NfcApduRunner nfcApduRunnerMock = Mockito.spy(nfcApduRunner);
        nfcApduRunnerMock.setNfcAdapter(nfcAdapterMock);
        return nfcApduRunnerMock;
    }

    protected void mockNfcAdapter(NfcApduRunner nfcApduRunner, boolean adapterEnabled) {
        NfcAdapter nfcAdapterMock = mock(NfcAdapter.class);
        when(nfcAdapterMock.isEnabled())
                .thenReturn(adapterEnabled);
        nfcApduRunner.setNfcAdapter(nfcAdapterMock);
    }

    protected void mockNfcAdapterToBeNull(NfcApduRunner nfcApduRunner) {
        MockedStatic<NfcAdapter> nfcAdapterMockedStatic = Mockito.mockStatic(NfcAdapter.class);
        nfcAdapterMockedStatic
                .when(() -> NfcAdapter.getDefaultAdapter(any()))
                .thenReturn(null);
        nfcApduRunner.setNfcAdapter(null);
    }

    protected HmacHelper prepareHmacHelperMock(HmacHelper hmacHelper) throws Exception{
        HmacHelper hmacHelperMock = Mockito.spy(hmacHelper);
        byte[] mac = BYTE_ARRAY_HELPER.bytes(STRING_HELPER.randomHexString(SHA_HASH_SIZE * 2));
        Mockito.doReturn(mac).when(hmacHelperMock).computeMac(any());
        System.out.println(BYTE_ARRAY_HELPER.hex(hmacHelperMock.computeMac(new byte[1])));
        return hmacHelperMock;
    }

    protected void mockAndroidKeyStore() throws Exception{
        if (androidKeyStoreIsMocked) return;
        KeyStore keyStoreMock = mock(KeyStore.class);
        MockedStatic<KeyStore> keyStoreMockStatic = Mockito.mockStatic(KeyStore.class);
        keyStoreMockStatic
                .when(() -> KeyStore.getInstance(any()))
                .thenReturn(keyStoreMock);
        Mockito.doNothing().when(keyStoreMock).load(any());
        Mockito.doNothing().when(keyStoreMock).setEntry(any(), any(), any());
        Mockito.doReturn(true).when(keyStoreMock).containsAlias(any());
        androidKeyStoreIsMocked = true;
    }

}