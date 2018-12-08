package my.project.agrim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static my.project.agrim.R.id.buyer_order_cancel_cardview;
import static my.project.agrim.R.id.buyer_order_closed_cardview;
import static my.project.agrim.R.id.buyer_order_pending_cardview;
import static my.project.agrim.R.id.buyer_order_place_cardview;
import static my.project.agrim.R.id.buyer_register_shop_cardview;

//import android.support.annotation;


public class Homepage_Buyer extends AppCompatActivity {

    //    GridView gv;
    String data = "logout";
    String Occupation = null;
    String Text_For_Intent_Action = "Action";
    String Text_For_Intent_Cancel = "Cancel";
    String Text_For_Intent_History = "History";
    //    CardView add_view_crop;
//    CardView add_view_farm;
//    CardView farmer_pending_action;
//    CardView view_orders_cancelled;
//    CardView view_orders_closed;
    @BindView(buyer_order_place_cardview) CardView buyer_order_place;
    @BindView(buyer_order_cancel_cardview) CardView buyer_order_cancel;
    @BindView(buyer_order_pending_cardview) CardView buyer_order_pending;
    @BindView(buyer_order_closed_cardview) CardView buyer_order_closed;
    @BindView(buyer_register_shop_cardview) CardView buyer_register_shop;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage__buyer);
        ButterKnife.bind(this);

    }






    @OnClick(R.id.buyer_order_place_cardview)
    public void itemClick()
    {
        Intent i1 = new Intent(this,Crop_ordering.class);
        startActivity(i1);
    }

    @OnClick(R.id.buyer_register_shop_cardview)
    void registershop()
    {
        Intent i2 = new Intent(this,Registerbuyershop.class);
        startActivity(i2);
    }

    @OnClick(R.id.buyer_order_pending_cardview)
    void pendingorderclick()
    {
        Intent i3 = new Intent(this,ViewOrderlist_Buyer_Open_Order.class);
        startActivity(i3);
    }

    @OnClick(R.id.buyer_order_cancel_cardview)
    void cancelledorderclick()
    {
        Intent i4 = new Intent(this,ViewOrderlist_Buyer_Info_Order_Cancelled.class);
        startActivity(i4);
    }

    @OnClick(R.id.buyer_order_closed_cardview)
    void closedorderclick()
    {
        Intent i5 = new Intent(this,ViewOrderlist_Buyer_Info_Order_Closed.class);
        startActivity(i5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_homepage_listview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.buyer_order_place_cardview:
                Toast.makeText(this,"inside crop view",Toast.LENGTH_LONG).show();
                break;
            case R.id.buyer_order_cancel_cardview:
                Toast.makeText(this,"inside farm view",Toast.LENGTH_LONG).show();
                break;
            case R.id.buyer_order_pending_cardview:
                Toast.makeText(this,"inside pending action ",Toast.LENGTH_LONG).show();
                break;
            case R.id.buyer_order_closed_cardview:
                Toast.makeText(this,"inside cancelled order",Toast.LENGTH_LONG).show();
                break;
            case R.id.buyer_register_shop_cardview:
                Toast.makeText(this,"inside closed order",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_logout:
                SharedPreferences sp = getSharedPreferences("agrimuser", MODE_PRIVATE);
                SharedPreferences.Editor spe = sp.edit();
                spe.clear();
                spe.commit();

                Intent lo = new Intent(getApplicationContext(), Entry.class);
                startActivity(lo);
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
