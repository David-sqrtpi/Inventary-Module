package com.sdandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

            db.collection("product")
                    .document(code)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    String name = document.getString("name");
                                    String price = document.getString("price");
                                    textViewId.setText("id: " + code);
                                    textViewPrice.setText("Precio: " + price);
                                    textViewName.setText("Nombre: " + name);
                                } else {
                                    Toast.makeText(Consult.this, "El registro no existe", Toast.LENGTH_LONG).show();
                                    textViewId.setText("Registro con codigo " + code + " no encontrado");
                                    textViewPrice.setText("");
                                    textViewName.setText("");
                                }
                            } else {
                                Log.d("TAG", "Failed with: ", task.getException());
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show();
        }

    }

}