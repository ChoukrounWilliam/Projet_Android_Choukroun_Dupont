package com.example.william.pokedex;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PokemonActivity extends AppCompatActivity {

    private final String TAG = "PokemonActivity";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.william.pokedex.R.layout.activity_pokemon);

        recyclerView = findViewById(com.example.william.pokedex.R.id.rv_current);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        PokemonAdapter pa = new PokemonAdapter(getPokemonFromFile());

        recyclerView.setAdapter(pa);

        GetPokemonsServices.startActionGetPokmenon(this);
        IntentFilter intentFilter = new IntentFilter(POKEMON_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PokemonUpdate(), intentFilter);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
        mBuilder.setContentTitle("POKEDEX");
        mBuilder.setContentText("Il y a : " + getPokemonFromFile().length() + " pokemon dans ce pokedex");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.example.william.pokedex.R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case com.example.william.pokedex.R.id.main_page :
                AlertDialog.Builder builder = new AlertDialog.Builder(PokemonActivity.this);
                builder.setTitle("Retour en arrière");
                builder.setMessage("Etes-vous sûr de vouloir retourner au menu ?");
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PokemonActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "On reste ici", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String POKEMON_UPDATE = "com.example.william.pokedex.POKEMON_UPDATE";

    public class PokemonUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Intent : " + getIntent().getAction());
            JSONArray array = getPokemonFromFile();
            PokemonAdapter pa = (PokemonAdapter) recyclerView.getAdapter();
            pa.setNewPokemon(array);
            Log.d("count ", Integer.toString(array.length()));
        }
    }

    public JSONArray getPokemonFromFile(){
        try {

            InputStream is = new FileInputStream(getCacheDir() + "/" + "pokemon.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer,"UTF-8")).getJSONArray("objects");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }




}
