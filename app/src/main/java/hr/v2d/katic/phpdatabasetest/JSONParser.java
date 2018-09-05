package hr.v2d.katic.phpdatabasetest;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
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
    public String makeHttpRequest(String url, String method,
                                      String params) {

        // Making HTTP request
        try {

            OkHttpClient client = new OkHttpClient();
            // check for request method
            if(method.equals("POST")){
                // request method is POST

                Log.d("JSONParser", params);

//                RequestBody body = RequestBody.create(JSON, params);
                Gson gson = new Gson();
                Product p = gson.fromJson(params, Product.class);

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add(EditProductActivity.TAG_NAME, p.name)
                        .add(EditProductActivity.TAG_DESCRIPTION, p.description)
                        .add(EditProductActivity.TAG_PRICE, p.price);

                RequestBody body = formBuilder.build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                responseString = response.body().string();


            } else if(method.equals("GET")){
                // request method is GET

                url += "?" + params;
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                responseString = response.body().string();

            }

        } catch (IOException|NullPointerException e) {
            e.printStackTrace();
        }

        Log.d("JSONParse", " " + responseString);

//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            json = sb.toString();
//        } catch (Exception e) {
//            Log.e("Buffer Error", "Error converting result " + e.toString());
//        }
//
//        // try parse the string to a JSON object
//        try {
//            jObj = new JSONObject(json);
//        } catch (JSONException e) {
//            Log.e("JSON Parser", "Error parsing data " + e.toString());
//        }

        // return JSON String
        return responseString;

    }

}
