package gr.logistic_i.logistic_i;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class APICalls {
    UserData us = new UserData(false,null,null,null,null,null);
    private Gson gson = new GsonBuilder().create();
    String json;
    String res;


    public UserData initLogin(Creds cr) {
        StringBuilder finalURL = new StringBuilder("https://");
        finalURL.append(cr.getCurl());
        finalURL.append("/s1services");
        Response.Listener<JSONObject> r1 = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                res = response.toString();
                us = gson.fromJson(response.toString(), UserData.class);
            }
        };
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, finalURL.toString(), cr.serObj(), r1, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        return us;
    }
}
