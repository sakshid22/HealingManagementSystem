package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class signUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button b=(Button)findViewById(R.id.signup);
        final EditText username=(EditText)findViewById(R.id.username);
        final EditText password=(EditText)findViewById(R.id.password);
        final EditText fullname=(EditText)findViewById(R.id.fullname);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sighnup(username.getText().toString(),password.getText().toString(),fullname.getText().toString());
            }
        });
    }

    private void sighnup(final String username, final String password,final String fullname) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loadingDialog = ProgressDialog.show(signUp.this, "Please wait", "Creating your Account...");
            }

            @Override
            protected String doInBackground(String... params) {


                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("fullname", fullname));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://scraap.esy.es/signup.php");
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
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.contains("success")){
                    Toast.makeText(getBaseContext(),"Account Created Successfully! Login",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Username/Password!", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username);

    }


}
