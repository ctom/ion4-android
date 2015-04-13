package com.sionicmobile.ion.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;

import com.sionicmobile.ion.models.Constants;
import com.sionicmobile.ion.services.RestService;

/**
 * Created by TChwang on 4/9/2015.
 */
public class IonBaseActivity extends ActionBarActivity {

    protected Handler _handler;

    protected Resources _resource;

    protected RestService _restService;

    protected LayoutInflater _layoutInflater;

    protected boolean _restServiceBound = false;

    protected boolean _restServiceConnected = false;

    protected static final boolean DEBUG = false;

    protected static final boolean VERBOSE = false;

    protected static final String CLASS_TAG = IonBaseActivity.class.getCanonicalName();

    protected ServiceConnection _connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            if (DEBUG) {
                Log.d(CLASS_TAG, "Service_Connected");
            }
            _restService = ((RestService.LocalAppServiceBinder)service).getService();
            _restServiceConnected = true;
            //Message msg = _handler.obtainMessage(Constants.BIND_SERVICE_SUCCESS);
            //_handler.sendMessage(msg);
        }

        public void onServiceDisconnected(ComponentName className) {
            if (DEBUG) {
                Log.d(CLASS_TAG, "Service_Disconnected");
            }
            _restServiceConnected = false;
        }
    };


    /**
     * Called when the activity is first created
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG)
            Log.d(CLASS_TAG, "IonBaseActivity: onCreate ");

        _layoutInflater = this.getLayoutInflater();
        _resource = this.getResources();
    }

}
