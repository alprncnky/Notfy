package com.denem.alperen.notfy;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences preferences;        // bazi bilgileri kaydet

    Button activate,whatsapp,twitter,instagram;
    ImageButton tick1,tick2,tick3;

    private String choosenApps="";  // which apps are the choosen

    private String onOff="";    // device is on or off read this string

    private String login_str="";    // fill this string with email adress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // bilgi kaydet
        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        activate = (Button)findViewById(R.id.activatebtn);

        whatsapp = (Button)findViewById(R.id.button5);
        instagram = (Button)findViewById(R.id.instabtn);
        twitter = (Button)findViewById(R.id.twitterbtn);

        tick1 = (ImageButton)findViewById(R.id.imageButton);
        tick2 = (ImageButton)findViewById(R.id.imageButton2);
        tick3 = (ImageButton)findViewById(R.id.imageButton3);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        navigationView1.setNavigationItemSelectedListener(this);


        // get the data of which apps are chosen and fill the strings
        readSettings();

        // select the previous ticks by looking to choosenApps string
        selectTicks();


        // nav_header_main deki bilgileri update et
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(login_str);  // name



        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onOff.equals("on"))
                {
                    // aciksa kapat
                    // butonu isik kapat
                    activate.setBackgroundResource(R.drawable.activatebtndraw);
                    activate.setText("Activate");
                    onOffpref("off");
                }
                else
                {
                    // kapalıysa ac
                    // butonu isik ac
                    activate.setBackgroundResource(R.drawable.deactivatebtndraw);
                    activate.setText("Deactivate");
                    onOffpref("on");
                }
            }
        });


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // button style degistir
                // kayit degistir
                if(choosenApps.contains("w"))
                {
                    //  secme islemini kaldir
                    tick1.setBackgroundResource(R.drawable.circlebtn);
                    removeChar('w');
                    savesets(); // save settings shared preferences
                }
                else
                {
                    // whatsapp i sec choosenApps stringne ekle
                    tick1.setBackgroundResource(R.drawable.circlebtntick);
                    choosenApps+="w";
                    savesets(); // save settings shared preferences
                }
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // button style degistir
                // kayit degistir
                if(choosenApps.contains("i"))
                {
                    //  secme islemini kaldir
                    tick2.setBackgroundResource(R.drawable.circlebtn);
                    removeChar('i');
                    savesets(); // save settings shared preferences
                }
                else
                {
                    // whatsapp i sec choosenApps stringne ekle
                    tick2.setBackgroundResource(R.drawable.circlebtntick);
                    choosenApps+="i";
                    savesets(); // save settings shared preferences
                }
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // button style degistir
                // kayit degistir
                if(choosenApps.contains("t"))
                {
                    //  secme islemini kaldir
                    tick3.setBackgroundResource(R.drawable.circlebtn);
                    removeChar('t');
                    savesets(); // save settings shared preferences
                }
                else
                {
                    // whatsapp i sec choosenApps stringne ekle
                    tick3.setBackgroundResource(R.drawable.circlebtntick);
                    choosenApps+="t";
                    savesets(); // save settings shared preferences
                }
            }
        });

    }


    // light the saved settings ticks
    public void selectTicks()
    {
        if(choosenApps.contains("w"))
            tick1.setBackgroundResource(R.drawable.circlebtntick);
        if(choosenApps.contains("i"))
            tick2.setBackgroundResource(R.drawable.circlebtntick);
        if(choosenApps.contains("t"))
            tick3.setBackgroundResource(R.drawable.circlebtntick);

        // activate button if deactivated change color to red
        if(onOff.equals("on")) {
            activate.setBackgroundResource(R.drawable.deactivatebtndraw);
            // change text too
            activate.setText("Deactivate");
        }
    }


    // this is for unselect the app tick
    public void removeChar(char karakter)
    {
        if(choosenApps.length()>1) {    // sadece suanki harften fazlası varsa
            String temp = "";
            for (int i = 0; i < choosenApps.length(); i++) {
                if(choosenApps.charAt(i)!=karakter)
                    temp += choosenApps.charAt(i);
            }
            choosenApps=temp;
        }
        else
        {
            choosenApps="e";    // empty
        }
    }

    // get the stored data of choosenApps and fill the strings
    public void readSettings()
    {
        // onOff
        try{
            SharedPreferences prfs = getSharedPreferences("SERVİCE_INFO_FILE", Context.MODE_PRIVATE);
            onOff = prfs.getString("which", "");
        }catch (Exception e)
        {
            onOff = "off";
        }

        // choosenApps
        try{
            SharedPreferences prfs = getSharedPreferences("STORE_SETTINGS", Context.MODE_PRIVATE);
            choosenApps = prfs.getString("settings", "");
        }catch (Exception e)
        {
            choosenApps = "e";  // empty this
        }

        // get login email
        try{
            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            login_str = prfs.getString("login", "");
        }catch(Exception e)
        {
            login_str= " ";
        }
    }


    // store the choosenApps string in the device
    public void savesets()
    {
        try {
            // delete stored choosenApps data
            SharedPreferences preferences = getSharedPreferences("STORE_SETTINGS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            // ekle yeniden
            editor.putString("settings",choosenApps);
            editor.apply();
        }catch (Exception e)
        {
            SharedPreferences preferences = getSharedPreferences("STORE_SETTINGS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("settings",choosenApps);
            editor.apply();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            // log out
            LogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LogOut()
    {
        // delete stored login data
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // delete stored onOff data
        SharedPreferences preferences2 = getSharedPreferences("SERVİCE_INFO_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();

        // delete stored choosenApps data
        SharedPreferences preferences3 = getSharedPreferences("STORE_SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.clear();
        editor3.apply();

        choosenApps="";
        onOff="";

        // go to Login activity
        final Intent login = new Intent(this,Login.class);
        startActivity(login);
        finish();

    }


    public void onOffpref(String data)
    {
        try {
            // delete stored login data
            SharedPreferences preferences = getSharedPreferences("SERVİCE_INFO_FILE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            // ekle yeniden
            editor.putString("which",data);
            editor.apply();
            onOff=data;
        }catch (Exception e)
        {
            System.out.println("********* onOff HATA **********");
            SharedPreferences preferences = getSharedPreferences("SERVİCE_INFO_FILE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("which","off");
            editor.apply();
            onOff=data;
        }

    }
}
