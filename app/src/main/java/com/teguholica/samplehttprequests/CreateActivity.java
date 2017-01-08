package com.teguholica.samplehttprequests;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class CreateActivity extends AppCompatActivity {

    private TextView txtName, txtWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        txtName = (TextView) findViewById(R.id.name);
        txtWebsite = (TextView) findViewById(R.id.website);
        Button btnSaveUrlEncoded = (Button) findViewById(R.id.save_urlencoded);
        Button btnSaveJSON = (Button) findViewById(R.id.save_json);

        btnSaveUrlEncoded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPostUrlEncoded().execute(
                        txtName.getText().toString(),
                        txtWebsite.getText().toString()
                );
            }
        });

        btnSaveJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPostJSON().execute(
                        txtName.getText().toString(),
                        txtWebsite.getText().toString()
                );
            }
        });
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private class SendPostUrlEncoded extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            String endpoint = "http://api.androidsemua.com/httpsample";

            try {
                URL url = new URL(endpoint);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.connect();

                JSONObject params = new JSONObject();
                params.put("name", strings[0]);
                params.put("website", strings[1]);

                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(getPostDataString(params));
                osw.flush();
                osw.close();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                in.close();

                return true;
            } catch (IOException | JSONException e) {
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            setResult(200);
            finish();
        }
    }

    private class SendPostJSON extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            String endpoint = "http://api.androidsemua.com/httpsample";

            try {
                URL url = new URL(endpoint);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.connect();

                JSONObject params = new JSONObject();
                params.put("name", strings[0]);
                params.put("website", strings[1]);

                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(params.toString());
                osw.flush();
                osw.close();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                in.close();

                return true;
            } catch (IOException | JSONException e) {
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            setResult(200);
            finish();
        }
    }
}
