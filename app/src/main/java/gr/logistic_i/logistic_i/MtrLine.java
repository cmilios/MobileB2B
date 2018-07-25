package gr.logistic_i.logistic_i;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MtrLine implements Parcelable {



    private String mtrl;
    private String code;
    private String description;
    private String qty;
    private String qty1;
    private String price;
    private String discount;
    private String cleanValue;
    private String fpaValue;
    private int mUnit;
    private String sUnit;

    MtrLine(String mtrl,String code, String description, String qty, String qty1, String price, String discount, String cleanValue, String fpaValue, int mUnit, String sUnit) {
        this.mtrl = mtrl;
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.qty1 = qty1;
        this.price = price;
        this.discount = discount;
        this.cleanValue = cleanValue;
        this.fpaValue = fpaValue;
        this.mUnit = mUnit;
        this.sUnit = sUnit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public String getMtrl() {
        return mtrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQty1() {
        return qty1;
    }

    public int getmUnit() {
        return mUnit;
    }

    public void setmUnit(int mUnit) {
        this.mUnit = mUnit;
    }

    public JSONObject serCalcLine(){
        JSONObject json = new JSONObject();
        try {
            json.put("MTRL", mtrl);
            json.put("QTY1", qty);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return json;
    }

    public String getsUnit() {
        return sUnit;
    }

    public void setsUnit(String sUnit) {
        this.sUnit = sUnit;
    }

    public String getCleanValue() {
        return cleanValue;
    }

    public void setCleanValue(String cleanValue) {
        this.cleanValue = cleanValue;
    }

    public String getFpaValue() {
        return fpaValue;
    }

    public void setFpaValue(String fpaValue) {
        this.fpaValue = fpaValue;
    }

    private MtrLine(Parcel in) {
        mtrl = in.readString();
        code = in.readString();
        description = in.readString();
        qty = in.readString();
        qty1 = in.readString();
        price = in.readString();
        discount = in.readString();
        cleanValue = in.readString();
        fpaValue = in.readString();
        mUnit = in.readInt();
        sUnit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mtrl);
        dest.writeString(code);
        dest.writeString(description);
        dest.writeString(qty);
        dest.writeString(qty1);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(cleanValue);
        dest.writeString(fpaValue);
        dest.writeInt(mUnit);
        dest.writeString(sUnit);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MtrLine> CREATOR = new Parcelable.Creator<MtrLine>() {
        @Override
        public MtrLine createFromParcel(Parcel in) {
            return new MtrLine(in);
        }

        @Override
        public MtrLine[] newArray(int size) {
            return new MtrLine[size];
        }
    };
}