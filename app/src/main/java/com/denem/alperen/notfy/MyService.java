package com.denem.alperen.notfy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.sql.SQLOutput;

public class MyService extends NotificationListenerService {

    private String output="";
    private String login_str=" ";
    private String onOff=" ";   // service "on" or "off"
    private String choosenApps="";
    private String packchar=" ";

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("********* SERVICE STARTED ***********");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        System.out.println("***** notification *****");
        String pack,title,text;
        Bundle extras;

        // login yapilmis mi kontrol et
        try{
            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            login_str = prfs.getString("login", "");
        }catch(Exception e)
        {
            login_str= " ";
        }

        // read settings here before do anything
        readSettings();

        System.out.println("***************** MYSERVICE ***********");
        System.out.println("*************** "+onOff+" ***************");
        System.out.println("************** "+choosenApps+" ************");

        // just make sure no mistake and fill the strings for if error occurs
        if(login_str.length()>5 && onOff.equals("on")) {  // login yapilmissa ve "on" ise islem yap
            output="";  // clear this for just in case
            packchar=" ";

            try {
                pack = sbn.getPackageName();
                extras = sbn.getNotification().extras;
                title = extras.getString("android.title").toString();
                text = extras.getCharSequence("android.text").toString();
            } catch (Exception e) {
                System.out.println("**** HATA NOTIFYSERVICE CLASS ****");
                pack = "empty1";
                title = "empty1";
                text = "empty1";
            }

            // write to console and check data
            Log.i("Package", pack);
            Log.i("Title", title);
            Log.i("Text", text);

            //com.instagram

            if(choosenApps.contains("w")) {
                if(pack.equals("com.whatsapp"))
                    packchar="w";
            }
            if(choosenApps.contains("i")) {
                if(pack.contains("com.instagram"))
                    packchar="i";
            }
            if(choosenApps.contains("t")) {
                if(pack.contains("com.twitter"))
                    packchar = "t";
            }


            if(!packchar.equals(" ")) {   // if pack is 'w' or 'i' or 't' then notification package is allow to go server
                // get ready data for server
                String input = lengthData(title, text);
                input += title + text;
                output = input;

                // call server here to send notification data
                System.out.println("*************** CALL THE SERVER *********");
                System.out.println("*************** " + output + "**************");
                new SoapCall().execute();
            }
        }
    }

    public void readSettings()
    {
        // if errors accured go to off
        try{
            SharedPreferences prfs = getSharedPreferences("SERVİCE_INFO_FILE", Context.MODE_PRIVATE);
            onOff = prfs.getString("which", "");
        }catch (Exception e)
        {
            onOff = "off";
        }

        if(onOff.equals("on")) {    // if onOff is off then no need for this
            // choosenApps
            try {
                SharedPreferences prfs = getSharedPreferences("STORE_SETTINGS", Context.MODE_PRIVATE);
                choosenApps = prfs.getString("settings", "");
            } catch (Exception e) {
                choosenApps = "e";  // empty this
            }
        }
    }

    // this is for: when i read the notification its gonna be mixed this way I know the how many chars are 'title' and 'text'
    public String lengthData(String title,String text)
    {
        String all="";  // add on this
        String number="";
        int lengthtitle = title.length();
        int lengthtext = text.length();
        all+=packchar;  // "w" or "t" or "i"
        all+=Integer.toString(lengthtitle);
        all+=":";
        all+=Integer.toString(lengthtext);
        all+=":";
        return all;
    }

    @Override
    public void onDestroy() {
        System.out.println("***** destroyed *****");
        super.onDestroy();
    }



    //**********************************************
    //**********************************************

    private class SoapCall extends AsyncTask<String,Object,String> {

        public static final String NAMESPACE = "YOUR-WEBSERVICE-HERE";
        public static final String URL = "YOUR-WEBSERVICE-HERE";
        public static final String SOAP_ACTION = "YOUR-WEBSERVICE-HERE";
        public static final String METHOD_NAME = "Data";
        public int TimeOut= 30000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // islem baslamadan once progress bar gibi birsey varsa buraya yaz
            }

        @Override
        protected String doInBackground(String... strings) {
            // create soap object
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

            request.addProperty("em",login_str);     // SERVER A GİDİCEK STRING ! KOMUT !
            request.addProperty("notify",output);     // SERVER A GİDİCEK STRING ! KOMUT !

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try{
                transportSE.call(SOAP_ACTION,envelope);
            }catch (Exception e)
            {
                e.printStackTrace();
                Log.e("Error ",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // islem tamamlandıktan sonrasi icin
            super.onPostExecute(s);
        }
    }

}
