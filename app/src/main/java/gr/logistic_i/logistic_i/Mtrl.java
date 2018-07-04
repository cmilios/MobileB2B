package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class Mtrl implements Parcelable {

    String imgURL;
    String code;
    String name;
    String sales;
    String manufacturer;
    String price;
    Drawable image;
    String correspondingBase;

    public Mtrl(String imgURL, String code, String name, String sales, String manufacturer, String correspondingBase) {
        this.imgURL = imgURL;
        this.code = code;
        this.name = name;
        this.sales = sales;
        this.manufacturer = manufacturer;
        this.correspondingBase = correspondingBase;
    }

    public void loadImage(){
        GsonWorker gsonWorker = new GsonWorker(correspondingBase);
        image = gsonWorker.getImage(correspondingBase, imgURL);




    }


    public Drawable getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    protected Mtrl(Parcel in) {
        imgURL = in.readString();
        code = in.readString();
        name = in.readString();
        sales = in.readString();
        manufacturer = in.readString();
        price = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgURL);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(sales);
        dest.writeString(manufacturer);
        dest.writeString(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Mtrl> CREATOR = new Parcelable.Creator<Mtrl>() {
        @Override
        public Mtrl createFromParcel(Parcel in) {
            return new Mtrl(in);
        }

        @Override
        public Mtrl[] newArray(int size) {
            return new Mtrl[size];
        }
    };
}