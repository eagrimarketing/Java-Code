package my.project.agrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static my.project.agrim.R.id.stage1_planning_cardview;
import static my.project.agrim.R.id.stage2_buyer_acceptance_cardview;
import static my.project.agrim.R.id.stage3_buyer_payment_cardview;
import static my.project.agrim.R.id.stage4_agent_payment_acceptance_cardview;

//import android.support.annotation;


public class ViewOrderlist_Buyer_Open_Order extends AppCompatActivity {

    @BindView(stage1_planning_cardview) CardView stage1_planning;
    @BindView(stage2_buyer_acceptance_cardview) CardView stage2_buyer_acceptance;
    @BindView(stage3_buyer_payment_cardview) CardView stage3_buyer_payment;
    @BindView(stage4_agent_payment_acceptance_cardview) CardView stage4_agent_payment_acceptance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_buyer_open_order);
        ButterKnife.bind(this);
    }

    @OnClick (R.id.stage1_planning_cardview)
    public void planning()
    {
        Intent i1 = new Intent(this,ViewOrderlist_Buyer_Order_Stage1_Groupview.class);
        startActivity(i1);
    }

    @OnClick (R.id.stage2_buyer_acceptance_cardview)
    public void acceptance()
    {
        Intent i2 = new Intent(this,ViewOrderlist_Buyer_Order_Stage2_Groupview.class);
        startActivity(i2);
    }

    @OnClick (R.id.stage3_buyer_payment_cardview)
    public void payment()
    {
        Intent i3 = new Intent(this,ViewOrderlist_Buyer_Order_Stage3_Groupview.class);
        startActivity(i3);
    }

    @OnClick (R.id.stage4_agent_payment_acceptance_cardview)
    public void agentpayaccept()
    {
        Intent i4 = new Intent(this,ViewOrderlist_Buyer_Order_Stage4_Pending_Agent_Accept.class);
        startActivity(i4);
    }


}
