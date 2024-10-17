package com.example.organizaiapp.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizaiapp.R;
import com.example.organizaiapp.dto.CategoriaDto;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private List<CategoriaDto> categoriaDtos;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoriaDto categoriaDto);
    }

    public CategoriaAdapter(List<CategoriaDto> categoriaDtos, OnCategoryClickListener listener) {
        this.categoriaDtos = categoriaDtos;
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
        CategoriaDto categoriaDto = categoriaDtos.get(position);
        holder.bind(categoriaDto, listener);
    }

    @Override
    public int getItemCount() {
        return categoriaDtos.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        ImageView iconeCategoria;
        TextView nomeCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            iconeCategoria = itemView.findViewById(R.id.icone_categoria);
            nomeCategoria = itemView.findViewById(R.id.nome_categoria);
        }

        public void bind(final CategoriaDto categoriaDto, final OnCategoryClickListener listener) {
            iconeCategoria.setImageResource(categoriaDto.getCategoriaId());
            nomeCategoria.setText(categoriaDto.getNomeCat());
            itemView.setOnClickListener(v -> listener.onCategoryClick(categoriaDto));
        }
    }
}
