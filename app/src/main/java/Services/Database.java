package Services;

import com.sdandroid.Product;

public interface Database {

    Product get(String id) throws InterruptedException;

    void delete(String id);

    void update(String id, Product product);

    void create(String id, Product payload);

}
