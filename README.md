# TonNfcClientAndroid

The library is developed to handle communication of Android smartphones with NFC TON Labs Security cards. It provides a useful API to work with all functionality (i.e. APDU commands) supported by NFC TON Labs Security card. The technical specification of TON Labs Security card can be found here https://ton.surf/scard.

## Installation

The library is published on  https://jitpack.io, see https://jitpack.io/#tonlabs/TonNfcClientAndroid.

Let's suppose you have an Android project. To use TonNfcClientAndroid library in the project you must go through the following steps.

+ Add it in your root build.gradle at the end of repositories.
```ruby
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

	
+ Add the dependency and replace here Tag by the necessary version.
```ruby
dependencies {
	implementation 'com.github.tonlabs:TonNfcClientAndroid:Tag'
}
```

+ You must take care of AndroidManifest.xml. It must contain NFC permission and special intent filter. Add the following snippets.

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="true" />
<intent-filter>
	<action android:name="android.nfc.action.NDEF_DISCOVERED" />
    	<action android:name="android.nfc.action.TECH_DISCOVERED" />
    	<action android:name="android.nfc.action.TAG_DISCOVERED" />
</intent-filter>
<meta-data android:name="android.nfc.action.TECH_DISCOVERED" android:resource="@xml/nfc_tech_filter" />
```

For this to work you must have an appropriate nfc_tech_filter.xml file in your xml subfolder (\app\src\main\res\xml). File nfc_tech_filter.xml must looks as follows.
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
	<tech-list>
		<tech>android.nfc.tech.IsoDep</tech>
        	<tech>android.nfc.tech.NfcA</tech>
    	</tech-list>
</resources>
```
		
To get the full picture of how AndroidManifest.xml should look like you may walk through the exemplary app inside https://github.com/tonlabs/TonNfcClientAndroid/tree/master/app/ .
_Note:_ minSdkVersion now is 24 to use the library.

## Usage (Simple example)

Let's suppose you want to work with NFC TON Labs security card in your MainActivity class. And you want to make a simple request to the card: return the maximum number of card's PIN tries. For this request there is a special APDU command supported by the card. And there is a corresponding function in TonNfcClientAndroid library sending it to the card and making postprocessing of card's response for you. To make it work you should add the following snippet.

```java
import com.tonnfccard.CardCoinManagerApi;
import com.tonnfccard.nfc.NfcApduRunner;

private CardCoinManagerApi cardCoinManagerNfcApi;
		
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(android.example.myapplication.R.layout.activity_main);
	...
	try {
		NfcApduRunner nfcApduRunner = NfcApduRunner.getInstance(getApplicationContext());
		cardCoinManagerNfcApi = new CardCoinManagerApi(getApplicationContext(),  nfcApduRunner);
	}
	catch (Exception e) {
		Log.e("TAG", "Error happened : " + e.getMessage());;
	}
	...
}
```

+ Also take care of onNewIntent method. It intercepts the intent created after NFC card (tag) connection. And you must extract the data about the tag from the intent. You need it to start work with the tag.

```java
@Override
public void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	try {
		if (cardCoinManagerNfcApi.setCardTag(intent)) {
			Toast.makeText(this, "NFC hardware touched", Toast.LENGTH_SHORT).show();
		}
	}
	catch (Exception e) {
		Log.e("TAG", "Error happened : " + e.getMessage());
	}
}
```
+ Finally make the request to the card. In this example we send it after pressing the button. So the activity for the button may look as follows.

```java
public void addListenerOnButton() {
	button = (Button) findViewById(android.example.myapplication.R.id.button1);
	button.setOnClickListener(new View.OnClickListener() {
		@Override
            	public void onClick(View arg0) {
			try {
                    		String json = cardCoinManagerNfcApi.getMaxPinTriesAndGetJson();
                    		Log.d("TAG", "Card response : " + json);
                	}
                	catch (Exception e) {
                    		e.printStackTrace();
                    		Log.e("TAG", "Error happened : " + e.getMessage());
                	}
            	}
        });
}
```

Here json variable contains the response from card wrapped into json of the following simple format: 
```		
{"message":"10","status":"ok"}
```
		
To get the full picture of how the simplest MainActivity may look like you may walk through the exemplary app inside https://github.com/tonlabs/TonNfcClientAndroid/tree/master/app/ .

## More about responses format

### Case of successful operation

In the case of successful operation with the card any function of TonNfcClientAndroid library always returns json string with two fields "message" and "status". "status" will contain "ok". In the field "message" you will find an expected payload. So jsons may look like this.

```
{"message":"done","status":"ok"}
{"message":"generated","status":"ok"}
{"message":"HMac key to sign APDU data is generated","status":"ok"}
{"message":"980133A56A59F3A59F174FD457EB97BE0E3BAD59E271E291C1859C74C795A83368FD8C7405BC37E1C4146F4D175CF36421BF6AD2AFF4329F5A6C6D772247ED03","status":"ok"}
	etc.
