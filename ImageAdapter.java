package my.project.agrim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{

    private Context mContext;
    LayoutInflater li;
    String Occ = null;

    public int[] imagelist_farmer = {R.drawable.farmer_reg_crop,R.drawable.farmer_reg_farm,R.drawable.farmer_pending_action,R.drawable.farmer_cancelled_orders,R.drawable.farmer_closed_orders};
    public int[] imagelist_agent = {R.drawable.agent_update_details,R.drawable.agent_pending_acceptance,R.drawable.agent_pending_delivery,R.drawable.agent_postdeelivery_status,R.drawable.agent_closed_orders};
    public int[] imagelist_buyer = {R.drawable.buyer_order_place,R.drawable.buyer_register_shop,R.drawable.buyer_order_pending,R.drawable.buyer_order_cancel,R.drawable.buyer_order_closed};

  public ImageAdapter (Context C, String Occupation)
  {
    mContext=C;
    li= (LayoutInflater.from(C));
      Occ = Occupation;
  }

    public int getCount()
    {
//        return imagelist_agent.length;
        int cnt=0;

        if (Occ.contentEquals("Agent"))
        {cnt = imagelist_agent.length;}
        if (Occ.contentEquals("Farmer"))
        { cnt =  imagelist_farmer.length;}
        if (Occ.contentEquals("Buyer/Store Owner"))
        {cnt = imagelist_buyer.length;}

        return cnt;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView (int position, View view, ViewGroup vg)
    {
//        view=li.inflate(R.layout.homepage_gridview,null);
        view = li.inflate(R.layout.homepage_gridview,null);
        ImageView iv = (ImageView)view.findViewById(R.id.icon);

//        iv.setLayoutParams(new ViewGroup.LayoutParams(85,85));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setPadding(8,8,8,8);

        if (Occ.contentEquals("Agent"))
        {iv.setImageResource(imagelist_agent[position]);}
        if (Occ.contentEquals("Farmer"))
        {iv.setImageResource(imagelist_farmer[position]);}
        if (Occ.contentEquals("Buyer/Store Owner"))
        {iv.setImageResource(imagelist_buyer[position]);}

        return view;

    }


}
