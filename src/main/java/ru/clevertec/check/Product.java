package main.java.ru.clevertec.check;

import java.io.InvalidObjectException;
import java.math.BigDecimal;

public class Product {

    private final Integer id;
    private String description;
    private BigDecimal price;
    private Integer quantityInStock;
    private Boolean isWholesaleProduct;

    public Product(Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.price = builder.price;
        this.quantityInStock = builder.quantityInStock;
        this.isWholesaleProduct = builder.isWholesaleProduct;
    }

    public static class Builder {

        private Integer id;
        private String description;
        private BigDecimal price;
        private Integer quantityInStock;
        private Boolean isWholesaleProduct;

        public Builder() {}

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder quantityInStock(Integer quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }

        public Builder isWholesaleProduct(Boolean isWholesaleProduct) {
            this.isWholesaleProduct = isWholesaleProduct;
            return this;
        }

        public Product build() throws InvalidObjectException {
            if (id == null ||
                description == null ||
                price == null ||
                quantityInStock == null ||
                isWholesaleProduct == null) {
                throw new InvalidObjectException("Невозможно создать объект Product с неинициализированными полями!");
            }
            if (price.signum() == -1) throw new InvalidObjectException("Невозможно задать отрицательную цену товара!");
            if (quantityInStock < 0) throw new InvalidObjectException("Невозможно задать отрицательное количество товара!");
            return new Product(this);
        }

    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Boolean isWholesaleProduct() {
        return isWholesaleProduct;
    }

    public void isWholesaleProduct(Boolean isWholesaleProduct) {
        this.isWholesaleProduct = isWholesaleProduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", wholesaleProduct=" + isWholesaleProduct +
                '}';
    }
}
