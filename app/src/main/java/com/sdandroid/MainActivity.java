package com.sdandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    EditText etCode, etNombre, etPrecio;
    Button btnEscanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCode = findViewById(R.id.codigo);
        btnEscanner = findViewById(R.id.button);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);



    }

    public void Escanear(View view){

        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        intent.setPrompt("Apunte al código de barras");
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

                etCode.setText(result.getContents());

            }

        }

        else{

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    public void consulta(View vista) {

        AdminSQLite admin = new AdminSQLite(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String barCode = etCode.getText().toString();

        //name = etNombre.getText().toString();
        //price = etNombre.getText().toString();

        Cursor fila = db.rawQuery("SELECT nombre, precio FROM producto WHERE code="+barCode, null);

        if (fila.moveToFirst()) {

            etNombre.setText(fila.getString(0));
            etPrecio.setText(fila.getString(1));

        }
        else {

            Toast.makeText(this, "El producto no esta registrado", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Escriba nombre y precio en los campos para registrar", Toast.LENGTH_LONG).show();

        }

    }

    public void registrar(View view){

        AdminSQLite admin = new AdminSQLite(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String code = etCode.getText().toString();
        String nombre = etNombre.getText().toString();
        String precio = etPrecio.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("code", code);
        registro.put("nombre", nombre);
        registro.put("precio", precio);

        db.insert("producto", null, registro);

        db.close();

        etPrecio.setText("");
        etCode.setText("");
        etNombre.setText("");

        Toast.makeText(this, "Se ha añadido el producto", Toast.LENGTH_LONG).show();

    }

    public void borrar() {

        AdminSQLite admin = new AdminSQLite(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigo = etCode.getText().toString();

        int cant = db.delete("usuarios", "code=" + codigo, null);

        db.close();

        etPrecio.setText("");
        etCode.setText("");
        etNombre.setText("");

        if(cant == 1){

            Toast.makeText(this, "Se ha eliminado el registro", Toast.LENGTH_LONG).show();

        }
        else{

            Toast.makeText(this, "El registro no existe", Toast.LENGTH_LONG).show();

        }

    }

    public void editar(){

        AdminSQLite admin = new AdminSQLite(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigo = etCode.getText().toString();
        String nombre = etNombre.getText().toString();
        String precio = etPrecio.getText().toString();

        ContentValues registro = new ContentValues();

        registro.put("code", codigo);
        registro.put("nombre", nombre);
        registro.put("precio", precio);

        int cant = db.update("producto", registro, "code=" + codigo, null);

        db.close();

        etPrecio.setText("");
        etCode.setText("");
        etNombre.setText("");

        if(cant == 1){

            Toast.makeText(this, "Se ha modificado el producto", Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(this, "El registro no existe", Toast.LENGTH_LONG).show();

        }



    }

}