```

### Case of error

If some error happened then functions of TonNfcClientAndroid library produce error messages wrapped into json strings of special format. The structure of json depends on the  error class. There are two main classes of errors.

#### Applet (card) errors

It is the case when applet (installed on the card) threw some error status word (SW). So Android code just catches it and throws away. The exemplary error json looks like this.
```
{
	"message":"Incorrect PIN (from Ton wallet applet).",
	"status":"fail",
	"errorCode":"6F07",
	"errorTypeId":0,
	"errorType":"Applet fail: card operation error",
	"cardInstruction":"VERIFY_PIN",
	"apdu":"B0 A2 00 00 44 35353538EA579CD62F072B82DA55E9C780FCD0610F88F3FA1DD0858FEC1BB55D01A884738A94113A2D8852AB7B18FFCB9424B66F952A665BF737BEB79F216EEFC3A2EE37 FFFFFFFF "
}
```
Here:
+ *errorCode* — error status word (SW) produced by the card (applet)

+ *cardInstruction* — title of APDU command that failed

+ *errorTypeId* — id of error type ( it will always be zero here)

+ *errorType* — description of error type 

+ *message* — contains error message corresponding to errorCode thrown by the card.

+ *apdu* — full text of failed APDU command in hex format

#### Android errors

It is the case when error happened in Android code itself. The basic examples: troubles with NFC connection or incorrect format of input data passed into TonNfcClientAndroid library from the outside world. The exemplary error json looks like this.

```
{
	"errorType": "Native code fail: incorrect format of input data",
	"errorTypeId": "3",
	"errorCode": "30006",
	"message": "Pin must be a numeric string of length 4.",
	"status": "fail"
}
```	

In this [document](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/docs/ErrorList.md) you may find the full list of json error messages (and their full classification) that can be thrown by the library.

_Note:_ In above snippet in the case of any exception happened during work of cardCoinManagerNfcApi.getMaxPinTriesAndGetJson() we will come into catch block. Message inside exception e is always in json format. 	

### String format

The majority of input data passed into TonNfcClientAndroid library is represented by hex strings of even length > 0. These hex strings are naturally converted into byte arrays inside the library, like: "0A0A" → new byte[]{10, 10}. 

And also the payload produced by the card and wrapped into json responses is usually represented by hex strings of even length > 0.  For example, this is a response from getPublicKey function  returning ed25519 public key.

```
{"message":"B81F0E0E07316DAB6C320ECC6BF3DBA48A70101C5251CC31B1D8F831B36E9F2A","status":"ok"}
```

Here B81F0E0E07316DAB6C320ECC6BF3DBA48A70101C5251CC31B1D8F831B36E9F2A is a 32 bytes length ed25519 public key in hex format.

## Test work with the card

After you prepared the application, you may run it on your Android device (not simulator). Then you need to establish NFC connection. For this hold the card to the top of the smartphone (field near the camera) as close as possible. Usually smartphone vibrates after establishing a connection. And if you use above example, you must get the toast with the message "NFC hardware touched". It means that NFC connection is established. To keep connection alive you must not move the card and smartphone and they should have physical contact.

After NFC connection is ready we can send APDU command to the card. For above example push the button to make request getMaxPinTries. Check your Logcat console in Android Studio. You must find the following output:

```
===============================================================
===============================================================
>>> Send apdu  00 A4 04 00 
(SELECT_COIN_MANAGER)
SW1-SW2: 9000, No error., response data bytes: 	6F5C8408A000000151000000A...
===============================================================
===============================================================
>>> Send apdu  80 CB 80 00 05 DFFF028103 
(GET_PIN_TLT)
SW1-SW2: 9000, No error., response data bytes: 0A
Card response : {"message":"10","status":"ok"}
```

Here you see the log of APDU commands sent to the card and their responses in raw format. And in the end there is a final wrapped response.

## Card activation

When user gets NFC TON Labs security card  at the first time, the applet on the card is in a special state.  The main functionality of applet is blocked for now. Applet waits for user authentication. To pass authentication user must have three secret hex strings **authenticationPassword, commonSecret, initialVector**. The tuple **(authenticationPassword, commonSecret, initialVector)** is called _card activation data._  The user is going to get (using debots) his activation data from Tracking Smartcontract deployed for his security card.

At this step not only the card waits for user authentication. The user also authenticates the card by verification of some hashes.

*Note:* There is a bijection between serial number (SN) printed on the card and activation data.

The detailed info about card activation and related workflow is [here]().

For now let's suppose the user somehow got activation data into his application from debot (the details of working with debot will be given later). Then to activate the card he may use the following exemplary snippets.


```java
import com.tonnfccard.CardCoinManagerApi;
import com.tonnfccard.CardActivationApi;
import com.tonnfccard.nfc.NfcApduRunner;
import static com.tonnfccard.helpers.JsonHelper.*;
import static com.tonnfccard.helpers.ResponsesConstants.*;
import static com.tonnfccard.smartcard.TonWalletAppletConstants.*;
import org.json.JSONException;
import org.json.JSONObject;

