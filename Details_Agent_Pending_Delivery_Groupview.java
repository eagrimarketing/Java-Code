package my.project.agrim;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Agent_Pending_Delivery_Groupview {

    String Count, Buyer_ID, Buyer_Name, Buyer_ContactNum, Farmer_Name;

    public Details_Agent_Pending_Delivery_Groupview(String order_Count, String order_Buyer_ID, String order_Buyer_Name, String order_Buyer_ContactNum, String order_Farmer_Name)
    {
        Count = order_Count;
        Buyer_ID = order_Buyer_ID;
        Buyer_Name = order_Buyer_Name;
        Buyer_ContactNum = order_Buyer_ContactNum;
        Farmer_Name = order_Farmer_Name;
     }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getBuyer_ID() {
        return Buyer_ID;
    }

    public void setBuyer_ID(String buyer_ID) {
        Buyer_ID = buyer_ID;
    }

    public String getBuyer_Name() {
        return Buyer_Name;
    }

    public void setBuyer_Name(String buyer_Name) {
        Buyer_Name = buyer_Name;
    }

    public String getBuyer_ContactNum() {
        return Buyer_ContactNum;
    }

    public void setBuyer_ContactNum(String buyer_ContactNum) {
        Buyer_ContactNum = buyer_ContactNum;
    }

    public String getFarmer_Name() {
        return Farmer_Name;
    }

    public void setFarmer_Name(String farmer_Name) {
        Farmer_Name = farmer_Name;
    }
}
