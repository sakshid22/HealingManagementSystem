package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class user_friend_list extends Activity {
    ArrayList<String> fullnameid = new ArrayList<String>();
    ArrayList<String> fusernameid = new ArrayList<String>();

    String myJSON;
    JSONArray peoples = null;
    ListView list_tab1;
    int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friend_list);
        get_user_list("dhruv");
    }
    private void get_user_list(final String username) {

        class LoginAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                try { nameValuePairs.add(new BasicNameValuePair("fbusername","dv"));
                }
                catch (Exception e){
                    Toast.makeText(getBaseContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
                String result = null;
                try{

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://scraap.esy.es/getlist.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e){
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            public void onPostExecute(String result){

                myJSON=result;
                showList();
                // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();

            }
        }
        LoginAsync la = new LoginAsync();
        la.execute();

    }
    protected void showList() {
        try {

            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray("result");
            }
            catch (Exception e){

            }

            for (int i = 0; i <peoples.length(); i++) {

                try {
                    JSONObject c = peoples.getJSONObject(i);

                    fullnameid.add(c.getString("fullname"));
                    fusernameid.add(c.getString("fusername"));

                }
                catch (NullPointerException e){


                }
                catch (RuntimeException v){

                }
                 /*   HashMap<String, String> persons = new HashMap<String, String>();
                persons.put(TAG_NAME, name);
                persons.put(TAG_MESSAGE, message);
                persons.put(TAG_time, time);
                persons.put(TAG_date, date);
                personList.add(persons);*/

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView list;
        CustomListAdapter_friends adapter=new CustomListAdapter_friends(this,fullnameid.toArray(new String[fullnameid.size()]),fusernameid.toArray(new String[fusernameid.size()]));
        list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

}
