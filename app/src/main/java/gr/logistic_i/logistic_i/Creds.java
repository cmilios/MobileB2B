package gr.logistic_i.logistic_i;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
@Entity
public class Creds  implements Serializable {

    @PrimaryKey
    @NonNull
    private String name;
    private String pass;
    private String curl;

    Creds(@NonNull String name, String pass, String curl) {
        this.name = name;
        this.pass = pass;
        this.curl = curl;
    }

    public String serObjLogin(){
        JSONObject json = new JSONObject();
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

    @Override
    public String toString() {
        return name;
    }


}
