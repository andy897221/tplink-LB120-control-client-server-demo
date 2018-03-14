package andy.tplink_togglelight;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog.Builder;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public void sendGETToggle() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        // server ip with the local network
        final String url = "http://0.0.0.0:3000";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        //LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        //alert.setMessage("GET Result: "+response+"\n shutting down.");
                        //alert.show();
                        System.exit(1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        alert.setMessage("GET Result: "+error.toString());
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "toggle");

                return params;
            }
        };
        queue.add(getRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendGETToggle();

        final Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            public void  onClick(View V) {
                sendGETToggle();
            }
        });
    }

}
