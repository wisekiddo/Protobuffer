package com.wisekiddo.protobuf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.wisekiddo.protobuf.mokapos.ItemOuterClass;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import android.util.Base64InputStream;


import static com.wisekiddo.protobuf.mokapos.ItemsOuterClass.Items;
import static com.wisekiddo.protobuf.mokapos.ItemOuterClass.Item;


public class MainActivity extends AppCompatActivity {

    private static OkHttpClient okHttpClient;
    private static int REQUEST_TIMEOUT = 60;
//https://service-dev.mokapos.com/library/v1/item-library
    private Observable observable = Observable.just("https://service-dev.mokapos.com/library/v1/item-library?page=0&outlet_id=743&is_mobile=false&is_deleted=0&per_page=20000&since=547010645.467711")
            .map(new Function<String, Items>() {
        @Override
        public Items apply(String url) throws Exception {

            if (okHttpClient == null)
                initOkHttp();

            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();

                if (responseBody != null) {

                //    Log.i("FFFF",ByteString.of(responseBody.source().readByte()).utf8());
                 //   responseBody.
                   // InputStream in = responseBody;
                   // BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  // ByteString  result = responseBody.source().readByteString();

                  // result = line;
                   // result.utf8()
                   //List<Item> itemsList = Items.parseFrom(responseBody.byteStream());//.getItemsList();


                   // String s2 = new String(result, "UTF-8");
                    //byte[] encodeValue = Base64.encode(testValue.getBytes(), Base64.DEFAULT);
                 //   byte[] decodeValue = Base64.decode(result.base64(), Base64.DEFAULT);

                    //Log.d("TEST", "defaultValue = " + testValue);
                   // Log.d("TEST", "encodeValue = " + new String(encodeValue));
                   // Log.d("TEST", "decodeValue = " + new String(result.utf8()));

                   // Log.i("-----",   result.hex()+"");
                 //  int charCode = Integer.parseInt(responseBody.source().readUtf8(), 2);
                 //  String str = new Character((char)charCode).toString();

                 //   Log.i("-----", str+"");

                   // for(Item item:itemsList){
                     //  Log.i("-----", item.getName()+"");
                  // }
                  /// while((line = reader.readLine()) != null) {
                      // result += line;
                  // }
                    //JSONObject jsonObject = new JSONObject(result);


                    return Items.parseFrom(responseBody.byteStream());

                }
            }

            return null;
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object object) throws Exception {

                showResult((Items) object);
            }
        });

    }

    private void showResult(Items result) {
        TextView textView = findViewById(R.id.txt_main);

        Log.i("----", result.getItemsList().size()+ " ");

        for( Item item:result.getItemsList()){
            Log.i("ITEM", item.getName());
        }
        //textView.setText(result.getItems().getDescription()+"--");
    }


    private static void initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/x-protobuf")
                        .addHeader("Content-Type", "application/x-protobuf")
                        .addHeader("Cookie", "remember_token=NegX-uDLHBV6Q4bKmIaL_O; Domain=.mokapos.com; Path=/;")
                        .addHeader("Authorization", "NegX-uDLHBV6Q4bKmIaL_O")
                        .addHeader("outlet_id", "743")
                        .addHeader("User-Agent", "MokaDev/10.8 (Tab; Android 5.1.1; v:77:1525682231268; Samsung SM-T285");
                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                // if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
                //    requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
                //}

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();




    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char)decimal);
        }
        return sb.toString();
    }


}
