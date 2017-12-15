package matsuya.web.api;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /**
     * JSONデータ取得URL
     */
    private final String MATSUYA_URL = "https://matsuya-api.herokuapp.com/v2/random";

    /**
     * HTTPリクエスト管理Queue
     */
    private RequestQueue mQueue;

    private TextView MatsuyaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MatsuyaText = findViewById(R.id.matsuya_text);

        findViewById(R.id.matsuya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matsuya();
            }
        });
    }
    private void matsuya() {
        mQueue = Volley.newRequestQueue(this);
        // リクエスト実行
        mQueue.add(new JsonObjectRequest(Method.GET, MATSUYA_URL, (JSONObject) null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String str = response.getString("menu");
                    MatsuyaText.setText(str);
                    return;
                } catch (JSONException e) {
                    MatsuyaText.setText(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // エラー処理 error.networkResponseで確認
                if (error.networkResponse != null) {
                    MatsuyaText.setText(error.getMessage());
                }

            }
        }));
    }
}
