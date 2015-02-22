package com.gamifyGame.android;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.gamifyGame.ActionResolver;


public class ActionResolverAndroid implements ActionResolver {
    Handler handler;
    private Context context;
    private static ActionResolverAndroid actionResolverAndroid;

    private ActionResolverAndroid(Context context){
        handler = new Handler();
        this.context = context;
    }

    public static ActionResolverAndroid getActionResolverAndroid(Context context, boolean authority){
        if (actionResolverAndroid == null){
            actionResolverAndroid = new ActionResolverAndroid(context);
        }
        if (authority){
            actionResolverAndroid.setContext(context);
        }
        return actionResolverAndroid;
    }

    private void setContext(Context context){this.context = context;}

    public void putSharedPrefs(final String key, final String val) {
        System.out.println("ActionResolverAndroid: Key:"+ key + " Val: " + val);
        handler.post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = context.getSharedPreferences("bitPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(key,val);
                editor.commit();
            }
        });
    }

    public void scanAct(final CharSequence text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Zxing.class);
                context.startActivity(intent);
            }
        });
    }

    public void toHomeScreen(final CharSequence text){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(setIntent);
            }
        });
    }

    public void showToast(final CharSequence text){

                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }

    public void setSleepState(final boolean isSleeping){
        if(isSleeping)       System.out.println("ACTIONRESOLVER: GO TO SLEEP");
        else System.out.println("ACTIONRESOLVER: Wake up!!!!");


    }
}

