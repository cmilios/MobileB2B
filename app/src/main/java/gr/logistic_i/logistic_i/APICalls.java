package gr.logistic_i.logistic_i;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class APICalls  extends AsyncTask<Creds, Void, Void> {
    UserData us = new UserData(false,null,null,null,null,null);
    private Gson gson = new GsonBuilder().create();




    public UserData initLogin(Creds cr){

//
//        StringBuilder finalURL = new StringBuilder("https://");
//        finalURL.append(cr.getCurl());
//        finalURL.append("/s1services");
//        HttpURLConnection con = null;
//        cr.setCurl(finalURL.toString());
//
//        try {
//            URL url = new URL(finalURL.toString());
//            con = (HttpURLConnection) url.openConnection();
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
//            con.setRequestMethod("POST");
//
//            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//            wr.write(cr.serObj());
//            wr.flush();
//
//            StringBuilder sb = new StringBuilder();
//            int HttpResult = con.getResponseCode();
//            StringBuilder result = new StringBuilder();
//            if (HttpResult == HttpURLConnection.HTTP_OK) {
//                InputStream in = new BufferedInputStream(con.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                String line = reader.readLine();
//                if (line.contains("false"))
//                    us.setSuccess(false);
//                result.append(line);
//                while ((line = reader.readLine()) != null) {
//                    result.append(line);
//                }
//            } else {
//                System.out.println(con.getResponseMessage());
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            con.disconnect();
//        }
//
//        Response.Listener<JSONObject> r1 = new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                res = response.toString();
//                us = gson.fromJson(String.valueOf(response), UserData.class);
//
//            }
//        };
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, finalURL.toString(), cr.serObj(), r1, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        requestQueue.add(req);
//        requestQueue.start();
//
//
//
//
//
//


        return us;
    }


    @Override
    protected Void doInBackground(Creds... creds) {
        StringBuilder finalURL = new StringBuilder("https://");
        finalURL.append(creds[0].getCurl());
        finalURL.append("/s1services");
        HttpURLConnection con = null;
        creds[0].setCurl(finalURL.toString());

        try {
            URL url = new URL(finalURL.toString());
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(creds[0].serObj());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            StringBuilder result = new StringBuilder();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = reader.readLine();
                if (line.contains("false"))
                    us.setSuccess(false);
                result.append(line);
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                System.out.println(con.getResponseMessage());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

    return null;
    }
}

