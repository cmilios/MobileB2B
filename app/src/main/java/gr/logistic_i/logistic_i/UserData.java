package gr.logistic_i.logistic_i;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {

    @SerializedName("Success")
    @Expose
    private boolean success;

    @SerializedName("clientID")
    @Expose
    private String clientID;

    @SerializedName("COMPANY")
    @Expose
    private String COMPANY;

    @SerializedName("BRANCH")
    @Expose
    private String BRANCH;

    @SerializedName("MODULE")
    @Expose
    private String MODULE;

    @SerializedName("REFID")
    @Expose
    private String REFID;

    public UserData(boolean success, String clientID, String COMPANY, String BRANCH, String MODULE, String REFID) {
        this.success = success;
        this.clientID = clientID;
        this.COMPANY = COMPANY;
        this.BRANCH = BRANCH;
        this.MODULE = MODULE;
        this.REFID = REFID;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    public String getMODULE() {
        return MODULE;
    }

    public void setMODULE(String MODULE) {
        this.MODULE = MODULE;
    }

    public String getREFID() {
        return REFID;
    }

    public void setREFID(String REFID) {
        this.REFID = REFID;
    }
}
