package my.project.agrim;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Agent_Pending_Delivery_Mapview implements Parcelable {

    String Buyer_Name;
    Double Shop_Latittude, Shop_Longitude;

    public Details_Agent_Pending_Delivery_Mapview(String buyer_Name, Double shop_Latittude, Double shop_Longitude) {
        Buyer_Name = buyer_Name;
        Shop_Latittude = shop_Latittude;
        Shop_Longitude = shop_Longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Buyer_Name);
        dest.writeDouble(Shop_Latittude);
        dest.writeDouble(Shop_Longitude);

    }

    //Constructor for parcel
    public Details_Agent_Pending_Delivery_Mapview(Parcel in){
        Buyer_Name = in.readString();
        Shop_Latittude = in.readDouble();
        Shop_Longitude = in.readDouble();
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Creator<Details_Agent_Pending_Delivery_Mapview> CREATOR = new Creator<Details_Agent_Pending_Delivery_Mapview>()
    {

        @Override
        public Details_Agent_Pending_Delivery_Mapview createFromParcel(Parcel source) {
            return new Details_Agent_Pending_Delivery_Mapview(source);
        }

        @Override
        public Details_Agent_Pending_Delivery_Mapview[] newArray(int size) {
            return new Details_Agent_Pending_Delivery_Mapview[size];
        }
    };


    public String getBuyer_Name() {
        return Buyer_Name;
    }

    public void setBuyer_Name(String buyer_Name) {
        Buyer_Name = buyer_Name;
    }

    public Double getShop_Latittude() {
        return Shop_Latittude;
    }

    public void setShop_Latittude(Double shop_Latittude) {
        Shop_Latittude = shop_Latittude;
    }

    public Double getShop_Longitude() {
        return Shop_Longitude;
    }

    public void setShop_Longitude(Double shop_Longitude) {
        Shop_Longitude = shop_Longitude;
    }

}
