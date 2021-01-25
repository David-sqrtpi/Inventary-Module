package Services;

import Entity.Product;

public interface Database {

    void get(String id, final ProductCallback productCallback) throws InterruptedException;

    void delete(String id);

    void update(Product product);

    void create(Product product);

}
