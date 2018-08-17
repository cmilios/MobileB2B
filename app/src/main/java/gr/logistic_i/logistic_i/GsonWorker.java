package gr.logistic_i.logistic_i;


import android.util.Patterns;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GsonWorker {

    private String url;
    private Boolean state = false;
    private String authenticateClID = "";
    private String refID = "";
    private Boolean authenticationFlag = false;
    private ArrayList<Order> sqlResponse = new ArrayList<>();
    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private boolean validURL = false;

    GsonWorker(String url) {
        this.url = url;
    }

    public void makeLogin(Creds creds) {

        String jsonData = creds.serObjLogin();
        String resstr = getData(jsonData);
        try {
            if (resstr == null){
                return;
            }
            JSONObject resObj = new JSONObject(resstr);
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                UserData us = new UserData();

                us = us.desirializeJsonStr(resObj.toString());
                makeAuthenticate(us);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void makeAuthenticate(UserData us) {

        String authUser = us.serObj();
        refID = us.getRefId();
        String resstr = getData(authUser);
        try {
            JSONObject resObj = new JSONObject(resstr);
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                authenticateClID = resObj.getString("clientID");
                authenticationFlag = true;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Order> getSqlOrders(SqlRequest sqlRequest) {
        String resstr = getData(sqlRequest.serSqlData());
        try {
            JSONObject resObj = new JSONObject(resstr);
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                sqlResponse = sqlRequest.parseResponse(resObj);
                return sqlResponse;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<MtrLine> getMtrLines(MtrLinesReq mtrLinesReq) {
        String resstr = getData(mtrLinesReq.serObj());
        try {
            JSONObject resObj = new JSONObject(resstr);
            if (resObj.has("success")) {
                state = resObj.getBoolean("success");
            }
            if (state) {
                mtrLines = mtrLinesReq.parseResponse(resObj);
                return mtrLines;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Mtrl> getFOI(MtrlReq mtrlReq) {
        String resstr = getData(mtrlReq.serMtrlOrders());
        try {
            JSONObject resObj = new JSONObject(resstr);
            return mtrlReq.parseResponse(resObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String calculatePrice(JSONObject jsonObject) {

        String resstr = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(resstr);
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (state) {
                return json.toString();
        }
        return null;

    }


    public String getWayOfTransformation(JSONObject jsonObject) {
        String resstr = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(resstr);
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (state) {
                return json.toString();
        }
        return null;

    }

    public String setData(JSONObject jsonObject) {
        String resstr = getData(jsonObject.toString());
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(resstr);
            if(json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (state) {
                return json.toString();
        }
        return null;

    }

    public String getIfAbleToDelete(JSONObject jsonObject){
        String var = null;
        String resstr= getData(jsonObject.toString());
        JSONObject json;
        try {
                json = new JSONObject(resstr);
                JSONObject data = json.getJSONObject("data");
                JSONArray saldoc = data.getJSONArray("SALDOC");
                var = saldoc.getJSONObject(0).getString("FINSTATES");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return var;
    }

    public boolean setDeleteFinstate(JSONObject jsonObject){
        String resstr = getData(jsonObject.toString());
        try {
            JSONObject json = new JSONObject(resstr);
            if (json.has("success")){
                state = json.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }


    private String getData(String json) {
        OkHttpClient client = new OkHttpClient();

        String finalURL = "https://" + url + ".oncloud.gr/s1services";
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
                client.newCall(request).execute().close();

                ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                response.close();

                return responseBodyCopy.string();

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
