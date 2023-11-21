package com.example.paytmapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_signup;
    EditText edit_user;
    EditText edit_pass;
    String API_URL = "http://192.168.0.108/API/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        edit_user = (EditText) findViewById(R.id.editText_id);
        edit_pass = (EditText) findViewById(R.id.editTextTextPassword);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin(view);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin(View v){
        String user_id = edit_user.getText().toString();
        String user_pass = edit_pass.getText().toString();

        if(user_id.equals("")||user_pass.equals("")){

        }else{
            String qry = "?t1="+user_id+"&t2="+user_pass;
            dbC dbC = new dbC();
            dbC.execute(API_URL+qry);
        }
    }

    class dbC extends AsyncTask<String,Void,String>{

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("found")){
                Intent intent = new Intent(MainActivity.this,FragmentMain.class);
                intent.putExtra("user_id_value",edit_user.getText().toString());
                startActivity(intent);
            }else if(s.equals("not found")){
                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
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