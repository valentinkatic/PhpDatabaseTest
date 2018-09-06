package hr.v2d.katic.phpdatabasetest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity implements AllProductsAdapter.AllProductsListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    List<Product> productsList;
    AllProductsAdapter adapter;
    RecyclerView recyclerView;

    private static final String TAG_PID = "pid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        // Hashmap for ListView
        productsList = new ArrayList<>();
        adapter = new AllProductsAdapter(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        // Loading products in Background Thread
        new LoadAllProducts().execute();
    }

    @Override
    public void onProductClicked(Product product) {
        // Starting new intent
        Intent in = new Intent(getApplicationContext(),
                EditProductActivity.class);
        // sending pid to next activity
        in.putExtra(TAG_PID, product.getPid());

        // starting new activity and expecting some response back
        startActivityForResult(in, 100);
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    @SuppressLint("StaticFieldLeak")
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(AllProductsActivity.this);
//            pDialog.setMessage("Loading products. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // getting JSON string from URL
            ApiResponse response = jParser.makeHttpRequest(Utils.GET_ALL_PRODUCTS_URL, "GET", null);

            if (response.getSuccess() == 1){
                productsList = response.getProducts();
            } else {
                // no products found
                Toast.makeText(AllProductsActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();

                // Launch Add New product Activity
                Intent i = new Intent(getApplicationContext(),
                        NewProductActivity.class);
                // Closing all previous activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            adapter.swapData(productsList);
        }

    }
}
