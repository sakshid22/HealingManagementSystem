package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class chat_page extends Activity {
    private Timer myTimer;
    public EditText input;
    Button button;
    TextView output;
    int flag=1;
    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "username";
    private static final String TAG_ADD = "message";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    public  String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        input = (EditText)findViewById(R.id.input);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message=input.getText().toString();
                insertintochatbox(MainActivity.username,CustomListAdapter_friends.clicked_on_username,message);
            }
        });
        checkforinternet();
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (flag == 1) {
                    list = (ListView) findViewById(R.id.listView2);
                    personList = new ArrayList<HashMap<String, String>>();
                    get_user_list(CustomListAdapter_friends.clicked_on_username);
                } else {

                }
            }
        }, 0, 4000);

    }
    private void insertintochatbox(final String person1, final String person2, final String message) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("message", message));
                nameValuePairs.add(new BasicNameValuePair("person1", person1));
                nameValuePairs.add(new BasicNameValuePair("person2", person2));

                String result = null;
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://scraap.esy.es/add_to_chat.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
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

                input.setText("");
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(person1,person2,message);

    }

    private void get_user_list(final String fusername) {

        class LoginAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                try { nameValuePairs.add(new BasicNameValuePair("username",fusername));

                }
                catch (Exception e){
                    Toast.makeText(getBaseContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://scraap.esy.es/get_chats.php");
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
              //  Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                showList();
                // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();

            }
        }
        LoginAsync la = new LoginAsync();
        la.execute();

    }
    protected void showList(){

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");
            //myDb.dropchatbox();
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String username = c.getString("username");
                String message = c.getString("message");
                //  myDb.insertDatachat(name,address,time);
                HashMap<String,String> persons = new HashMap<String,String>();
                persons.put("username",username);
                persons.put("message", message);
                personList.add(persons);
            }


            ListAdapter adapter = new SimpleAdapter(

                    chat_page.this, personList, R.layout.list_item,

                    new String[]{"username","message"},
                    new int[]{R.id.name, R.id.message}

            );

            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void checkforinternet()
    {
            Log.i("bgv", "Checking for internet!");
            ConnectivityManager cm2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni2 = cm2.getActiveNetworkInfo();
            if (ni2 == null) {
                Toast.makeText(chat_page.this, "Not Connected to Internet!!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

