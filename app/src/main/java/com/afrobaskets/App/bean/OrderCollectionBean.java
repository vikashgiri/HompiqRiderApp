package com.afrobaskets.App.bean;

import java.util.ArrayList;

/**
 * Created by HP-PC on 12/25/2017.
 */

public class OrderCollectionBean {
    String id;
    String rider_id;
    String order_id;
    String status;
    String created_date;
    String store_id;
    String shipping_address_id;
    String order_status;
    String imageRootPath;
   String updated_date;
    String payment_status;
    String user_id;
    String commission_amount;
    String   amount;
    String discount_amount;

    ArrayList<ShippingAddressListBeans>shippingAddressListBeansArrayList;
    ArrayList<OrderItemListBeans>orderItemListBeansArrayList;
    ArrayList<StoreListBeans>storeListBeansArrayList;
    ArrayList<UserDetail>userDetailArrayList;
    String payable_amount;


    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(String commission_amount) {
        this.commission_amount = commission_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }


    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public ArrayList<UserDetail> getUserDetailArrayList() {
        return userDetailArrayList;
    }

    public void setUserDetailArrayList(ArrayList<UserDetail> userDetailArrayList) {
        this.userDetailArrayList = userDetailArrayList;
    }

    public String getImageRootPath() {
        return imageRootPath;
    }

    public void setImageRootPath(String imageRootPath) {
        this.imageRootPath = imageRootPath;
    }

    public ArrayList<ShippingAddressListBeans> getShippingAddressListBeansArrayList() {
        return shippingAddressListBeansArrayList;
    }

    public void setShippingAddressListBeansArrayList(ArrayList<ShippingAddressListBeans> shippingAddressListBeansArrayList) {
        this.shippingAddressListBeansArrayList = shippingAddressListBeansArrayList;
    }

    public ArrayList<OrderItemListBeans> getOrderItemListBeansArrayList() {
        return orderItemListBeansArrayList;
    }

    public void setOrderItemListBeansArrayList(ArrayList<OrderItemListBeans> orderItemListBeansArrayList) {
        this.orderItemListBeansArrayList = orderItemListBeansArrayList;
    }

    public ArrayList<StoreListBeans> getStoreListBeansArrayList() {
        return storeListBeansArrayList;
    }

    public void setStoreListBeansArrayList(ArrayList<StoreListBeans> storeListBeansArrayList) {
        this.storeListBeansArrayList = storeListBeansArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShipping_address_id(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
