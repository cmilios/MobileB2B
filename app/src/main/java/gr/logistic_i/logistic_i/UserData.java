package gr.logistic_i.logistic_i;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {

    @SerializedName("clientID")
    @Expose
    private String clientID;

    @SerializedName("objs")
    @Expose
    private ArrayList<String> objs = new ArrayList<>();

    private JSONObject json = new JSONObject();

    public UserData(String clientID, ArrayList<String> objs) {
        this.clientID = clientID;
        this.objs = objs;
    }

    UserData() {

    }

    private void setSuccess(boolean success) {
    }

    private void setClientID(String clientID) {
        this.clientID = clientID;
    }

    private void appendObjs(String s){
        objs.add(s);

    }

    public UserData desirializeJsonStr(String jsonStr){
        UserData usa = new UserData();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray objsArray = obj.getJSONArray("objs");

            boolean jstate = obj.getBoolean("success");
            String jaccessid = obj.getString("clientID");

            usa.setSuccess(jstate);
            usa.setClientID(jaccessid);
            usa.appendObjs(objsArray.getJSONObject(0).getString("COMPANY"));
            usa.appendObjs(objsArray.getJSONObject(0).getString("BRANCH"));
            usa.appendObjs(objsArray.getJSONObject(0).getString("MODULE"));
            usa.appendObjs(objsArray.getJSONObject(0).getString("REFID"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return usa;


    }

    public String serObj(){
        try {
            json.put("Service", "authenticate");
            json.put("clientID", clientID);
            json.put("COMPANY", objs.get(0));
            json.put("BRANCH", objs.get(1));
            json.put("MODULE", objs.get(2));
            json.put("REFID", objs.get(3));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();


    }

    public String getRefId(){
        return objs.get(3);
    }
}
