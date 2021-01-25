package Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import Entity.Product;

public class CloudFirestore implements Database {

    FirebaseIntegration firebaseIntegration = new FirebaseIntegration();
    private boolean exists = true;
    private Product product = new Product();

    @Override
    public void get(String id, final ProductCallback productCallback) {

        firebaseIntegration.db.collection("product")
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
                            product.setCode(code);
                            product.setName(name);
                            product.setPrice(price);

                            productCallback.responseCallback(product);

                        }
                    }
                });

    }

    @Override
    public void delete(String id) {

        firebaseIntegration.db.collection("product").document(id).delete();

    }

    @Override
    public void
    update(Product product) {

        product.setCode(product.getCode());

        firebaseIntegration.db.collection("product").document(product.getCode())
                .set(product, SetOptions.merge());
    }

    @Override
    public void create(Product product) {

        firebaseIntegration.db.collection("product").document(product.getCode()).set(product);

    }

    public void exists(String id, final BooleanCallback booleanCallback) {

        firebaseIntegration.db.collection("product")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            exists = document.exists();
                            booleanCallback.responseCallback(exists);
                        } else {
                            Log.d("TAG", "Failed with: ", task.getException());
                        }
                    }
                });

    }

}
