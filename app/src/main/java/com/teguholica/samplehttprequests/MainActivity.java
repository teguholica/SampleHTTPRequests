package com.teguholica.samplehttprequests;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView vList = (ListView) findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createActivity = new Intent(getApplicationContext(), CreateActivity.class);
                startActivityForResult(createActivity, 1);
            }
        });

        listAdapter = new ListAdapter();
        vList.setAdapter(listAdapter);

        new SendGet().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 200) {
            new SendGet().execute();
        }
    }

    private class ListAdapter extends BaseAdapter {

        private List<Profile> profiles;

        private ListAdapter() {
            this.profiles = new ArrayList<>();
        }

        private void setData(List<Profile> profiles) {
            this.profiles = profiles;
        }

        @Override
        public int getCount() {
            return profiles.size();
        }

        @Override
        public Profile getItem(int i) {
            return profiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);
            }

            Profile profile = getItem(i);

            TextView txtName = (TextView) view.findViewById(android.R.id.text1);
            TextView txtWebsite = (TextView) view.findViewById(android.R.id.text2);

            txtName.setText(profile.getName());
            txtWebsite.setText(profile.getWebsite());

            return view;
        }
    }

    private class SendGet extends AsyncTask<Void, Void, List<Profile>> {

        @Override
        protected List<Profile> doInBackground(Void... voids) {
            String endpoint = "http://api.androidsemua.com/httpsample";

            List<Profile> profiles = new ArrayList<>();

            try {
                URL url = new URL(endpoint);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                JSONArray doc = new JSONArray(response.toString());
                for (int i = 0;i < doc.length();i++) {
                    JSONObject element = doc.getJSONObject(i);
                    profiles.add(new Profile(
                            element.getString("name"),
                            element.getString("website")
                    ));
                }

                return profiles;
            } catch (IOException | JSONException e) {
                return profiles;
            }
        }

        @Override
        protected void onPostExecute(List<Profile> profiles) {
            listAdapter.setData(profiles);
            listAdapter.notifyDataSetChanged();
        }
    }
}
