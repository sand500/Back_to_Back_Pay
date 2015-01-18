package com.codetilyoudrop.tap2tap.phonetophonepay;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Arshiya Singh on 1/17/2015.
 */
public class Payment {
    public void paymentPost() {
        HttpClient client = new DefaultHttpClient();
        String postURL = "https://api.venmo.com/v1/payments";
        HttpPost post = new HttpPost(postURL);
        String access_token = new Scanner("access_token.txt").toString();

        try {
            List<NameValuePair> params = new ArrayList<>(5);
            params.add(new BasicNameValuePair("access_token", access_token));
            params.add(new BasicNameValuePair("phone", ""));
            params.add(new BasicNameValuePair("note", ""));
            params.add(new BasicNameValuePair("amount", ""));
            params.add(new BasicNameValuePair("audience", ""));
            post.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = client.execute(post);
        }catch (ClientProtocolException e) {
            // something
        }catch (IOException e) {
            // something
        }
    }

    public static void main(String[] args) {


    }
}