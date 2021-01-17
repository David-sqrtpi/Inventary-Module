package Services;

import com.sdandroid.Product;

public interface Database {

    Product get(String id) throws InterruptedException;

    void delete();

    void update();

    void create(String id, Product payload);

}
