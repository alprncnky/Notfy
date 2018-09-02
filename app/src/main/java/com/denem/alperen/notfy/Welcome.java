package com.denem.alperen.notfy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    TextView goTxt;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                // timer icinde ekle bunu
                // duruma gore main yada login e git
                try {
                    // i>0 so dont go again in the if
                    if(i<1) {
                        if (NotificationManagerCompat.getEnabledListenerPackages(getApplicationContext()).contains(getApplicationContext().getPackageName())) {
                            //service is not enabled try to enabled by calling...
                            Intent login = new Intent(Welcome.this, Login.class);
                            startActivity(login);
                            finish();
                            i++;
                        }
                    }
                }catch (Exception e)
                { }
            }
        }, 0, 1500);


        goTxt = (TextView)findViewById(R.id.textView9);

        try {
            SharedPreferences preferences = getSharedPreferences("APP_USER_INFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("first", "active");
            editor.apply();
        }catch (Exception e)
        {}


        goTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to setting page
                getApplicationContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });


    }
}
