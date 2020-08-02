package com.sdandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    EditText etCode;
    Button btnEscanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCode = findViewById(R.id.codigo);
        btnEscanner = findViewById(R.id.button);

        btnEscanner.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Escanear();

            }

        });

    }

    public void Escanear(){

        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        intent.setPrompt("Escanear Código");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){

            if(result.getContents() == null){

                Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();

            }

            else{

                etCode.setText(result.getContents().toString());

            }

        }

        else{

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

}