package matsuya.web.api

import android.os.AsyncTask
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    /**
     * JSONデータ取得URL
     */
    private val MATSUYA_URL = "https://matsuya.makotia.me/v4/random"
    private var matsuyaTask: AsyncTask<Void?, Void?, JSONObject>? = null

    private var NameText: TextView? = null
    private var TypeText: TextView? = null
    private var PriceText: TextView? = null
    private var CalorieText: TextView? = null
    private var ProteinText: TextView? = null
    private var LipidText: TextView? = null
    private var CarbohydrateText: TextView? = null
    private var SodiumText: TextView? = null
    private var SaltEquivalentText: TextView? = null
    private var DescriptionText: TextView? = null
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSwipeRefreshLayout = findViewById<View>(R.id.swipelayout) as SwipeRefreshLayout

        // 色設定
        mSwipeRefreshLayout!!.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow)
        // Listenerをセット
        mSwipeRefreshLayout!!.setOnRefreshListener(this)

        NameText = findViewById(R.id.name)
        TypeText = findViewById(R.id.type)
        PriceText = findViewById(R.id.price)
        CalorieText = findViewById(R.id.calorie)
        ProteinText = findViewById(R.id.protein)
        LipidText = findViewById(R.id.lipid)
        CarbohydrateText = findViewById(R.id.carbohydrate)
        SodiumText = findViewById(R.id.sodium)
        SaltEquivalentText = findViewById(R.id.saltEquivalent)
        DescriptionText = findViewById(R.id.description)
        imageView = findViewById(R.id.imageView)

        matsuya()
    }

    override fun onRefresh() {
        matsuya()
    }

    private fun matsuya() {
        if (matsuyaTask != null && matsuyaTask?.getStatus() === AsyncTask.Status.RUNNING) {
            return
        }
        matsuyaTask = object : AsyncTask<Void?, Void?, JSONObject>() {

            override fun doInBackground(vararg params: Void?): JSONObject? {
                try {
                    val url = MATSUYA_URL

                    val request = Request.Builder()
                            .url(url)
                            .build()

                    val okHttpClient = OkHttpClient.Builder()
                            .build()

                    okHttpClient.newCall(request).execute().use { response ->
                        val responseCode = response.code()
                        println("responseCode: $responseCode")

                        if (!response.isSuccessful) {
                            println("error!!")
                            return null
                        }
                        if (response.body() != null) {
                            try {
                                val body = response.body()?.string().toString()
                                println("body: $body")
                                val json = JSONObject(body)
                                return json
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                return null
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }
                return null
            }

            override fun onPostExecute(json: JSONObject) {
                if (json != null) {
                    Log.d("matsuya", "ok")
                    val name: String = json.getString("name")
                    val type: String = json.getString("type")
                    val price: String = json.getString("price")//税込
                    val calorie: String = json.getString("calorie")//カロリー
                    val protein: String = json.getString("protein")//たんぱく質
                    val lipid: String = json.getString("lipid")//脂質
                    val carbohydrate: String = json.getString("carbohydrate")//炭水化物
                    val sodium: String = json.getString("sodium")//ナトリウム
                    val saltEquivalent: String = json.getString("saltEquivalent")//食塩相当量
                    val description: String = json.getString("description")
                    val imageURL: String = json.getString("imageURL")
                    NameText?.setText(name)
                    TypeText?.setText(type)
                    PriceText?.setText("価格：" + price + "円(税込)")
                    CalorieText?.setText("カロリー：" + calorie + "kcal")
                    ProteinText?.setText("たんぱく質：" + protein + "g")
                    LipidText?.setText("脂質：" + lipid + "g")
                    CarbohydrateText?.setText("炭水化物：" + carbohydrate + "g")
                    SodiumText?.setText("ナトリウム：" + sodium + "mg")
                    SaltEquivalentText?.setText("食塩相当量：" + saltEquivalent + "g")
                    if (description == "null") {
                        DescriptionText?.setText("")
                    } else {
                        DescriptionText?.setText(description)
                    }
                    if (imageURL == "null") {
                        imageView?.setVisibility(View.GONE)
                    } else {
                        imageView?.let { Glide.with(this@MainActivity).load(imageURL).apply(RequestOptions().centerInside()).into(it) }
                        imageView?.setVisibility(View.VISIBLE)
                    }
                    mSwipeRefreshLayout?.setRefreshing(false)
                } else {
                    Log.d("matsuya", "ng")
                    NameText?.setText("error")
                    TypeText?.setText("error")
                    PriceText?.setText("error")
                    CalorieText?.setText("error")
                    ProteinText?.setText("error")
                    LipidText?.setText("error")
                    CarbohydrateText?.setText("error")
                    SodiumText?.setText("error")
                    SaltEquivalentText?.setText("error")
                    DescriptionText?.setText("error")
                    imageView?.setVisibility(View.GONE)
                    mSwipeRefreshLayout?.setRefreshing(false)
                }
            }
        }
        matsuyaTask?.execute()
    }
}
