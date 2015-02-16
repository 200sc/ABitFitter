package com.gamifyGame.android;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
/**
 * Created by jiatao on 1/29/15.
 */
public abstract class PostJsonTask<T> extends AsyncTask<JSONObject, Void, String> {
    private String serverAddress;
    private String endpoint;
    public PostJsonTask(String serverAddress, String endpoint){
        this.serverAddress = serverAddress;
        this.endpoint = endpoint;
    }
    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        HttpURLConnection connection = null;
        String res = "";
//Make the web request to fetch new data
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(serverAddress+endpoint);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonObjects[0].toString()));
            HttpResponse response = client.execute(request);
            res = EntityUtils.toString(response.getEntity());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return res;
    }
    protected abstract String parseResponse (String response);
}