private CardActivationApi cardActivationApi;
private CardCoinManagerApi cardCoinManagerNfcApi;
private static final String DEFAULT_PIN = "5555";
private static final String SERIAL_NUMBER = "504394802433901126813236";
private static final String COMMON_SECRET = "7256EFE7A77AFC7E9088266EF27A93CB01CD9432E0DB66D600745D506EE04AC4";
private static final String IV = "1A550F4B413D0E971C28293F9183EA8A";
private static final String PASSWORD  = "F4B072E1DF2DB7CF6CD0CD681EC5CD2D071458D278E6546763CBB4860F8082FE14418C8A8A55E2106CBC6CB1174F4BA6D827A26A2D205F99B7E00401DA4C15ACC943274B92258114B5E11C16DA64484034F93771547FBE60DA70E273E6BD64F8A4201A9913B386BCA55B6678CFD7E7E68A646A7543E9E439DD5B60B9615079FE";

@Override
protected void onCreate(Bundle savedInstanceState) {
	try {
		NfcApduRunner nfcApduRunner = NfcApduRunner.getInstance(getApplicationContext());
		cardCoinManagerNfcApi = new CardCoinManagerApi(getApplicationContext(),  nfcApduRunner);
		cardActivationApi = new CardActivationApi(getApplicationContext(),  nfcApduRunner);
	}
	catch (Exception e) {
	  	Log.e("TAG", e.getMessage());
	}
}
	
