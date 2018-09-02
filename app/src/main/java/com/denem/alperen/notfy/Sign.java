package com.denem.alperen.notfy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Sign extends AppCompatActivity {

    private ProgressDialog mProgress;

    private String server_response=" ";      // serverdan gelen response

    Button logBtn,regBtn;
    private EditText nm,em,pw,pwa;  // name , email , password
    String name,email,password;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);


        // progress bar stg
        mProgress = new ProgressDialog(context);
        mProgress.setTitle("Kayıt işlemi gerçekleştiriliyor...");
        mProgress.setMessage("Lütfen Bekleyin...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        logBtn = (Button)findViewById(R.id.button2);
        regBtn = (Button)findViewById(R.id.button);
        nm = (EditText)findViewById(R.id.nameEdit);
        em = (EditText)findViewById(R.id.editText);
        pw = (EditText)findViewById(R.id.editText2);
        pwa = (EditText)findViewById(R.id.editText3);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // control email
                // send server
                name = nm.getText().toString();
                email = em.getText().toString();
                password = pw.getText().toString();

                if(email.indexOf("@")>=0 && email.length()>5)   // @ bu isaret varsa ve uzunluk 5 ten buyukse
                {
                    if(password.length()>1 && password.equals(pwa.getText().toString()))
                    {
                        // show progress bar while checking the server
                        mProgress.show();
                        //send server
                        new SoapCall().execute();
                    }
                    else
                    {
                        Toast.makeText(Sign.this,"Wrong password input !",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(Sign.this,"Please enter valid email adress !",Toast.LENGTH_LONG).show();
                }
            }
        });


        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent login = new Intent(Sign.this,Login.class);
                startActivity(login);
                finish();
            }
        });
    }

    public void goToLogin()
    {
        Intent login = new Intent(Sign.this,Login.class);
        startActivity(login);
        finish();
    }



    //**********************************************
    //**********************************************

    private class SoapCall extends AsyncTask<String,Object,String> {

        public static final String NAMESPACE = "YOUR-WEBSERVICE-HERE";
        public static final String URL = "YOUR-WEBSERVICE-HERE";
        public static final String SOAP_ACTION = "YOUR-WEBSERVICE-HERE";
        public static final String METHOD_NAME = "Sign";
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

            request.addProperty("nm",name);
            request.addProperty("em",email);     // SERVER A GİDİCEK STRING ! KOMUT !
            request.addProperty("pw",password);     // SERVER A GİDİCEK STRING ! KOMUT !

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try{
                transportSE.call(SOAP_ACTION,envelope);
                SoapObject result = (SoapObject)envelope.bodyIn;
                server_response = result.getProperty("SignResult").toString();
                System.out.println("************** GELEN :"+server_response+" *****************");
            }catch (Exception e)
            {
                server_response=" ";
                e.printStackTrace();
                Toast t = Toast.makeText(Sign.this,"shit",Toast.LENGTH_LONG);
                t.show();
                Log.e("Error ",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // islem tamamlandıktan sonrasi icin
            if(server_response.length()>1)  // true yada false gelcek
            {
                if(server_response.equals("true sign"))
                {
                    // stop progress bar
                    mProgress.dismiss();
                    // register successfully go to login activity
                    // login e git
                    goToLogin();
                }
                else
                {
                    // stop progress bar
                    mProgress.dismiss();
                    // hatali giris
                    // ekrana yazi goster
                    Toast.makeText(Sign.this,"Incorrect Register Entries",Toast.LENGTH_LONG).show();
                }
            }
            super.onPostExecute(s);
        }
    }

}
