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

/**
 * Created by jiatao on 2/8/15.
 */
public class SignUpLauncher extends Activity {

    Context context;
    Bundle thisBundle;
    EditText userName;
    EditText email;
    EditText passWord;
    EditText passWord2;
    String signUpStatus;

    public void onCreate(Bundle savedStuff) {
        super.onCreate(savedStuff);
        this.thisBundle = savedStuff;
        this.context = getApplicationContext();
        this.signUpStatus = "false";

        setContentView(R.layout.signupscreenres);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        passWord2 = (EditText) findViewById(R.id.password1);
        email = (EditText) findViewById(R.id.email);

        findViewById(R.id.signup).setOnClickListener(signUp);
    }

    private final View.OnClickListener signUp;
    {
        signUp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theName = userName.getText().toString();
                String thePass = passWord.getText().toString();
                String thePass2 = passWord2.getText().toString();
                String emaill = email.getText().toString();

                if (thePass.equals(thePass2)){
                    JSONObject userInfo = new JSONObject();
                    try {
                        userInfo.put("username", theName);
                        userInfo.put("password", thePass);
                        userInfo.put("email", emaill);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Make request for password confirmation
                    new signingUp("http://104.131.171.125:8080", "/api/signUp").execute(userInfo);
                }

                else{
                    //popup notification
                }


            }
        };
    }

    class signingUp extends PostJsonTask<String> {


        public signingUp(String serverAddress, String endpoint) {
            super(serverAddress, endpoint);
        }

        protected String parseResponse(String response){
            return response;
        }

        @Override
        protected void onPostExecute(String output){
                Intent change = new Intent(getApplicationContext(), SignInLauncher.class);
                Bundle newBundle = new Bundle();
                startActivity(change);
                String tmpMessage = "hiiiii";
                Toast.makeText(getApplicationContext(), tmpMessage, Toast.LENGTH_LONG).show();
            }
        }
   }


