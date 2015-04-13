package com.sionicmobile.ion.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sionicmobile.ion.models.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by TChwang on 4/8/2015.
 */
public class RestService extends Service {

    // Only for test
    static final int GET_REQ = 5;

    private HttpClient _httpClient;

    private HttpPost   _httpPost;

    private String     _endPoint;

    public static final String CLASS_TAG = RestService.class.getCanonicalName();

    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;

    private static final String BASE_URL = "testapi.ionloyalty.com";

    private LocalAppServiceBinder _binder = new LocalAppServiceBinder();


    public class LocalAppServiceBinder extends Binder {

        public RestService getService() {

            return RestService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return _binder;
    }

    private String getHash(String time, String url, String requestString) {

        //System.currentTimeMillis();

        String hashedvalue;
        /*
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = df.format(date);
        */


/*
        hashedvalue = time + url + requestString;
        hashedvalue = new String(hashedvalue.getBytes(Charset.forName("ASCII")), Charset.forName("UTF-8"));
        hashedvalue = hashedvalue.getBytes(Charset.forName("UTF-8")).toString();

        //hashedvalue = time + url;
        //InputStream is = new ByteArrayInputStream(hashedvalue.getBytes(Charset.forName("UTF-8")));

        return hashedvalue;
        //return is.toString(); */

        hashedvalue = /*secret +*/ time + url + requestString;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(hashedvalue.getBytes("UTF-8"));
            byte[] byteString = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < byteString.length; i++) {
                buffer.append(Integer.toString((byteString[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedvalue = buffer.toString();
        }
        catch (Exception e){

        }
        return hashedvalue;

    }

    private HttpClient getHttpClient() {

        Log.d(CLASS_TAG, "createHttpClient: ");
        checkType();

        _httpClient = new DefaultHttpClient();
        final HttpParams params = _httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);

        return _httpClient;
    }

    private String ionResponse() {

        return "";
    }

    public void checkType() {

        try {
            ApplicationInfo ai = getPackageManager()
                    .getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            _endPoint = bundle.getString(Constants.ION_DATA_SERVER_URL);

            Log.i(CLASS_TAG, "END POINT: " + _endPoint);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            getHttpClient();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void getLocationReq() {

        obtainIonRespData(GET_REQ);
    }

    private void get() throws Throwable {

    }


    private void post() throws Throwable {

        _httpPost.setHeader("content-type", "application/json");
    }

    public class SHA256  {
        //private static final Logger logger = LoggerFactory.getLogger(SHA256Hash.class);
        private final char[] HEX_ARRAY = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        private Semaphore semaphore = null;
        private MessageDigest messageDigest = null;

        public SHA256() {
            try {
                this.semaphore = new Semaphore(1);
                this.messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException var2) {
                //logger.error(var2.getMessage());
            }

        }


        public byte[] hash(byte[] data) {
            byte[] output = null;
            boolean acquired = false;

            try {
                acquired = this.semaphore.tryAcquire(1L, TimeUnit.MILLISECONDS);
                if(acquired) {
                    this.messageDigest.reset();
                    this.messageDigest.update(data);
                    output = this.messageDigest.digest();
                } else {
                    //logger.debug("hash() semaphore could not be acquired.  hash() failed.");
                }
            } catch (InterruptedException var9) {
                //logger.debug("hash() semaphore could not be acquired.  hash() failed.");
            } catch (Exception var10) {
                // logger.error("hash() failed.  Exception: " + var10.getMessage());
            } finally {
                if(acquired) {
                    this.semaphore.release();
                }

            }

            return output;
        }

        public String hash(String data) {
            byte[] inputArray = data.getBytes();
            byte[] outputArray = this.hash((byte[])inputArray);
            return outputArray == null?null:this.toHexString(outputArray);
        }

        public String toHexString(byte[] data) {
            char[] hexChars = new char[data.length * 2];

            for(int ix = 0; ix < data.length; ++ix) {
                int v = data[ix] & 255;
                hexChars[ix * 2] = HEX_ARRAY[v >>> 4];
                hexChars[ix * 2 + 1] = HEX_ARRAY[v & 15];
            }

            return new String(hexChars);
        }
    }

    public String secret = "e8ec60ba295b464e8589d3fe42c78831";
    public String device_Identifier = "b7088375-9fa6-4b5f-88c7-ee578e264e49";
    public String install_Identifier = "9dd805a8-f2e1-4e55-820c-e72128f0a7e2";

    public String hash(String time, String url, String requestString){
/*
        String hashedvalue = secret + device_Identifier + install_Identifier+ time + url + requestString;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(hashedvalue.getBytes("UTF-8"));
            byte[] byteString = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < byteString.length; i++) {
                buffer.append(Integer.toString((byteString[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedvalue = buffer.toString();
        }
        catch (Exception e){

        }
        return hashedvalue;*/
        requestString = new String(requestString.getBytes(Charset.forName("UTF-8")));

        String hashedvalue = secret + device_Identifier+ install_Identifier+ time + url + "?" + requestString;
        //String hashedvalue = requestString;

        //hashedvalue = new String(hashedvalue.getBytes(Charset.forName("UTF-8")));
        SHA256 sha256 = new SHA256();
        return sha256.hash(hashedvalue);

        //return "";
    }

    private void obtainIonRespData(final int request) {


        new Thread() {

            public void run() {

                String address = "https://testapi.ionloyalty.com/API/locations?id-list=2335,2,3";
                //String address = "https://testapi.ionloyalty.com/API";
                if (request == GET_REQ) {
                    String smallAddress =address.substring( address.indexOf("/API/") + 4);
                    String url = smallAddress.substring(0, smallAddress.indexOf("?"));
                    String requestString = smallAddress.substring(smallAddress.indexOf("?")+ 1);

                    //String requestString = address.substring( address.indexOf("?"), address.length() - 1);
                    long timestamp  = System.currentTimeMillis();
                    StringBuilder builder = new StringBuilder();
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    httpGet.setHeader("client-version", "4.0.0b0001");
                    httpGet.addHeader("client-platform", "HTC One X");
                    httpGet.addHeader("timestamp", new Long(timestamp).toString());
                    httpGet.addHeader("application_identifier", "083260fa-4343-4c5c-842c-164934f7eedf");
                    httpGet.addHeader("access-token", "72a7e760df2f4806813c71527211e92e");
                    httpGet.addHeader("hash",hash(new Long(timestamp).toString(), url, requestString));
                    httpGet.addHeader("timezone", "America/New_York");
                    httpGet.addHeader("Cache-Control", "no-cache");
                    try{
                        HttpResponse response = client.execute(httpGet);
                        StatusLine statusLine = response.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        if(statusCode == 200){
                            HttpEntity entity = response.getEntity();
                            InputStream content = entity.getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                            String line;
                            while((line = reader.readLine()) != null){
                                builder.append(line);
                            }
                        } else {
                            //Log.e(MainActivity.class.toString(), "Failedet JSON object");
                        }
                    }catch(ClientProtocolException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                } else {
                    try {
                        post();

                    } catch (SocketTimeoutException se) {

                        Log.d(CLASS_TAG, "timeoutexception");

                    } catch (SocketException e) {

                    } catch (UnknownHostException ue) {

                    } catch (Exception e) {

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
