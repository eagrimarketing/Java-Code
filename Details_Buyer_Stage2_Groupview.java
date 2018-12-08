package my.project.agrim;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Buyer_Stage2_Groupview {

    String Order_Count, Order_Farmer_ID, Order_Farmer_Name, Order_Agent_ID, Order_Agent_Name, Farmer_ContactNum, Agent_Contact;

    public Details_Buyer_Stage2_Groupview(String Count, String order_Farmer_ID, String order_Farmer_Name, String order_Agent_ID, String order_Agent_Name, String O_Farmer_ContactNum, String O_Agent_Contact) {
        Order_Count = Count;
        Order_Farmer_ID = order_Farmer_ID;
        Order_Farmer_Name = order_Farmer_Name;
        Order_Agent_ID = order_Agent_ID;
        Order_Agent_Name = order_Agent_Name;
        Farmer_ContactNum = O_Farmer_ContactNum;
        Agent_Contact = O_Agent_Contact;
    }

    public String getFarmer_ContactNum() {
        return Farmer_ContactNum;
    }

    public void setFarmer_ContactNum(String farmer_ContactNum) {
        Farmer_ContactNum = farmer_ContactNum;
    }

    public String getAgent_Contact() {
        return Agent_Contact;
    }

    public void setAgent_Contact(String agent_Contact) {
        Agent_Contact = agent_Contact;
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

    public String getOrder_Agent_ID() {
        return Order_Agent_ID;
    }

    public void setOrder_Agent_ID(String order_Agent_ID) {
        Order_Agent_ID = order_Agent_ID;
    }

    public String getOrder_Agent_Name() {
        return Order_Agent_Name;
    }

    public void setOrder_Agent_Name(String order_Agent_Name) {
        Order_Agent_Name = order_Agent_Name;
    }
}
