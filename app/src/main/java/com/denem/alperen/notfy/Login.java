package com.denem.alperen.notfy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
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

public class Login extends AppCompatActivity {

    private ProgressDialog mProgress;

    final Context context = this;

    public static SharedPreferences preferences;        // bazi bilgileri kaydet

    private String server_response=" ";      // serverdan gelen response
    private EditText email,password;
    private Button loginBtn,signBtn;
    String Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // progress bar stg
        mProgress = new ProgressDialog(context);
        mProgress.setTitle("Kontrol ediliyor...");
        mProgress.setMessage("Lutfen Bekleyin...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        // bilgi kaydet
        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        // sill
        final Intent main = new Intent(this,MainActivity.class);
        // end

        email = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        loginBtn = (Button)findViewById(R.id.button);
        signBtn = (Button)findViewById(R.id.button2);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // control email
                // send server
                Email = email.getText().toString();
                Password = password.getText().toString();
                if(Email.indexOf("@")>=0 && Email.length()>5)   // @ bu isaret varsa ve uzunluk 5 ten buyukse
                {
                    if(Password.length()>1)
                    {
                        // show progress bar while checking the server
                        mProgress.show();
                        //send server
                        new SoapCall().execute();
                    }
                    else
                    {
                        Toast.makeText(Login.this,"Wrong password input !",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(Login.this,"Please enter valid email adress !",Toast.LENGTH_LONG).show();
                }

            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent sgn = new Intent(Login.this,Sign.class);
                startActivity(sgn);
                finish();
            }
        });
    }

    // go to MainActivity
    public void goToMain()
    {
        Intent main = new Intent(this,MainActivity.class);
        startActivity(main);
        finish();
    }


    //**********************************************
    //**********************************************

    private class SoapCall extends AsyncTask<String,Object,String> {

        public static final String NAMESPACE = "YOUR-WEBSERVICE-HERE";
        public static final String URL = "YOUR-WEBSERVICE-HERE";
        public static final String SOAP_ACTION = "YOUR-WEBSERVICE-HERE";
        public static final String METHOD_NAME = "Login";
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

            request.addProperty("em",Email);     // SERVER A GİDİCEK STRING ! KOMUT !
            request.addProperty("pw",Password);     // SERVER A GİDİCEK STRING ! KOMUT !
            request.addProperty("dvc","p");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try{
                transportSE.call(SOAP_ACTION,envelope);
                SoapObject result = (SoapObject)envelope.bodyIn;
                server_response = result.getProperty("LoginResult").toString();
                System.out.println("************** GELEN :"+server_response+" *****************");
            }catch (Exception e)
            {
                server_response=" ";
                e.printStackTrace();
                Toast t = Toast.makeText(Login.this,"shit",Toast.LENGTH_LONG);
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
                if(server_response.equals("true"))
                {
                    // stop progress bar
                    mProgress.dismiss();
                    // login successfully go to mainActivity
                    // maine git login i cihaza kaydet
                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("login",Email);
                    editor.apply();

                    goToMain();
                }
                else
                {
                    // stop progress bar
                    mProgress.dismiss();
                    // hatali giris
                    // ekrana yazi goster
                    Toast.makeText(Login.this,"Incorrect Login Entries",Toast.LENGTH_LONG).show();
                }
            }
            super.onPostExecute(s);
        }
    }


}
