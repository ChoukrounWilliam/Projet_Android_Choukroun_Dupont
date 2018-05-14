package com.example.william.pokedex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonHolder> {

    JSONArray pokemonArray;

    public PokemonAdapter(JSONArray array){
        this.pokemonArray = array;
    }

    @Override
    public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.william.pokedex.R.layout.rv_pokemon_element,parent,false);
        PokemonHolder holder = new PokemonHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PokemonHolder holder, int position) {
        try {
            String name = pokemonArray.getJSONObject(position).getString("name");
            String attack = pokemonArray.getJSONObject(position).getString("attack").toString();
            String defense = pokemonArray.getJSONObject(position).getString("defense").toString();
            String hp = pokemonArray.getJSONObject(position).getString("hp").toString();
            String weight = pokemonArray.getJSONObject(position).getString("weight");
            String id = pokemonArray.getJSONObject(position).getString("national_id").toString();
            holder.changeText(name,attack,defense,hp,weight,id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pokemonArray.length();
    }

    public void setNewPokemon(JSONArray array){
        this.pokemonArray = array;
        notifyDataSetChanged();
    }

    public class PokemonHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_attack, tv_defense, tv_hp, tv_weight;
        ImageView image;

        public PokemonHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_name);
            tv_attack = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_attack);
            tv_defense = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_defence);
            tv_hp = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_hp);
            tv_weight = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_weight);
            image = itemView.findViewById(com.example.william.pokedex.R.id.rv_element_image);
        }

        private void changeText(String name, String attack, String defense, String hp, String weight, String id){
            tv_name.setText(name);
            tv_weight.setText("Poids : " + weight);
            tv_hp.setText("PV : " + hp);
            tv_defense.setText("Def : " + defense + "pt");
            tv_attack.setText("Att : " + attack + "pt");
            Glide.with(itemView).load("https://pokeapi.co/media/img/"+id+".png").into(image);
        }
    }

}
