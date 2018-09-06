package hr.v2d.katic.phpdatabasetest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView pid;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            pid = itemView.findViewById(R.id.pid);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        private void bind(int position){
            Product p = mProductList.get(position);
            pid.setText(p.getPid());
            name.setText(p.getName());
        }

        @Override
        public void onClick(View view) {
            mListener.onProductClicked(mProductList.get(getAdapterPosition()));
        }
    }

    private List<Product> mProductList;
    private AllProductsListener mListener;

    public AllProductsAdapter(AllProductsListener listener) {
        mProductList = new ArrayList<>();
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public void swapData(List<Product> productList){
        mProductList = productList;
        notifyDataSetChanged();
    }

    public interface AllProductsListener {

        void onProductClicked(Product product);

    }

}