private String extractMessage(String jsonStr) throws JSONException { 
	JSONObject jObject = new JSONObject(jsonStr);
	return jObject.getString(MESSAGE_FIELD);
}
```
	
And use the following code to start card activation (for example add it as button action).
``` java    
try {
	String seedStatus = extractMessage(cardCoinManagerNfcApi.getRootKeyStatusAndGetJson());
        if (seedStatus.equals(NOT_GENERATED_MSG)) {
		cardCoinManagerNfcApi.generateSeedAndGetJson(DEFAULT_PIN); 
	}
		
	String appletState = extractMessage(cardActivationApi.selectTonWalletAppletAndGetTonAppletStateAndGetJson());	
	if (!appletState.equals(WAITE_AUTHORIZATION_MSG)) {
		throw new Exception("Incorret applet state : " + appletState);
	}
	String hashOfEncryptedCommonSecret = extractMessage(cardActivationApi.getHashOfEncryptedCommonSecretAndGetJson());
	String hashOfEncryptedPassword = extractMessage(cardActivationApi.getHashOfEncryptedPasswordAndGetJson());
		
	String newPin = "7777";
	appletState = extractMessage(cardActivationApi.turnOnWalletAndGetJson(newPin, PASSWORD, COMMON_SECRET, IV));
	Log.d("TAG", "Card response (state) : " + appletState);
		
	if (!appletState.equals(PERSONALIZED_STATE_MSG)) {
		throw new Exception("Incorrect applet state after activation : " + appletState);
	}
}
catch (Exception e) {
	Log.e("TAG", "Error happened : " + e.getMessage());
}      
```	
	
    
## About applet states and provided functionality

Applet installed onto NFC TON Labs security card may be in the one of the following states (modes):

1. TonWalletApplet waits two-factor authorization.
2. TonWalletApplet is personalized.
3. TonWalletApplet is blocked.
4. TonWalletApplet is personalized and waits finishing key deleting from keychain.

**Some details of states transitions:**

- When user gets the card at the first time, applet must be in the state 1 (see previous section).
- If user would try to pass incorrect activation data more than 20 times, then applet state will be switched on state 3. And this is irreversable. In this state all functionality of applet is blocked and one may call only getTonAppletState and getSerialNumber (see the below section Full functions list for more details).
- After correct activation (≤ 20 attempts to pass activation data) applet goes into state 2. And after this one can not go back to state 1. State 1 becomes unreachable. And at state 2 the main applet functionality is available.
- If user started operation of deleting a key from card's keychain, then applet is switched on state 4. And it stays in this state until the current delete operation will not be finished. After correct finishing of delete operation applet goes back into state 2. The other way to go back into state 2 is to call resetKeychain function (see the details below).
- Applet in states 2, 4 may be switched into state 3 in the case if HMAC SHA256 signature verification was failed by applet 20 times successively (more details below).

The functionality provided by NFC TON Labs security card can be divided into several groups.

- Module for card activation (available in state 1).
- Crypto module providing ed22519 signature  (available in states 2, 4).
- Module for maintaining recovery data  (available in states 2, 4).
- Keychain module  (available in states 2, 4).
- CoinManager module providing some auxiliary functions (available in any state).

## Protection against MITM

We protect the most critical card operations (APDU commands) against MITM attack by HMAC SHA256 signature. In this case the data field of such APDU is extended by 32-bytes sault generated by the card and the final byte array is signed. The obtained signature is added to the end of APDU data, i.e. its data field has the structure: payload || sault ||  sign(payload || sault). When the card gets such APDU, first it verifies sault and signature.  

The secret key for HMAC SHA256 is produced based on card activation data (see above section). This key is saved into Android keystore under alias "hmac_key_alias_SN" (SN is replaced by serial number printed on the card) and then it is used by the app to sign APDU commands data fields. Usually after correct card activation in the app (call of cardActivationApi.turnOnWalletAndGetJson) this key is produced and saved into keystore. So no extra code is required.

Another situation is possible. Let's suppose you activated the card earlier. After that you reinstalled the app working with NFC TON Labs security card or you started using new Android device. Then Android keystore does not have the key to sign APDU commands. So you must create it.

```java
cardCryptoApi.createKeyForHmacAndGetJson(authenticationPassword, commonSecret, serialNumber));
```

You may work with multiple NFC TON Labs security cards. In this case in your Android keystore there is a bunch of keys. Each keys is marked by corresponding SN. And you can get the list of serial numbers for which you have the key in keystore

The list of operations protected by HMAC SHA256:

- verifyPin, signForDefaultHdPath, sign
- all functions related to card keychain
	
	
## Request ED25519 signature

The basic functionality provided by NFC TON Labs security card is Ed25519 signature. You may request public key and request the signature for some message.

```java
nfcApduRunner = NfcApduRunner.getInstance(getApplicationContext());
CardCryptoApi cardCryptoApi = new CardCryptoApi(getApplicationContext(), nfcApduRunner);
String hdInd = "1";
String response = cardCryptoApi.getPublicKeyAndGetJson(hdInd);
String msg = "A10D";
String pin = "5555";
String response = cardCryptoApi.verifyPinAndSignAndGetJson(msg, hdInd, pin);
```

_Note:_ Functions signForDefaultHdPath, sign are protected by HMAC SHA256 signature (see previous section). But also there is an additional protection for them by PIN code. You have 10 attempts to enter PIN, after 10th fail you will not be able to use existing seed (keys for ed25519) . The only way to unblock these functions is to reset the seed (see resetWallet function) and generate new seed (see generateSeed). After resetting the seed PIN will be also reset to default value 5555.

## Card keychain

Inside NFC TON Labs security card we implemented small flexible independent keychain. It allows to store some user's keys and secrets. The maximum number of keys is 1023, maximum key size — 8192 bytes and the total available volume of storage — 32767 bytes.

Each key has its unique id. This is its HMAC SHA256 signature created using the key elaborated based on card activation data. So id is a hex a string of length 64.

The below snippet demonstrates the work with the keychain. We add one key, then retrieve it from the card. Then we replace it by a new key of the same key. At the end we delete the key.

_Note:_ This test is quite long working. So take care of your NFC connection. To keep it alive your screen must not go out. You may increase timeout for your Android device to achieve this.

```java
import com.tonnfccard.CardKeyChainApi;

