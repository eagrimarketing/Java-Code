package my.project.agrim;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Buyer_Stage1_Groupview {

    String Order_Count, Order_Farmer_ID, Order_Farmer_Name, Farmer_ContactNum;

    public Details_Buyer_Stage1_Groupview(String Count, String order_Farmer_ID, String order_Farmer_Name, String O_Farmer_ContactNum)
    {
        Order_Count = Count;
        Order_Farmer_ID = order_Farmer_ID;
        Order_Farmer_Name = order_Farmer_Name;
        Farmer_ContactNum = O_Farmer_ContactNum;
    }

    public String getOrder_Count() {
        return Order_Count;
    }

    public void setOrder_Count(String order_Count) {
        Order_Count = order_Count;
    }

    public String getOrder_Farmer_ID() {
        return Order_Farmer_ID;
    }

    public void setOrder_Farmer_ID(String order_Farmer_ID) {
        Order_Farmer_ID = order_Farmer_ID;
    }

    public String getOrder_Farmer_Name() {
        return Order_Farmer_Name;
    }

    public void setOrder_Farmer_Name(String order_Farmer_Name) {
        Order_Farmer_Name = order_Farmer_Name;
    }

    public String getFarmer_ContactNum() {
        return Farmer_ContactNum;
    }

    public void setFarmer_ContactNum(String farmer_ContactNum) {
        Farmer_ContactNum = farmer_ContactNum;
    }
}
