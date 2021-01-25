package com.sdandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import Entity.Product;

public class Cart extends AppCompatActivity {

    private EditText editText;
    private ListView listView;
    private List<Product> products = new ArrayList<>();
    private ArrayAdapter<Product> arrayAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);

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

                addToList(code);

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    private void addToList(final String code) {

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

                                    Product product = new Product();

                                    product.setCode(document.getString("code"));
                                    product.setName(document.getString("name"));
                                    product.setPrice(document.getString("price"));

                                    products.add(product);

                                    arrayAdapter = new ArrayAdapter<>
                                            (Cart.this, android.R.layout.simple_list_item_1, products);

                                    listView.setAdapter(arrayAdapter);

                                } else {
                                    Toast.makeText(Cart.this, "Registro con codigo " + code + " no encontrado", Toast.LENGTH_LONG).show();
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