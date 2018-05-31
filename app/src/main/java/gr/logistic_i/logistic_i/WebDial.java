package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

public class WebDial extends AsyncTask<Object,Void,Void> {

    private StringBuilder responseItem = new StringBuilder();
    String service = new String();

    Context context;
    public WebDial(Context context) {
        this.context = context.getApplicationContext();
    }


    @Override
    protected Void doInBackground(Object... obj) {


        if (obj[1] == "login"){
            makeLogin((String)obj[0], obj[2].toString());
//            service = "login";
//
//            //Build url to connect to
//            StringBuilder finalURL = new StringBuilder("https://");
//            finalURL.append(obj[0]);
//            finalURL.append("/s1services");
//
//            HttpURLConnection con = null;
//
//            try {
//                // Make a connection with the API
//                URL furl = new URL(finalURL.toString());
//                con = (HttpURLConnection) furl.openConnection();
//                con.setRequestMethod("POST");
//                con.addRequestProperty("Accept", "application/json");
//                con.addRequestProperty("Content-Type", "application/json; charset=win1253");
//                con.setDoInput(true);
//
//                //Request Parameters you want to send
//                String urlParameters = obj[2].toString();
//
//                // Send post request
//                con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();
//
//                // Begin streaming the JSON
//                InputStream in = new BufferedInputStream(con.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));
//
//                // Read the first line
//                String line = reader.readLine();
//
//                // Append the first line to the builder
//                responseItem.append(line);
//
//
//
//                // Read the remaining stream until we are done
//                while ((line = reader.readLine()) != null) {
//                    responseItem.append(line);
//                }
//                obj[3] = responseItem;
//                PrintStream out = new PrintStream(System.out, true, "windows-1253");
//                out.println(responseItem);
//
//            } catch (Exception e) {
//                // Usually indicates a lack of Internet connection
//                return null;
//            } finally {
//                // Close connection
//                con.disconnect();
//            }
//
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (responseItem!=null && service == "login"){
            context.startActivity(new Intent(context, MainMenu.class));
        }
    }

    public String makeLogin(String url, String jsonData){

        service = "login";

        //Build url to connect to
        StringBuilder finalURL = new StringBuilder("https://");
        finalURL.append(url);
        finalURL.append("/s1services");

        HttpURLConnection con = null;


        // Make a connection with the API
        URL furl = null;
        try {
            furl = new URL(finalURL.toString());
            con = (HttpURLConnection) furl.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Accept", "application/json");
            con.addRequestProperty("Content-Type", "application/json; charset=win1253");
            con.setDoInput(true);

            //Request Parameters you want to send
            String urlParameters = jsonData.toString();

            // Send post request
            con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));

            // Read the first line
            String line = reader.readLine();

            // Append the first line to the builder
            responseItem.append(line);



            // Read the remaining stream until we are done
            while ((line = reader.readLine()) != null) {
                responseItem.append(line);
            }

            return  responseItem.toString();
        } catch (Exception e) {
            return null;
        }
        finally {
            con.disconnect();
        }


    }

}
