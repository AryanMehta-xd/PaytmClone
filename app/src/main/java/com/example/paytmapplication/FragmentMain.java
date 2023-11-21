package com.example.paytmapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentMain extends AppCompatActivity {

    ImageView btn_scan;
    String user_id;
    String API_URL = "http://192.168.0.108/API/saveScan.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id_value");
        btn_scan = (ImageView) findViewById(R.id.btn_pay);

        btn_scan.setOnClickListener(v -> {
            scanCode();
        });
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume Up to Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents()!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMain.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String qry = "?t1="+user_id+"&t2="+result.getContents();
                    dbCsp dbCsp = new dbCsp();
                    dbCsp.execute(API_URL+qry);
                }
            }).show();
        }
    });

    class dbCsp extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success")){
                Toast.makeText(FragmentMain.this,"Scan Successfull",Toast.LENGTH_SHORT).show();
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