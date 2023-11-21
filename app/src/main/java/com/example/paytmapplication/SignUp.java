package com.example.paytmapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    EditText et_mobNo,et_name,et_email,et_pass,et_repass;
    Button btn_createAccount;

    String API_URL = "http://192.168.0.108/API/signup.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_mobNo = (EditText) findViewById(R.id.editText_mobNo);
        et_name = (EditText) findViewById(R.id.editText_name);
        et_email = (EditText) findViewById(R.id.editText_email);
        et_pass = (EditText) findViewById(R.id.editText_pass);
        et_repass = (EditText) findViewById(R.id.editText_repass);

        btn_createAccount = (Button) findViewById(R.id.btn_createAccount);

        builder = new AlertDialog.Builder(this);
        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup(view);
            }
        });
    }

    private void handleSignup(View v){
        String user_name = et_name.getText().toString();
        String user_id = et_mobNo.getText().toString();
        String user_email = et_email.getText().toString();
        String user_pass = et_pass.getText().toString();
        String user_repass = et_repass.getText().toString();

        if(user_pass.equals(user_repass)){
            String qry = "?t1="+user_id+"&t2="+user_name+"&t3="+user_email+"&t4="+user_pass;
            dbCs dbC = new dbCs();
            dbC.execute(API_URL+qry);
        }else{
            Toast.makeText(this,"Passwords do not Match",Toast.LENGTH_LONG).show();
        }
    }

    class dbCs extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success")){
                builder.setTitle("Alert").
                        setMessage("Account Created Successfully\nPlease Login").
                        setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }else if(s.equals("failure")){
                builder.setTitle("Alert").
                        setMessage("Something went wrong!!\nPlease Try Again").
                        setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String furl = strings[0];

            try {
                URL url = new URL(furl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                return br.readLine();
            }catch (Exception e){
                return e.getMessage();
            }
        }
    }
}