private CardKeyChainApi cardKeyChainApi;

@Override
protected void onCreate(Bundle savedInstanceState) {
	try {
		Context activity = getApplicationContext();
		NfcApduRunner nfcApduRunner = NfcApduRunner.getInstance(activity);
		cardKeyChainApi = new CardKeyChainApi(activity,  nfcApduRunner);
	}
	catch (Exception e) {
		Log.e("TAG", "Error happened : " + e.getMessage());
	}
}
```

And use the following code to test work with keychain.
```java
try {
	String status = cardCryptoApi.createKeyForHmacAndGetJson(PASSWORD, COMMON_SECRET, SERIAL_NUMBER);
        Log.d("TAG", "status : " + status);
	String response = cardKeyChainApi.resetKeyChainAndGetJson();
        Log.d("TAG", "resetKeyChain response : " + response);
	response = cardKeyChainApi.getKeyChainInfoAndGetJson();
	Log.d("TAG", "getKeyChainInfo response : " + response);
        String keyInHex = "001122334455";
	response = cardKeyChainApi.addKeyIntoKeyChainAndGetJson(keyInHex);
       	Log.d("TAG", "addKeyIntoKeyChain response : " + response);
	String keyHmac = extractMessage(response);
       	Log.d("TAG", "keyHmac : " + response);
	response = cardKeyChainApi.getKeyChainInfoAndGetJson();
        Log.d("TAG", "getKeyChainInfo response : " + response);
	response = cardKeyChainApi.getKeyFromKeyChainAndGetJson(keyHmac);
        String keyFromCard = extractMessage(response);
        Log.d("TAG", "keyFromCard : " + response);
	if (!keyInHex.equals(keyFromCard)) {
		throw  new Exception("Bad key from card : " + keyFromCard);
        }
	String newKeyInHex = "00AA22334466";
        response = cardKeyChainApi.changeKeyInKeyChainAndGetJson(newKeyInHex, keyHmac);
        Log.d("TAG", "changeKeyInKeyChain response : " + response);
        String newKeyHmac = extractMessage(response);
	response = cardKeyChainApi.getKeyChainInfoAndGetJson();
        Log.d("TAG", "getKeyChainInfo response : " + response);
	response = cardKeyChainApi.getKeyFromKeyChainAndGetJson(newKeyHmac);
        String newKeyFromCard = extractMessage(response);
        Log.d("TAG", "keyFromCard : " + response);
        if (!newKeyInHex.equals(newKeyFromCard)) {
		throw  new Exception("Bad key from card : " + newKeyFromCard);
        }
        response = cardKeyChainApi.deleteKeyFromKeyChainAndGetJson(newKeyHmac);
        Log.d("TAG", "deleteKeyFromKeyChain response : " + response);
	response = cardKeyChainApi.getKeyChainInfoAndGetJson();
        Log.d("TAG", "getKeyChainInfo response : " + response);
	JSONObject jObject = new JSONObject(response);
        Integer num  =  Integer.parseInt(jObject.getString(NUMBER_OF_KEYS_FIELD));	
        if (num != 0) {
		throw  new Exception("Bad number of keys : " + num);
        }
}
catch (Exception e) {
        Log.e("TAG", "Error happened : " + e.getMessage());
}
```	
## Recovery module

This module is to store/maintain the data for recovering service: multisignature wallet address (hex string of length 64), TON Labs Surf public key (hex string of length 64) and part of card's activation data: authenticationPassword (hex string of length 256), commonSecret(hex string of length 64). This data will allow to recover access to multisignature wallet in the case when user has lost Android device with installed Surf application and also a seed phrase for Surf account. More details about recovery service can be found here.

There is an snippet demonstrating the structure of recovery data and the way of adding it into NFC TON Labs security card.

```java
import com.tonnfccard.RecoveryDataApi;
import com.tonnfccard.nfc.NfcApduRunner;
import com.tonnfccard.utils.ByteArrayUtil;

