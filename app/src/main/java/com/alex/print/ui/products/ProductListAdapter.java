package com.alex.print.ui.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.print.databinding.ItemProductBinding;
import com.alex.print.repository.db.entity.Product;


public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.ProductVh> {

    private LayoutInflater inflater;

    public ProductListAdapter(Context context) {
        super(DIFF_CALLBACK);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProductVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductVh(ItemProductBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVh holder, int position) {
        Product product = getItem(position);
        if (product != null) {
            holder.bind(product, position);
        }
    }

    private static DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Product>() {

                @Override
                public boolean areItemsTheSame(Product oldObject, @NonNull Product newObject) {
                    return oldObject.equals(newObject);
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Product oldObject, @NonNull Product newObject) {
                    return oldObject.equals(newObject);
                }
            };

    class ProductVh extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        int position;

        ProductVh(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product, int position) {
            this.position = position;
            binding.name.setText(product.getName());
            binding.supplier.setText(product.getSupplier());
        }
    }
}