package com.codetilyoudrop.tap2tap.phonetophonepay;

import android.content.Context;
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
    public String paymentPost(String user_id, String amount, String note) {
        OkHttpClient client = new OkHttpClient();
        String postURL = "https://api.venmo.com/v1/payments";
        String access_token = "";
        RequestBody body = new FormEncodingBuilder()
                .add("access_token", access_token)
                .add("email", user_id)
                .add("note", note)
                .add("amount", amount)
                .build();

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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String doInBackground(String... params) {
       return paymentPost(params[0],params[1],params[2]);
    }

    @Override
    protected void onPostExecute(String balance) {
        Toast.makeText(context, "Your balance is: " + balance, Toast.LENGTH_SHORT).show();
    }
}