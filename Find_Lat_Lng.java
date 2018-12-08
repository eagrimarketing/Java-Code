package my.project.agrim;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Vivek on 30-07-2017.
 */

public class Find_Lat_Lng {

    private static final String TAG = "Geo codingLocation";
    Latlng ll;

    ArrayList<String> ss;

    public Latlng getAddressFromLocation(final String locationAddress, final Context context)
    {
        ll = new Latlng(0.0,0.0);
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                String result = null;
////                ll = new Latlng(0.0,0.0);
//                try {
//                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
//                    if (addressList != null && addressList.size() > 0)
//                    {
//                        Address address = (Address) addressList.get(0);
//////                        StringBuilder sb = new StringBuilder();
////                        sb.append(address.getLatitude()).append("\n");
////                        sb.append(address.getLongitude()).append("\n");
////                        result = sb.toString();
//                        ll.setLongitude(address.getLongitude());
//                        ll.setLatitude(address.getLatitude());
//                    }
//                } catch (IOException e) {
//                    Log.e(TAG, "Unable to connect to Geocoder", e);
//                }
////                finally {
////                    Message message = Message.obtain();
////                    message.setTarget(handler);
////                    if (result != null) {
////                        message.what = 1;
////                        Bundle bundle = new Bundle();
////                        result = "Address: " + locationAddress +
////                                "\n\nLatitude and Longitude :\n" + result;
////                        bundle.putString("address", result);
////                        message.setData(bundle);
////                    } else {
////                        message.what = 1;
////                        Bundle bundle = new Bundle();
////                        result = "Address: " + locationAddress +
////                                "\n Unable to get Latitude and Longitude for this address location.";
////                        bundle.putString("address", result);
////                        message.setData(bundle);
////                    }
////                    message.sendToTarget();
////                }
////                }
////            }
////        };
////        thread.start();
//
//        Toast.makeText(context,"result:"+ ll,Toast.LENGTH_LONG).show();
        return ll;
    }

}