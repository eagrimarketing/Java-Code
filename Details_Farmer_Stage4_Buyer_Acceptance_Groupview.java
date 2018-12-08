package my.project.agrim;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Farmer_Stage4_Buyer_Acceptance_Groupview {

    String Count, Buyer_ID, Buyer_Name, Buyer_ContactNum, Agent_Name;

    public Details_Farmer_Stage4_Buyer_Acceptance_Groupview(String order_Count, String order_Buyer_ID, String order_Buyer_Name, String order_Buyer_ContactNum, String order_Agent_Name)
    {
        Count = order_Count;
        Buyer_ID = order_Buyer_ID;
        Buyer_Name = order_Buyer_Name;
        Buyer_ContactNum = order_Buyer_ContactNum;
        Agent_Name = order_Agent_Name;
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

    public String getAgent_Name() {
        return Agent_Name;
    }

    public void setAgent_Name(String agent_Name) {
        Agent_Name = agent_Name;
    }
}
