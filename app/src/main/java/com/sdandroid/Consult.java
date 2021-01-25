package com.sdandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import Entity.Product;
import Services.BooleanCallback;
import Services.CloudFirestore;
import Services.ProductCallback;

public class Consult extends AppCompatActivity {

    private EditText editText;
    private TextView textViewId, textViewName, textViewPrice;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        editText = findViewById(R.id.editText);
        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        textViewPrice = findViewById(R.id.textViewPrice);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)&&(i == KeyEvent.KEYCODE_ENTER)) {
                    get(editText.getText().toString());
                }
                return false;
            }
        });

    }

    public void scan(View view){

        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        intent.setPrompt("Apunte al código de barras");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.setOrientationLocked(false);
        intent.initiateScan();

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() == null) {

                Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();

            } else {

                String code = result.getContents();

                get(code);

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    private void get(final String code) {

        if (!code.equals("")) {

            final CloudFirestore cloudFirestore = new CloudFirestore();

            cloudFirestore.exists(code, new BooleanCallback() {
                @Override
                public void responseCallback(boolean result) {
                    if(!result){
                        Toast.makeText(Consult.this, "El registro no existe", Toast.LENGTH_LONG).show();
                        textViewId.setText("Registro con codigo " + code + " no encontrado");
                        textViewPrice.setText("");
                        textViewName.setText("");
                    } else {
                        cloudFirestore.get(code, new ProductCallback() {
                            @Override
                            public void responseCallback(Product product) {
                                textViewId.setText("id: " + product.getCode());
                                textViewPrice.setText("Precio: " + product.getPrice());
                                textViewName.setText("Nombre: " + product.getName());
                            }
                        });
                    }
                }

            });

        } else {
            Toast.makeText(this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show();
        }

    }

}