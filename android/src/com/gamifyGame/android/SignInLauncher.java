package com.gamifyGame.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.*;
import static android.widget.Toast.makeText;


/**
 * Created by Folly on 1/26/2015.
 */
public class SignInLauncher extends Activity{

    Context context;
    Bundle thisBundle;
    EditText userName;
    EditText passWord;
    String signInStatus;

    public void onCreate(Bundle savedStuff) {
        super.onCreate(savedStuff);
        this.thisBundle = savedStuff;
        this.context = getApplicationContext();
        this.signInStatus = "false";

        setContentView(R.layout.loginscreenres);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);

        findViewById(R.id.signin).setOnClickListener(signIn);
        findViewById(R.id.signup).setOnClickListener(signUp);
        findViewById(R.id.skip).setOnClickListener(skip);
    }

    private final View.OnClickListener signIn;
    {
        signIn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theName = userName.getText().toString();
                String thePass = passWord.getText().toString();

                JSONObject userInfo = new JSONObject();
                try {
                    userInfo.put("username", theName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    userInfo.put("password", thePass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Make request for password confirmation
                new signingIn("http://104.131.171.125:8080", "/api/signIn").execute(userInfo);
            }
        };
    }

    private final View.OnClickListener skip;
    {
        skip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent change = new Intent(getApplicationContext(), AndroidLauncher.class);
            //thisBundle.putSerializable("Player", player);
            startActivity(change);
        }

    };
    }

    private final View.OnClickListener signUp;
    {
        signUp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(getApplicationContext(), SignUpLauncher.class);
                //thisBundle.putSerializable("Player", player);
                startActivity(change);
            }

        };
    }

    class signingIn extends PostJsonTask<String> {
        JSONObject tempRes = new JSONObject();
        String username;
        String userID;

        public signingIn(String serverAddress, String endpoint) {
            super(serverAddress, endpoint);
        }

        protected String parseResponse(String response){
            return response;
        }

        @Override
        protected void onPostExecute(String output){
            try {
                this.tempRes = new JSONObject(output);
                System.out.println(signInStatus);
                this.username = tempRes.getString("name");
                this.userID = tempRes.getString("id");
            } catch (JSONException e){
                e.printStackTrace();
            }

            try{
                signInStatus = tempRes.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (signInStatus.equals("true")){
                Intent change = new Intent(getApplicationContext(), AndroidLauncher.class);
                Bundle newBundle = new Bundle();
                //thisBundle.putSerializable("Player", tempRes);
                newBundle.putString("ID", userID);
                change.putExtras(newBundle);
                startActivity(change);
            }

            else if (signInStatus.equals("false")) {
                //setContentView(R.layout.loginscreenres);
            }
        }
    }



}
