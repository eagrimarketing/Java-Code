package my.project.agrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static my.project.agrim.R.id.stage1fm_farmer_acceptance_cardview;
import static my.project.agrim.R.id.stage2fm_agent_acceptance_cardview;
import static my.project.agrim.R.id.stage3fm_agent_pending_delivery_cardview;
import static my.project.agrim.R.id.stage4fm_buyer_pending_acceptance_cardview;
import static my.project.agrim.R.id.stage5fm_buyer_pending_payment_cardview;
import static my.project.agrim.R.id.stage6fm_agent_pending_acceptance_cardview;

//import android.support.annotation;


public class ViewOrderlist_Farmer_Open_Order extends AppCompatActivity {

    @BindView(stage1fm_farmer_acceptance_cardview) CardView stage1_farmer_accept;
    @BindView(stage2fm_agent_acceptance_cardview) CardView stage2_agent_accept_order;
    @BindView(stage3fm_agent_pending_delivery_cardview) CardView stage3_agent_delivery;
    @BindView(stage4fm_buyer_pending_acceptance_cardview) CardView stage4_buyer_acceptance;
    @BindView(stage5fm_buyer_pending_payment_cardview) CardView stage5_buyer_pay_pending;
    @BindView(stage6fm_agent_pending_acceptance_cardview) CardView stage6_agent_accept_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_farmer_open_order);
        ButterKnife.bind(this);
    }

    @OnClick (R.id.stage1fm_farmer_acceptance_cardview)
    public void planning()
    {
        Intent i1 = new Intent(this,ViewOrderlist_Farmer_Pending_Action.class);
        startActivity(i1);
    }

    @OnClick (R.id.stage2fm_agent_acceptance_cardview)
    public void acceptance()
    {
        Intent i2 = new Intent(this,ViewOrderlist_Farmer_Order_Stage2_Groupview_Pending_Agent_Accept.class);
        startActivity(i2);
    }

    @OnClick (R.id.stage3fm_agent_pending_delivery_cardview)
    public void delivery()
    {
        Intent i3 = new Intent(this,ViewOrderlist_Farmer_Order_Stage3_Groupview_Pending_Delivery.class);
        startActivity(i3);
    }

    @OnClick (R.id.stage4fm_buyer_pending_acceptance_cardview)
    public void buyeraccept()
    {
        Intent i4 = new Intent(this,ViewOrderlist_Farmer_Order_Stage4_Buyer_Acceptance_Groupview.class);
        startActivity(i4);
    }

    @OnClick (R.id.stage5fm_buyer_pending_payment_cardview)
    public void buyerpayment()
    {
        Intent i5 = new Intent(this,ViewOrderlist_Farmer_Order_Stage5_Pending_Buyer_Payment.class);
        startActivity(i5);
    }
    @OnClick (R.id.stage6fm_agent_pending_acceptance_cardview)
    public void agentpayaccept()
    {
        Intent i6 = new Intent(this,ViewOrderlist_Farmer_Order_Stage6_Pending_Agent_Acceptance.class);
        startActivity(i6);
    }

}
