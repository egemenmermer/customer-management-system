package entity;

public class Cart {
    private int id;
    private int productId;
    private Product product;

    public Cart() {

    }

    public Cart(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", productId=" + productId +
                ", product=" + product +
                '}';
    }
}
