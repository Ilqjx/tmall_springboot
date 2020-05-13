package com.ilqjx.pojo;

import javax.persistence.*;

@Entity
@Table(name = "orderitem")
public class OrderItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int number;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "oid")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", number=" + number +
                ", product=" + product +
                ", order=" + order +
                ", user=" + user +
                '}';
    }

}
