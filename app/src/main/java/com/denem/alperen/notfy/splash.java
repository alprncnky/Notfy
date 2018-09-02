package com.denem.alperen.notfy;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;

public class splash extends AppCompatActivity {

    private int choose;
    private String login_str=" ";
    private String first=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent main = new Intent(this,MainActivity.class);
        final Intent login = new Intent(this,Login.class);
        final Intent welcome = new Intent(this,Welcome.class);

        Thread timer = new Thread(){
            public void run() {
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    // duruma gore main yada login e git
                    if(choose==1)
                        startActivity(main);
                    if(choose==2)
                        startActivity(login);
                    if(choose==3)
                        getApplicationContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    if(choose==4)
                        startActivity(welcome);

                    finish();
                }
            }
        };
        timer.start();


        // if kontrol et kullanici login yapmismi
        // eger log out yapmamissa direk ana sayfaya aktar
        // degilse login sayfasina yolla
        // if main se  choose=1     if login se  choose=2

        try{
            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            login_str = prfs.getString("login", "");
        }catch(Exception e)
        {
            login_str= " ";
        }


        try{
            // fill the first string
            SharedPreferences preferences = getSharedPreferences("APP_USER_INFO", Context.MODE_PRIVATE);
            first = preferences.getString("first","");

        }catch (Exception e)
        {
            first=" ";
        }


        // login kayitlarda varsa yonlendir
        if(login_str.length()>5)
            choose=1;
        else
            choose=2;

        // izin verilmis mi diye kontrol et yonlendir
        if (!NotificationManagerCompat.getEnabledListenerPackages (getApplicationContext()).contains(getApplicationContext().getPackageName())) {
            //service is not enabled try to enabled by calling...
            choose=3;
        }

        // check the user open the app first time or what?
        if(first.length()<2)
            choose=4;
    }







}
