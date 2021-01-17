package com.sdandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import Services.CloudFirestore;

public class MainActivity extends AppCompatActivity {

    EditText etCode, etNombre, etPrecio;
    Button btnEscanner, btnBorrar, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCode = findViewById(R.id.codigo);
        btnEscanner = findViewById(R.id.button);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        btnBorrar = findViewById(R.id.BorrarBtn);
        btnUpdate = findViewById(R.id.updateBtn);
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

                etCode.setText(result.getContents());

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    public void registrar(View view) {

        String code = etCode.getText().toString();
        String name = etNombre.getText().toString();
        String price = etPrecio.getText().toString();


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

    public void consultar(View vista) {

        String code = etCode.getText().toString();

        if (!code.equals("")) {

            CloudFirestore cloudFirestore = new CloudFirestore();

            if(cloudFirestore.exists(code)){

                Product product = cloudFirestore.get(code);

                etPrecio.setText(product.getPrice());
                etNombre.setText(product.getName());

            } else {

                Toast.makeText(MainActivity.this, "El registro no existe", Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show();
        }
    }

    /*public void borrar(View view) {

        String codigo = etCode.getText().toString();

        final String TAG = "";

        if (!codigo.equals("")) {
            db.collection("productos").whereEqualTo("código", codigo).
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            Toast.makeText(MainActivity.this, "Registro borrado", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                            Toast.makeText(MainActivity.this, "No se puede borrar el registro", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "El campo código no puede estar vacío", Toast.LENGTH_LONG).show();
        }

        etPrecio.setText("");
        etCode.setText("");
        etNombre.setText("");

    }

    public void editar(View view) {

        String codigo = etCode.getText().toString();

        db.collection("productos")
                .whereEqualTo("codigo", codigo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                if (document.exists()) {
                                    String codigo = etCode.getText().toString();
                                    String nombre = etNombre.getText().toString();
                                    String precio = etPrecio.getText().toString();

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("descripción", nombre);
                                    data.put("precio", precio);

                                    db.collection("productos").document(codigo)
                                            .set(data, SetOptions.merge());

                                    etPrecio.setText("");
                                    etCode.setText("");
                                    etNombre.setText("");

                                    Toast.makeText(MainActivity.this, "Se ha editado", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MainActivity.this, "El registro no existe", Toast.LENGTH_LONG).show();
                                }

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error inesperado", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }*/

}