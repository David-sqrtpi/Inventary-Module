package Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdandroid.Product;

import java.util.HashMap;
import java.util.Map;

public class CloudFirestore implements Database {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean exists = true;
    private Product product = new Product();

    @Override
    public Product get(String id) throws InterruptedException {


        db.collection("product")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            String code = documentSnapshot.getString("code");
                            String name = documentSnapshot.getString("name");
                            String price = documentSnapshot.getString("price");
                            System.out.println("********************************************");
                            System.out.println("Nombre: " + name + ", Precio: " + price + ".");
                            System.out.println("********************************************");
                            product.setCode(code);
                            product.setName(name);
                            product.setPrice(price);
                            product.setName("Hola");
                        }
                    }
                }).wait();

        return product;

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void create(String id, Product product) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", product.getName());
        payload.put("code", product.getCode());
        payload.put("price", product.getPrice());

        db.collection("product").document(product.getCode()).set(product);

    }

    public boolean exists(String id) {

        db.collection("product")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            exists = document.exists();
                        } else {
                            Log.d("TAG", "Failed with: ", task.getException());
                        }
                    }
                });

        return exists;

    }
}
