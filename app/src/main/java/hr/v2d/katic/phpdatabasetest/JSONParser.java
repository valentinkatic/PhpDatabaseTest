package hr.v2d.katic.phpdatabasetest;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    static String responseString = null;

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET method
    public ApiResponse makeHttpRequest(String url, String method,
                                      Product product) {

        Gson gson = new Gson();

        // Making HTTP request
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
            // check for request method
            if(method.equals("POST")){
                // request method is POST

                RequestBody body;
                if (product != null) {
                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add(EditProductActivity.TAG_PID, product.pid == null ? "" : product.pid)
                            .add(EditProductActivity.TAG_NAME, product.name)
                            .add(EditProductActivity.TAG_DESCRIPTION, product.description)
                            .add(EditProductActivity.TAG_PRICE, product.price);

                    body = formBuilder.build();
                } else {
                    body = new FormBody.Builder().build();
                }

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                responseString = response.body().string();


            } else if(method.equals("GET")){
                // request method is GET

                HttpUrl.Builder urlBuilder
                        = HttpUrl.parse(url).newBuilder();
                if (product != null) {
                    urlBuilder.addQueryParameter(EditProductActivity.TAG_PID, product.pid);
                }

                Log.d("JSONParse url", urlBuilder.build().toString());

                Request request = new Request.Builder()
                        .url(urlBuilder.build().toString())
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                responseString = response.body().string();

            }

        } catch (IOException|NullPointerException e) {
            e.printStackTrace();
        }

        Log.d("JSONParse", " " + responseString);

        return gson.fromJson(responseString, ApiResponse.class);
    }

}
