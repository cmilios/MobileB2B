package gr.logistic_i.logistic_i;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Mtrl implements Parcelable {
    String img;
    String code;
    String name;
    String sales;
    String manufacturer;
    String price;

    public Mtrl(String img, String code, String name, String sales, String manufacturer) {
        this.img = img;
        this.code = code;
        this.name = name;
        this.sales = sales;
        this.manufacturer = manufacturer;
    }

    public Bitmap makeStringToBitmap(){
        img = img.replace("0x", "");

        Bitmap bmp = StringToBitMap(img);
        return bmp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {

            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
        img = in.readString();
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
        dest.writeString(img);
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