package gr.logistic_i.logistic_i;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Creds  implements Serializable {

    private String name;
    private String pass;
    private String curl;

    private JSONObject json = new JSONObject();

    public Creds(String name, String pass, String curl) {
        this.name = name;
        this.pass = pass;
        this.curl = curl;
    }

    public String serObjLogin(){
        try {
            json.put("Service", "login");
            json.put("Username", name);
            json.put("Password", pass);
            json.put("AppId", "1100");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }
}
