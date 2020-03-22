package ani.am.e_commerce.interfaces;

import ani.am.e_commerce.db.entity.Product;

public interface CustomOnClickListener {
    void onClickListener(int position);

    void editProduct(Product product);

    void editCategory(int position);

    void removeCategory(int position);
}
