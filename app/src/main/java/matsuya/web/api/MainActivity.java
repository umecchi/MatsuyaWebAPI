package matsuya.web.api;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * JSONデータ取得URL
     */
    private final String MATSUYA_URL = "https://matsuya.makotia.me/v4/random";

    /**
     * HTTPリクエスト管理Queue
     */
    private RequestQueue mQueue;

    private TextView NameText;
    private TextView TypeText;
    private TextView PriceText;
    private TextView CalorieText;
    private TextView ProteinText;
    private TextView LipidText;
    private TextView CarbohydrateText;
    private TextView SodiumText;
    private TextView SaltEquivalentText;
    private TextView DescriptionText;
    private ImageView imageView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);

        // 色設定
        mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);
        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this);

        NameText = findViewById(R.id.name);
        TypeText = findViewById(R.id.type);
        PriceText = findViewById(R.id.price);
        CalorieText = findViewById(R.id.calorie);
        ProteinText = findViewById(R.id.protein);
        LipidText = findViewById(R.id.lipid);
        CarbohydrateText = findViewById(R.id.carbohydrate);
        SodiumText = findViewById(R.id.sodium);
        SaltEquivalentText = findViewById(R.id.saltEquivalent);
        DescriptionText = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void onRefresh() {
        matsuya();
    }

    private void matsuya() {
        mQueue = Volley.newRequestQueue(this);
        // リクエスト実行
        mQueue.add(new JsonObjectRequest(Method.GET, MATSUYA_URL, (JSONObject) null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    String type = response.getString("type");
                    String price = response.getString("price");//税込
                    String calorie = response.getString("calorie");//カロリー
                    String protein = response.getString("protein");//たんぱく質
                    String lipid = response.getString("lipid");//脂質
                    String carbohydrate = response.getString("carbohydrate");//炭水化物
                    String sodium = response.getString("sodium");//ナトリウム
                    String saltEquivalent = response.getString("saltEquivalent");//食塩相当量
                    String description = response.getString("description");
                    String imageURL = response.getString("imageURL");
                    NameText.setText(name);
                    TypeText.setText(type);
                    PriceText.setText("価格：" + price + "円(税込)");
                    CalorieText.setText("カロリー：" + calorie + "kcal");
                    ProteinText.setText("たんぱく質：" + protein + "g");
                    LipidText.setText("脂質：" + lipid + "g");
                    CarbohydrateText.setText("炭水化物：" + carbohydrate + "g");
                    SodiumText.setText("ナトリウム：" + sodium + "mg");
                    SaltEquivalentText.setText("食塩相当量：" + saltEquivalent + "g");
                    DescriptionText.setText(description);
                    imageView.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse(imageURL);
                    Uri.Builder builder = uri.buildUpon();
                    AsyncTaskHttpRequest task = new AsyncTaskHttpRequest(imageView);
                    task.execute(builder);
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                } catch (JSONException e) {
                    NameText.setText("error");
                    TypeText.setText("error");
                    PriceText.setText("error");
                    CalorieText.setText("error");
                    ProteinText.setText("error");
                    LipidText.setText("error");
                    CarbohydrateText.setText("error");
                    SodiumText.setText("error");
                    SaltEquivalentText.setText("error");
                    DescriptionText.setText("error");
                    imageView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // エラー処理 error.networkResponseで確認
                //if (error.networkResponse != null) {
                    NameText.setText("error");
                    TypeText.setText("error");
                    PriceText.setText("error");
                    CalorieText.setText("error");
                    ProteinText.setText("error");
                    LipidText.setText("error");
                    CarbohydrateText.setText("error");
                    SodiumText.setText("error");
                    SaltEquivalentText.setText("error");
                    DescriptionText.setText("error");
                    imageView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                //}

            }
        }));
    }
}