private static final int AES_KEY_SIZE = 128; // in bits
private static final int AES_COUNTER_SIZE = 16; // in bytes
private static final String SURF_PUBLIC_KEY = "B81F0E0E07416DAB6C320ECC6BF3DBA48A70101C5251CC31B1D8F831B36E9F2A";
private static final String MULTISIG_ADDR = "A11F0E0E07416DAB6C320ECC6BF3DBA48A70121C5251CC31B1D8F8A1B36E0F2F";

private RecoveryDataApi recoveryDataApi;
private SecureRandom sr = new SecureRandom();
private KeyGenerator kg;
private SecretKey key;
private byte[] counter = new byte[AES_COUNTER_SIZE];
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	try {
		Context activity = getApplicationContext();
		NfcApduRunner nfcApduRunner = NfcApduRunner.getInstance(activity);
		recoveryDataApi = new RecoveryDataApi(activity,  nfcApduRunner);
		kg = KeyGenerator.getInstance("AES");
		kg.init(AES_KEY_SIZE);
		key = kg.generateKey();
	}
	catch (Exception e) {
		Log.e("TAG", e.getMessage());
	}
}
```
And use the following code to test recovery data adding (for example add it as button action).

```java
 try {
        String response = recoveryDataApi.resetRecoveryDataAndGetJson();
        Log.d("TAG", "resetRecoveryData response : " + response);
	response = recoveryDataApi.isRecoveryDataSetAndGetJson();
        Log.d("TAG", "isRecoveryDataSet response : " + response);
        JSONObject recoveryData = new JSONObject();
        recoveryData.put("surfPublicKey", SURF_PUBLIC_KEY);
        recoveryData.put("multisigAddress", MULTISIG_ADDR);
	recoveryData.put("p1", PASSWORD);
        recoveryData.put("cs", COMMON_SECRET);
	byte[] recoveryDataBytes = recoveryData.toString().getBytes(StandardCharsets.UTF_8);
	Cipher aesCtr = Cipher.getInstance("AES/CTR/NoPadding");
	sr.nextBytes(counter);
	aesCtr.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(counter));
	byte[] encryptedRecoveryDataBytes = aesCtr.doFinal(recoveryDataBytes);
	String encryptedRecoveryDataHex = ByteArrayHelper.getInstance().hex(encryptedRecoveryDataBytes);
	response = recoveryDataApi.addRecoveryDataAndGetJson(encryptedRecoveryDataHex );
        Log.d("TAG", "addRecoveryData response : " + response);
	response = recoveryDataApi.isRecoveryDataSetAndGetJson();
        Log.d("TAG", "isRecoveryDataSet response : " + response);
 }
 catch (Exception e) {
        Log.e("TAG", "Error happened : " + e.getMessage());
 }
 ```
 
 There is an exemplary short code snippet demonstrating the way of getting recovery data from the card.
 ```java
 try {
	String response = recoveryDataApi.isRecoveryDataSetAndGetJson();
	Log.d("TAG", "isRecoveryDataSet response : " + response);
	String status = extractMessage(response);
	if (status.equals("false")) {
		Log.d("TAG", "Recovery data is no set yet.");
		return;
	}
	response = recoveryDataApi.getRecoveryDataAndGetJson();
	Log.d("TAG", "getRecoveryData response : " + response);
	String encryptedRecoveryDataHex = extractMessage(response);
	byte[] encryptedRecoveryDataBytes = ByteArrayHelper.getInstance().bytes(encryptedRecoveryDataHex);	
	Cipher aesCtr = Cipher.getInstance("AES/CTR/NoPadding");
	aesCtr.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(counter));
	byte[] recoveryDataBytes = aesCtr.doFinal(encryptedRecoveryDataBytes);
	String recoveryData = new String(recoveryDataBytes, StandardCharsets.UTF_8);
	Log.d("TAG", "Got recoveryData from card : " + recoveryData);
}
catch (Exception e) {
	Log.e("TAG", "Error happened : " + e.getMessage());
}
```

## About NfcCallback	

For each card operation now there is a pair of functions. The first one returns json response or throws a exception containing json error message. The second function does the same work, but it puts json response/json error message into callback. For this we defined NfcCallback.

```java
public class NfcCallback {
  	private NfcResolver resolve;
  	private NfcRejecter reject;
  	public NfcCallback(NfcResolver resolve, NfcRejecter reject) {
    		set(resolve, reject);
  	}
}

