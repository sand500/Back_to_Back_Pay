package com.codetilyoudrop.tap2tap.phonetophonepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arshiya Singh on 1/17/2015.
 */
public class PaymentActivity extends AsyncTask <String, String, String>{
    private Context context;
    public PaymentActivity(Context context) {
      this.context = context;
    }
    public String paymentPost(String tokenGuest, String tokenMine, String username, String amount, String note) {
        OkHttpClient client = new OkHttpClient();
        String postURL = "https://api.venmo.com/v1/payments";
      String userID =  setUserId(tokenMine);

        if(userID==null) {
            Uri uriUrl = Uri.parse("https://api.venmo.com/v1/oauth/authorize?client_id=2271&scope=make_payments%20access_profile&response_type=token");
            SharedPreferences.Editor spEditor = context.getSharedPreferences("default",Activity.MODE_PRIVATE).edit();
            spEditor.putFloat("LastTime", System.currentTimeMillis());
            spEditor.commit();
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            context.startActivity(launchBrowser);
        }
        Log.d("MEDebug", "uSER id Response" + userID);
        RequestBody body = null;
        try {
            body = new FormEncodingBuilder()
                    .add("access_token", tokenGuest)
                    .add("user_id", userID)
                    .add("note", note)
                    .add("amount", amount)
                    .build();
        } catch(NullPointerException e){
            Uri uriUrl = Uri.parse("https://api.venmo.com/v1/oauth/authorize?client_id=2271&scope=make_payments%20access_profile&response_type=token");
            SharedPreferences.Editor spEditor = context.getSharedPreferences("default",Activity.MODE_PRIVATE).edit();
            spEditor.putFloat("LastTime", System.currentTimeMillis());
            spEditor.commit();
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            context.startActivity(launchBrowser);

        }
        Request request = new Request.Builder()
                .url(postURL)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();

        try {

            Response response = client.newCall(request).execute();
            String str_response = response.body().string();
            Log.v("log", str_response);
            JSONObject jsonResponse = new JSONObject(str_response);
            JSONObject data = jsonResponse.getJSONObject("data");
            return data.getString("balance");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String doInBackground(String... params) {
       return paymentPost(params[0],params[1],params[2],params[3],params[4]);
    }

    @Override
    protected void onPostExecute(String balance) {
        Toast.makeText(context, "Your balance is: " + balance, Toast.LENGTH_SHORT).show();
    }

    public String setUserId(String token) {
        SharedPreferences sp = context.getSharedPreferences("default",Activity.MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        String postURL = "https://api.venmo.com/v1/me?access_token=" + sp.getString("Token","");
        Log.d("MEDebug", "URL" +postURL);

        Request request = new Request.Builder()
                .url(postURL).build();

        try {

            Response response = client.newCall(request).execute();
            String str_response = response.body().string();
            Log.v("log", str_response);
            JSONObject jsonResponse = new JSONObject(str_response);
            Log.d("MEDebug", "Response" + str_response);
            if(jsonResponse.has("error")) {
                Log.d("MEDebug", "If SP Doesnt Containt Account");
                Uri uriUrl = Uri.parse("https://api.venmo.com/v1/oauth/authorize?client_id=2271&scope=make_payments%20access_profile&response_type=token");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putFloat("LastTime", System.currentTimeMillis());
                spEditor.commit();
                context.startActivity(launchBrowser);


            }
            else {
                JSONObject data = jsonResponse.getJSONObject("data");
                JSONObject user = data.getJSONObject("user");
                String uID = user.getString("id");
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString("Username", uID);

                spEditor.commit();
                return uID;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}