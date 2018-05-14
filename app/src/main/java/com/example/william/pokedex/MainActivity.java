package com.example.william.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.william.pokedex.R.layout.activity_main);

        Button btn = findViewById(com.example.william.pokedex.R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Vous allez affichez les 60 premiers pokemon",Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, PokemonActivity.class);
                startActivity(i);
            }
        });

    }
}