@FunctionalInterface
public interface NfcRejecter {
  	void reject(String errorMsg);
}

@FunctionalInterface
public interface NfcResolver {
  	void resolve(Object value);
}
```

To use you must override NfcRejecter and NfcResolver interfaces.

For example let's look at operation getMaxPinTries. Previously we tried it already. There are two functions for it.

```java
public String getMaxPinTriesAndGetJson() throws Exception
public void getMaxPinTries(final NfcCallback callback)
```
	
Example of work with NfcCallback.
```java
import com.facebook.react.bridge.Promise;
...
cardCoinManagerNfcApi.getMaxPinTries(NfcCallback(promise::resolve, promise::reject));
```
	
## Full functions list 

The full list of functions provided by the library to communicate with the card you will find [here](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/docs/FuntionsList.md)

## Auxiliary classes

TonNfcClientAndroid provides also additional entities. 
+ In [TonWalletConstants](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/tonnfcclientandroid/src/main/java/com/tonnfccard/TonWalletConstants.java) one may find the list of all constants required for work. 
 + Class [ByteArrayUtil](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/tonnfcclientandroid/src/main/java/com/tonnfccard/utils/ByteArrayUtil.java) provides functions to handle byte arrays, hex representations of byte arrays and integer numbers. It is to simplify the work with the main API. 
 + Class [NfcApduRunner](https://github.com/tonlabs/TonNfcClientAndroid/tree/master/tonnfcclientandroid/src/main/java/com/tonnfccard/nfc) provides functionality to connect NFC smart card and send arbitrary APDU to it. You can play with it, but normally you should not use it to work with TON Wallet functionality. Use functions from classes with ending 'Api' to communicate with the card correctly and get well formed json responses.
 + Class wrappers for APDU command [CAPDU](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/tonnfcclientandroid/src/main/java/com/tonnfccard/smartcard/CAPDU.java) and its response [RAPDU](https://github.com/tonlabs/TonNfcClientAndroid/blob/master/tonnfcclientandroid/src/main/java/com/tonnfccard/smartcard/RAPDU.java). They are to play with NfcApduRunner.



