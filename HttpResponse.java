package my.project.agrim;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Vivek on 26-02-2017.
 */
public interface HttpResponse
{
    void getResponse(String serverResponse, String responseType);


    void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
