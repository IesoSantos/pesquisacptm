package com.metro.pesquisacptm.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.metro.pesquisacptm.R;
import com.metro.pesquisacptm.model.Pesquisa;

public class PesquisaRecycleAdapter extends
        RecyclerView.Adapter<PesquisaRecycleAdapter.PesquisaVH> {

    private List<Pesquisa> lista;

    public PesquisaRecycleAdapter(List<Pesquisa> dados) {
        this.lista = dados;
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }
    static class PesquisaVH extends RecyclerView.ViewHolder {
        TextView tvPesquisador, tvTipoPesquisa, tvDataPesquisa,tvHoraPesquisa,
                tvLinhaPesquisa, tvEstacaoPesquisa, tvAreaPesquisa,
                tvOrigem, tvDestino;
        PesquisaVH(View v){
            super(v);
            tvPesquisador = v.findViewById(R.id.tvPesquisadorItem);
            tvTipoPesquisa = v.findViewById(R.id.tvTipoPesquisaItem);
            tvDataPesquisa = v.findViewById(R.id.tvDataPesquisaItem);
            tvHoraPesquisa = v.findViewById(R.id.tvHoraPesquisaItem);
            tvLinhaPesquisa = v.findViewById(R.id.tvLinhaPesquisaItem);
            tvEstacaoPesquisa = v.findViewById(R.id.tvEstacaoPesquisaItem);
            tvAreaPesquisa = v.findViewById(R.id.tvAreaPesquisaItem);
            tvOrigem = v.findViewById(R.id.tvOrigemItem);
            tvDestino = v.findViewById(R.id.tvDestinoItem);
        }
    }

    @Override
    public PesquisaVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_pesquisa_recycleview, parent,
                false);
        return new PesquisaVH(v);

    }

    @Override
    public void onBindViewHolder(PesquisaVH h, int position) {

        Pesquisa p = lista.get(position); // ajusta índice por conta do header
        h.tvPesquisador.setText(p.getPesquisador());
        h.tvTipoPesquisa.setText(p.getTipoPesquisa());
        h.tvDataPesquisa.setText(p.getDataPesquisa());
        h.tvHoraPesquisa.setText(p.getHoraPesquisa());
        h.tvLinhaPesquisa.setText(p.getLinhaPesquisa().getNome());
        h.tvEstacaoPesquisa.setText(p.getEstacaoPesquisa().getNome());
        h.tvAreaPesquisa.setText(p.getAreaPesquisa());
        h.tvOrigem.setText(p.getOrigem());
        h.tvDestino.setText(p.getDestino());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizarLista(ArrayList<Pesquisa> pesquisas){
        this.lista = pesquisas;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void atualizarListaTeste(ArrayList<Pesquisa> pesquisas){
        this.lista = pesquisas;
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).getPesquisador().equals("AndersonTestando")){
                lista.remove(lista.get(i));
                i--;
            }
        }
        notifyDataSetChanged();
    }
}