package my.project.agrim;

/**
 * Created by Vivek on 25-06-2017.
 */
public class Details_Farmer_Stage2_Groupview {

    String Count, Agent_ID, Agent_Name, Agent_ContactNum, Buyer_Name;

    public Details_Farmer_Stage2_Groupview(String order_Count, String order_Agent_ID, String order_Agent_Name, String order_Agent_ContactNum, String order_Buyer_Name)
    {
        Count = order_Count;
        Agent_ID = order_Agent_ID;
        Agent_Name = order_Agent_Name;
        Agent_ContactNum = order_Agent_ContactNum;
        Buyer_Name = order_Buyer_Name;
     }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getAgent_ID() {
        return Agent_ID;
    }

    public void setAgent_ID(String agent_ID) {
        Agent_ID = agent_ID;
    }

    public String getAgent_Name() {
        return Agent_Name;
    }

    public void setAgent_Name(String agent_Name) {
        Agent_Name = agent_Name;
    }

    public String getAgent_ContactNum() {
        return Agent_ContactNum;
    }

    public void setAgent_ContactNum(String agent_ContactNum) {
        Agent_ContactNum = agent_ContactNum;
    }

    public String getBuyer_Name() {
        return Buyer_Name;
    }

    public void setBuyer_Name(String buyer_Name) {
        Buyer_Name = buyer_Name;
    }
}
