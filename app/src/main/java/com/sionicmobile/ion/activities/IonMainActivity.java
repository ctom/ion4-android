package com.sionicmobile.ion.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sionicmobile.ion.R;
import com.sionicmobile.ion.adapters.IonDrawerListAdapter;
import com.sionicmobile.ion.services.RestService;


public class IonMainActivity extends IonBaseActivity {

    String[] _drawerList;

    private IonDrawerListAdapter _drawerAdapter;

    private ListView _drawerView;

    private LinearLayout _drawer;

    private DrawerLayout _drawerLayout;

    private IonDrawerListAdapter _adapter;

    private ActionBarDrawerToggle _toggle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ion_main);

        _drawerList = getResources().getStringArray(R.array.ion_drawer_list);

        _drawer = (LinearLayout) findViewById(R.id.drawer);

        _adapter = new IonDrawerListAdapter(this, _drawerList);
        _drawerLayout = (DrawerLayout) findViewById(R.id.activity_ion_main_id);

        _restServiceBound = getApplicationContext().bindService(
                new Intent(this, RestService.class), _connection, Context.BIND_AUTO_CREATE);

        Button locationButton = (Button) this.findViewById(R.id.location_button);
        locationButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                Toast toast = Toast.makeText(IonMainActivity.this, "Location button is pressed", Toast.LENGTH_LONG);
                TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                if ( tv != null ) {
                    tv.setGravity(Gravity.CENTER);
                    tv.setWidth(400);
                }
                toast.show();

                _restService.getLocationReq();
            }
        });

        _toggle = new ActionBarDrawerToggle(this, _drawerLayout,
                R.drawable.ion_hamburger, R.string.drawer_open, R.string.drawer_close ) {

            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View view) {

            }
        };

        // Setting event listener for the drawer
        _drawerLayout.setDrawerListener(_toggle);

        _drawerView = (ListView) findViewById(R.id.drawer_list);
        //_drawerView.setVisibility(View.VISIBLE);

        _drawerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                _drawerLayout.closeDrawer(_drawer);
            }


        });


        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        _drawerView.setAdapter(_adapter);

        renderMenuTitle();


    }

    private void renderMenuTitle() {

        TextView tv = (TextView) this.findViewById(R.id.drawer_title_id);

        Spannable firstWord = new SpannableString("ANDREW" + "\n");
        firstWord.setSpan( new ForegroundColorSpan(Color.WHITE), 0, firstWord.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(firstWord);

        // Set font size and color
        Spannable secondWord = new SpannableString("choi@sionicmobile.com");
        secondWord.setSpan( new RelativeSizeSpan(0.8f), 0, secondWord.length(), 0);
        secondWord.setSpan( new ForegroundColorSpan(0xFF788F9C), 0, secondWord.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(secondWord);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceStates) {
        super.onPostCreate(savedInstanceStates);

        _toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ion_main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
