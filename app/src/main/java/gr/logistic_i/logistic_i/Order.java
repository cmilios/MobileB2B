package gr.logistic_i.logistic_i;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Parcelable {

    private String code;
    private String findoc;
    private String fincode;
    private String trndate;
    private String trdrName;
    private String sumamnt;

    public Order(String code, String findoc, String fincode, String trndate, String trdrName, String sumamnt) {
        this.code = code;
        this.findoc = findoc;
        this.fincode = fincode;
        this.trndate = trndate;
        this.trdrName = trdrName;
        this.sumamnt = sumamnt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFindoc() {
        return findoc;
    }

    public void setFindoc(String findoc) {
        this.findoc = findoc;
    }

    public String getFincode() {
        return fincode;
    }

    public void setFincode(String fincode) {
        this.fincode = fincode;
    }

    public String getTrndate() {
        return trndate;
    }

    public void setTrndate(String trndate) {
        this.trndate = trndate;
    }

    public String getTrdrName() {
        return trdrName;
    }

    public void setTrdrName(String trdrName) {
        this.trdrName = trdrName;
    }

    public String getSumamnt() {
        return sumamnt;
    }

    public void setSumamnt(String sumamnt) {
        this.sumamnt = sumamnt;
    }


    protected Order(Parcel in) {
        code = in.readString();
        findoc = in.readString();
        fincode = in.readString();
        trndate = in.readString();
        trdrName = in.readString();
        sumamnt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(findoc);
        dest.writeString(fincode);
        dest.writeString(trndate);
        dest.writeString(trdrName);
        dest.writeString(sumamnt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}