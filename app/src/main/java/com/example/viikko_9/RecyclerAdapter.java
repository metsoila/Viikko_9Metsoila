package com.example.viikko_9;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";
    List<Elokuvatiedot> lista;

    public RecyclerAdapter(List<Elokuvatiedot> lista){
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rivi_elementti, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pteksti.setText(lista.get(position).Aika);
        holder.iteksti.setText(lista.get(position).Elokuva);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView iteksti, pteksti;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iteksti = itemView.findViewById(R.id.Isoteksti); //elokuva
            pteksti = itemView.findViewById(R.id.alateksti); //kellonaika
        }
    }



}
