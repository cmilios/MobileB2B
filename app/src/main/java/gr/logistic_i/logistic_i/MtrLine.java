package gr.logistic_i.logistic_i;

import android.os.Parcel;
import android.os.Parcelable;

public class MtrLine implements Parcelable {

    String code;
    String description;
    String qty;
    String qty1;
    String price;
    String discount;
    String cleanValue;
    String fpaValue;

    public MtrLine(String code, String description, String qty, String qty1, String price, String discount, String cleanValue, String fpaValue) {
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.qty1 = qty1;
        this.price = price;
        this.discount = discount;
        this.cleanValue = cleanValue;
        this.fpaValue = fpaValue;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    protected MtrLine(Parcel in) {
        code = in.readString();
        description = in.readString();
        qty = in.readString();
        qty1 = in.readString();
        price = in.readString();
        discount = in.readString();
        cleanValue = in.readString();
        fpaValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(description);
        dest.writeString(qty);
        dest.writeString(qty1);
        dest.writeString(price);
        dest.writeString(discount);
        dest.writeString(cleanValue);
        dest.writeString(fpaValue);
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