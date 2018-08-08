package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.webkit.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GsonWorker {

    private String url;
    private Boolean state = false;
    private String authenticateClID = new String();
    private String refID = new String();
    private Boolean authenticationFlag = false;
    private ArrayList<Order> sqlResponse = new ArrayList<>();
    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private boolean validURL = false;

    public GsonWorker(String url) {
        this.url = url;
    }

    public void makeLogin(Creds creds) {

        String jsonData = creds.serObjLogin();
        Response loginResponse = getData(jsonData);
        try {
            if (loginResponse == null){
                return;
            }
            JSONObject resObj = new JSONObject(loginResponse.body().string());
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                String loginClID = resObj.getString("clientID");
                UserData us = new UserData();

                us = us.desirializeJsonStr(resObj.toString());
                makeAuthenticate(us);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void makeAuthenticate(UserData us) {

        String authUser = us.serObj();
        refID = us.getRefId();
        Response authenticateResponse = getData(authUser);
        try {
            JSONObject resObj = new JSONObject(authenticateResponse.body().string());
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                authenticateClID = resObj.getString("clientID");
                authenticationFlag = true;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Order> getSqlOrders(SqlRequest sqlRequest) {
        Response sqlOrders = getData(sqlRequest.serSqlData());
        try {
            JSONObject resObj = new JSONObject(sqlOrders.body().string());
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                sqlResponse = sqlRequest.parseResponse(resObj);
                return sqlResponse;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<MtrLine> getMtrLines(MtrLinesReq mtrLinesReq) {
        Response lines = getData(mtrLinesReq.serObj());
        try {
            JSONObject resObj = new JSONObject(lines.body().string());
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                mtrLines = mtrLinesReq.parseResponse(resObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Mtrl> getFOI(MtrlReq mtrlReq) {
        Response res = getData(mtrlReq.serMtrlOrders());
        try {
            JSONObject resObj = new JSONObject(res.body().string());
            ArrayList<Mtrl> mtrList = mtrlReq.parseResponse(resObj);
            return mtrList;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String calculatePrice(JSONObject jsonObject) {

        Response lines = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(lines.body().string());
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (state) {
                return json.toString();
        }
        return null;

    }


    public String getWayOfTransformation(JSONObject jsonObject) {
        Response lines = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject( lines.body().string());
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (state) {
                return json.toString();
        }
        return null;

    }

    public String setData(JSONObject jsonObject) {
        Response lines = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(lines.body().string());
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (state) {
                return json.toString();
        }
        return null;

    }


    private String getJSON(String furl, String jsonData) {
        state = false;
        HttpURLConnection conn = null;

        // Create a StringBuilder for the final URL
        StringBuilder finalURL = new StringBuilder("https://");
        // Append API URL
        finalURL.append(furl);
        // Append endpoint
        finalURL.append("/s1services");


        // Create a StringBuilder to store the JSON string
        StringBuilder result = new StringBuilder();
        try {
            // Make a connection with the API
            URL url = new URL(finalURL.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Content-Type", "application/json; charset=" + "windows-1253");
            conn.setDoInput(true);
            conn.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonData);
            wr.flush();
            wr.close();
            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));
            // Read the first line
            String line = reader.readLine();

            // Append the first line to the builder
            result.append(line);

            // Read the remaining stream until we are done
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            if (result != null) {
                JSONObject res = new JSONObject(result.toString());
                state = res.getBoolean("success");
            }


        } catch (Exception e) {
            // Usually indicates a lack of Internet connection
            return null;
        } finally {
            // Close connection
            if (conn != null)
                conn.disconnect();
        }

        // Return the JSON string
        return result.toString();
    }

    public Drawable getImage(String furl, String imgURL) {
        state = false;
        HttpURLConnection conn = null;

        // Create a StringBuilder for the final URL
        StringBuilder finalURL = new StringBuilder("https://");
        // Append API URL
        finalURL.append(furl);
        // Append endpoint
        finalURL.append("/s1services");
        finalURL.append("/?filename=");
        finalURL.append(imgURL);


        // Create a StringBuilder to store the JSON string
        try {
            // Make a connection with the API
            URL url = new URL(finalURL.toString());
            conn = (HttpURLConnection) url.openConnection();


            // Begin streaming the JSON
            InputStream in = (InputStream) url.getContent();
            return Drawable.createFromStream(in, "src name");


        } catch (Exception e) {
            // Usually indicates a lack of Internet connection
            return null;
        } finally {
            // Close connection
            if (conn != null)
                conn.disconnect();
        }


    }

    private Response getData(String json) {
        OkHttpClient client = new OkHttpClient();

        String finalURL = "https://" + url + "/s1services";
        validURL = Patterns.WEB_URL.matcher(finalURL).matches();


        if (validURL) {
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(finalURL)
                    .post(body)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response;

            } catch (IOException e) {
                e.printStackTrace();


            }
        }
        return null;

    }


        public String getAuthenticateClID () {
            return authenticateClID;
        }

        public String getRefID () {
            return refID;
        }

        public Boolean getAuthenticationFlag () {
            return authenticationFlag;
        }

        public ArrayList<Order> getSqlResponse () {
            return sqlResponse;
        }

        public String getUrl () {
            return url;
        }

        public ArrayList<MtrLine> getMtrLines () {
            return mtrLines;
        }

    public boolean isValidURL() {
        return validURL;
    }
}
