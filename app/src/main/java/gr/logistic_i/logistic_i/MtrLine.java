package gr.logistic_i.logistic_i;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class MtrLine implements Parcelable {

    private String mtrl;
    private String code;
    public String description;

    public String qty;
    private String qty1;

    public String price;

    public String discount;

    public String cleanValue;

    public String fpaValue;
    private int unitSpinnerPosition;
    private String unitSelectedName;
    private int unitSelectedCode;
    private Mtrl linkedMtrl;
    private int num02;

    MtrLine(String mtrl,String code, String description, String qty, String qty1, String price, String discount, String cleanValue, String fpaValue, int mUnit, String sUnit,int unitSelectedCode, int num02) {
        this.mtrl = mtrl;
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.qty1 = qty1;
        this.price = price;
        this.discount = discount;
        this.cleanValue = cleanValue;
        this.fpaValue = fpaValue;
        this.unitSpinnerPosition = mUnit;
        this.unitSelectedName = sUnit;
        this.unitSelectedCode = unitSelectedCode;
        this.num02 = num02;
    }


    public void setLinkedMtrl(Mtrl linkedMtrl) {
        this.linkedMtrl = linkedMtrl;
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

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQty1() {
        return qty1;
    }

    public void setMtrl(String mtrl) {
        this.mtrl = mtrl;
    }

    public String getQty() {
        return qty;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getUnitSelectedCode() {
        return unitSelectedCode;
    }

    public void setUnitSelectedCode(int unitSelectedCode) {
        this.unitSelectedCode = unitSelectedCode;
    }

    public int getUnitSpinnerPosition() {
        return unitSpinnerPosition;
    }

    public void setUnitSpinnerPosition(int unitSpinnerPosition) {
        this.unitSpinnerPosition = unitSpinnerPosition;
    }

    public JSONObject serCalcLine(int counter){
        JSONObject json = new JSONObject();
        try {
            json.put("LINENUM", counter);
            if (linkedMtrl==null){
                json.put("MTRL", mtrl);
                json.put("QTY1", qty1.replace(",", "."));
            }
            else {
                json.put("MTRL", mtrl);
            if (unitSpinnerPosition == 0) {
                json.put("QTY1", qty.replace(",", "."));
            }
            if (unitSpinnerPosition == 1) {
                if (linkedMtrl.getMu21mode().equals("1")) {
                    json.put("QTY2", qty1.replace(",", "."));
                } else {
                    json.put("QTY1", qty.replace(",", "."));
                }
            }
            if (unitSpinnerPosition == 2) {
                if (linkedMtrl.getMu41mode().equals("1")) {
                    json.put("QTY", qty1.replace(",", "."));
                } else {
                    json.put("QTY1", qty.replace(",", "."));
                }
            }

            }
            if (linkedMtrl==null){
                json.put("NUM02", num02);
            }
            else {
                json.put("NUM02", num02);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return json;
    }

    public String getUnitSelectedName() {
        return unitSelectedName;
    }

    public void setUnitSelectedName(String unitSelectedName) {
        this.unitSelectedName = unitSelectedName;
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
        unitSpinnerPosition = in.readInt();
        unitSelectedName = in.readString();
        linkedMtrl = in.readParcelable(Mtrl.class.getClassLoader());
        unitSelectedCode = in.readInt();
        num02 = in.readInt();
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
        dest.writeInt(unitSpinnerPosition);
        dest.writeString(unitSelectedName);
        dest.writeParcelable(this.linkedMtrl, flags);
        dest.writeInt(unitSelectedCode);
        dest.writeInt(num02);
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