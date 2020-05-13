package com.ilqjx.pojo;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ilqjx.service.OrderService;

@Entity
@Table(name = "order_")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String orderCode;
    private String address;
    private String post;
    private String receiver;
    private String userMessage;
    private String mobile;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmDate;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @Transient
    private float total;
    @Transient
    private int totalNumber;
    @Transient
    private String statusDesc;
    @Transient
    private List<OrderItem> orderItemList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getStatusDesc() {
        if (null != statusDesc) {
            return statusDesc;
        }
        String desc;
        switch (status) {
            case OrderService.waitPay:
                desc = "待付款";
                break;
            case OrderService.waitDelivery:
                desc = "待发货";
                break;
            case OrderService.waitConfirm:
                desc = "待收货";
                break;
            case OrderService.waitReview:
                desc = "待评论";
                break;
            case OrderService.finish:
                desc = "完成";
                break;
            case OrderService.delete:
                desc = "删除";
                break;
            default:
                desc = "未知";
        }
        statusDesc = desc;
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                ", address='" + address + '\'' +
                ", post='" + post + '\'' +
                ", receiver='" + receiver + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", payDate=" + payDate +
                ", deliveryDate=" + deliveryDate +
                ", confirmDate=" + confirmDate +
                ", user=" + user +
                ", total=" + total +
                ", totalNumber=" + totalNumber +
                ", statusDesc='" + statusDesc + '\'' +
                ", orderItemList=" + orderItemList +
                '}';
    }

}
