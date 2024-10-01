package com.example.organizaiapp.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizaiapp.R;
import com.example.organizaiapp.dto.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private List<Categoria> categorias;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Categoria categoria);
    }

    public CategoriaAdapter(List<Categoria> categorias, OnCategoryClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.bind(categoria, listener);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        ImageView iconeCategoria;
        TextView nomeCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            iconeCategoria = itemView.findViewById(R.id.icone_categoria);
            nomeCategoria = itemView.findViewById(R.id.nome_categoria);
        }

        public void bind(final Categoria categoria, final OnCategoryClickListener listener) {
            iconeCategoria.setImageResource(categoria.getIcone());
            nomeCategoria.setText(categoria.getNome());
            itemView.setOnClickListener(v -> listener.onCategoryClick(categoria));
        }
    }
}
