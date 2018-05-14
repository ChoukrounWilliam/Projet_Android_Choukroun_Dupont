package com.example.william.pokedex;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class GetPokemonsServices extends IntentService {

    private static final String get_pokemon = "com.example.william.pokedex.action.GetPokemonsServices";
    private static final String TAG = "GetPokemonsServices";

    public GetPokemonsServices() {
        super("GetPokemonsServices");
    }

    public static void startActionGetPokmenon(Context context) {
        Intent intent = new Intent(context, GetPokemonsServices.class);
        intent.setAction(get_pokemon);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (get_pokemon.equals(action)) {
                handleActionGetPokemon();
            }
        }
    }


    private void handleActionGetPokemon() {
        Log.i(TAG, "Thread service name : " + Thread.currentThread().getName());
        URL url = null;

        try {
            url = new URL("https://pokeapi.co/api/v1/pokemon/?limit=60");
            Log.i(TAG, "URL : " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "pokemon.json"));
                Log.d(TAG, "Current Weather JSON downloaded !");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(PokemonActivity.POKEMON_UPDATE));
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
