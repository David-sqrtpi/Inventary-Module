package com.sdandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import Entity.Product;
import Services.CloudFirestore;

public class MainActivity extends AppCompatActivity {

    EditText etCode, etNombre, etPrecio;
    Button btnEscanner, btnBorrar, btnUpdate;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //etCode = findViewById(R.id.codigo);
        //btnEscanner = findViewById(R.id.button);
        //etNombre = findViewById(R.id.etNombre);
        //etPrecio = findViewById(R.id.etPrecio);
        //btnBorrar = findViewById(R.id.BorrarBtn);
        //btnUpdate = findViewById(R.id.updateBtn);
    }

    public void registrar(View view) {

        final String code = etCode.getText().toString();
        final String name = etNombre.getText().toString();
        final String price = etPrecio.getText().toString();

        db.collection("product")
                .document(code)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Toast.makeText(MainActivity.this, "El registro ya existe", Toast.LENGTH_LONG).show();
                            } else {
                                if (!code.equals("") && !name.equals("") && !price.equals("")) {

                                    Product product = new Product();
                                    product.setCode(code);
                                    product.setName(name);
                                    product.setPrice(price);

                                    CloudFirestore cloudFirestore = new CloudFirestore();

                                    cloudFirestore.create(code, product);

                                    etPrecio.setText("");
                                    etCode.setText("");
                                    etNombre.setText("");

                                    Toast.makeText(MainActivity.this, "Se ha registrado el producto", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MainActivity.this, "Los campos son obligatorios", Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            Log.d("TAG", "Failed with: ", task.getException());
                        }
                    }
                });

    }

    public void consultar(View vista) {

        String code = etCode.getText().toString();

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
                                    etPrecio.setText(price);
                                    etNombre.setText(name);
                                } else {
                                    Toast.makeText(MainActivity.this, "El registro no existe", Toast.LENGTH_LONG).show();
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

    public void borrar(View view) {

        String code = etCode.getText().toString();

        if(code.equals("")){
            Toast.makeText(MainActivity.this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show();
        } else {
            CloudFirestore cloudFirestore = new CloudFirestore();

            cloudFirestore.delete(code);

            Toast.makeText(MainActivity.this, "Se ha borrado el registro", Toast.LENGTH_LONG).show();
        }

        etPrecio.setText("");
        etCode.setText("");
        etNombre.setText("");

    }

    public void editar(View view) {

        final String code = etCode.getText().toString();

        db.collection("product")
                .document(code)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){

                                CloudFirestore cloudFirestore = new CloudFirestore();

                                Product product = new Product();

                                if (!product.getName().equals("")) {
                                    product.setName(etNombre.getText().toString());
                                } else {
                                    product.setName(documentSnapshot.getString("name"));
                                }

                                if (!product.getPrice().equals("")) {
                                    product.setPrice(etPrecio.getText().toString());
                                } else {
                                    product.setName(documentSnapshot.getString("price"));
                                }

                                cloudFirestore.update(code, product);

                            } else {
                                Toast.makeText(MainActivity.this, "El registro no existe. No se puede actualizar", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    public void newActivity(View view) {
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }

    public void consult(View view) {
        Intent intent = new Intent(this, Consult.class);
        startActivity(intent);
    }

    public void cart(View view) {
         Intent intent = new Intent(this, Cart.class);
         startActivity(intent);
    }

}