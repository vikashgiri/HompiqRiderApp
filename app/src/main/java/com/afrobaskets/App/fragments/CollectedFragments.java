package com.afrobaskets.App.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afrobaskets.App.adapter.CollectionFragmentAdapter;
import com.afrobaskets.App.bean.CategoriesBean;
import com.afrobaskets.App.bean.OrderCollectionBean;
import com.afrobaskets.App.bean.OrderItemListBeans;
import com.afrobaskets.App.bean.ProductDetailBeans;
import com.afrobaskets.App.bean.ShippingAddressListBeans;
import com.afrobaskets.App.bean.StoreListBeans;
import com.afrobaskets.App.bean.UserDetail;
import com.afrobaskets.App.interfaces.Constant;
import com.afrobaskets.App.interfaces.SavePref;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webistrasoft.org.ecommerce.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hh on 13-Nov-17.
 */

public class CollectedFragments extends android.support.v4.app.Fragment {
     JSONObject sendJson;
ProgressDialog pDialog;
    private RecyclerView new_arrival_recyclerView,hot_deal_recyclerView,recyclerView;

    private List<CategoriesBean> foodList = new ArrayList<>();

    CollectionFragmentAdapter fAdapter;

    public CollectedFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.collectionfragments, container, false);

        recyclerView = (RecyclerView)view. findViewById(R.id.recycler_view);
        getData(getActivity());
        return  view;
    }
    void getData(final Context context)
    {  pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {

            sendJson = new JSONObject();
            sendJson.put("method", "assignedordertorider");
            sendJson.put("order_status","sample_collected");
            //sendJson.put("user_id", "1");
             sendJson.put("user_id", SavePref.getPref(getActivity(),SavePref.User_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        try {
                            ArrayList<OrderCollectionBean> orderCollectionBeanArrayList;

                            orderCollectionBeanArrayList =
                                    new ArrayList<>();
                            orderCollectionBeanArrayList.clear();
                            JSONObject jObject = new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            JSONObject order_detailsJObject = jObject.getJSONObject("orderList");
                            Iterator<String> orderlistkeys = order_detailsJObject.keys();
                            OrderCollectionBean orderCollectionBean;
                            while (orderlistkeys.hasNext()) {
                                orderCollectionBean = new OrderCollectionBean();
                                String orderlistkey = orderlistkeys.next();
                                JSONObject orderJObject = order_detailsJObject.getJSONObject(orderlistkey);
                                orderCollectionBean.setOrder_status(orderJObject.getString("order_status"));
                                //orderCollectionBean.setImageRootPath(new JSONObject(response).getString("imageRootPath"));
                                orderCollectionBean.setId(orderJObject.getString("id"));
                                orderCollectionBean.setPayable_amount(orderJObject.getString("payable_amount"));
                                orderCollectionBean.setRider_id(orderJObject.getString("rider_id"));
                                orderCollectionBean.setOrder_id(orderJObject.getString("order_id"));
                                orderCollectionBean.setCreated_date(orderJObject.getString("updated_date"));
                                //orderCollectionBean.setStore_id(orderJObject.getString("store_id"));
                                orderCollectionBean.setShipping_address_id(orderJObject.getString("shipping_address_id"));
                                orderCollectionBean.setStatus(orderJObject.getString("status"));
                                orderCollectionBean.setCreated_date(orderJObject.getString("created_date"));
                                orderCollectionBean.setUpdated_date(orderJObject.getString("updated_date"));
                               // orderItemListBeans.setNumber_of_item(jsonObject.getString("number_of_item"));
                                orderCollectionBean.setPayment_status(orderJObject.getString("payment_status"));
                                orderCollectionBean.setCommission_amount(orderJObject.getString("user_id"));
                                orderCollectionBean.setDiscount_amount(orderJObject.getString("commission_amount"));
                                orderCollectionBean.setAmount(orderJObject.getString("amount"));
                                orderCollectionBean.setStatus(orderJObject.getString("discount_amount"));

                                JSONObject shippingAddressListJObject = jObject.getJSONObject("shippingAddressList");
                                Iterator<String> shippingAddresskeys = shippingAddressListJObject.keys();
                                ArrayList<ShippingAddressListBeans>shippingAddressListBeansArrayList=new ArrayList<>();
                                JSONObject shippingorderJObject=null;
                                while (shippingAddresskeys.hasNext()) {
                                    ShippingAddressListBeans shippingAddressListBeans = new ShippingAddressListBeans();
                                    String shippingkey = shippingAddresskeys.next();
                                    shippingorderJObject = shippingAddressListJObject.getJSONObject(shippingkey);
                                    if(orderJObject.getString("shipping_address_id").equalsIgnoreCase(shippingorderJObject                                                     .getString("id"))) {

                                        shippingAddressListBeans.setId(shippingorderJObject.getString("id"));
                                        shippingAddressListBeans.setUser_id(shippingorderJObject.getString("user_id"));
                                       // shippingAddressListBeans.setAddress_nickname(shippingorderJObject.getString("address_nickname"));

                                        shippingAddressListBeans.setContact_name(shippingorderJObject.getString("contact_name"));
                                        shippingAddressListBeans.setCity_id(shippingorderJObject.getString("city_id"));
                                        shippingAddressListBeans.setCity_name(shippingorderJObject.getString("city_name"));
                                        shippingAddressListBeans.setHouse_number(shippingorderJObject.getString("house_number"));
                                        shippingAddressListBeans.setStreet_detail(shippingorderJObject.getString("street_detail"));
                                        shippingAddressListBeans.setAge(shippingorderJObject.getString("age"));
                                        shippingAddressListBeans.setZipcode(shippingorderJObject.getString("zipcode"));

                                        shippingAddressListBeans.setGender(shippingorderJObject.getString("gender"));
                                        shippingAddressListBeans.setCreated_date(shippingorderJObject.getString("created_date"));
                                        shippingAddressListBeans.setUpdated_date(shippingorderJObject.getString("updated_date"));
                                        shippingAddressListBeansArrayList.add(shippingAddressListBeans);
                                    }
                                    orderCollectionBean.setShippingAddressListBeansArrayList(shippingAddressListBeansArrayList);
                                }



                                JSONObject userListJObject = jObject.getJSONObject("userList");
                                Iterator<String> userListkeys = userListJObject.keys();
                                ArrayList<UserDetail>userDetailJObjectArrayList=new ArrayList<>();
                                while (userListkeys.hasNext()) {
                                    UserDetail UserDetailBeans = new UserDetail();
                                    String shippingkey = userListkeys.next();
                                    JSONObject userJObject = userListJObject.getJSONObject(shippingkey);
                                    if(shippingorderJObject.getString("user_id").equalsIgnoreCase(userJObject                                                     .getString("id"))) {

                                        UserDetailBeans.setId(userJObject.getString("id"));
                                        UserDetailBeans.setName(userJObject.getString("name"));
                                        UserDetailBeans.setMobile_number(userJObject.getString("mobile_number"));
                                        UserDetailBeans.setEmail(userJObject.getString("email"));
                                        UserDetailBeans.setKey(userJObject.getString("key"));
                                        UserDetailBeans.setCity_id(userJObject.getString("city_id"));
                                        UserDetailBeans.setPassword(userJObject.getString("password"));
                                        UserDetailBeans.setAddress(userJObject.getString("address"));
                                        UserDetailBeans.setCreated_date(userJObject.getString("created_date"));
                                        UserDetailBeans.setUpdated_date(userJObject.getString("updated_date"));
                                        UserDetailBeans.setStatus(userJObject.getString("status"));
                                        userDetailJObjectArrayList.add(UserDetailBeans);
                                    }
                                }
                                orderCollectionBean.setUserDetailArrayList(userDetailJObjectArrayList);



                                JSONObject orderItemListJObject = jObject.getJSONObject("orderItemList");
                                Iterator<String> orderItemListkeys = orderItemListJObject.keys();
                                while (orderItemListkeys.hasNext()) {
                                    String orderItemkey = orderItemListkeys.next();
                                    if(orderJObject.getString("order_id").equalsIgnoreCase(orderItemkey)) {
                                        ArrayList<OrderItemListBeans> orderItemListBeansArrayList = new ArrayList<>();

                                        JSONArray jsonArray = orderItemListJObject.getJSONArray(orderItemkey);
                                        ArrayList<ProductDetailBeans> productDetailBeansArrayList = new
                                                ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            OrderItemListBeans orderItemListBeans = new OrderItemListBeans();
                                            JSONObject product_dump_jObject = new JSONObject(jsonObject.getString("test_dump")).getJSONObject("product_details");
                                            ProductDetailBeans productDetailBeans = new
                                                    ProductDetailBeans();
                                            productDetailBeans.setId(product_dump_jObject.getString("id"));
                                            productDetailBeans.setLab_id(product_dump_jObject.getString("lab_id"));
                                            productDetailBeans.setDiscount_value(product_dump_jObject.getString("discount_value"));
                                            productDetailBeans.setDiscount_type(product_dump_jObject.getString("discount_type"));
                                            productDetailBeans.setPrice(product_dump_jObject.getString("price"));
                                            //productDetailBeans.setPercent(product_dump_jObject.getString("percent"));
                                            productDetailBeans.setTest_name(product_dump_jObject.getString("test_name"));
                                            productDetailBeans.setLab_address(product_dump_jObject.getString("lab_address"));
                                            productDetailBeansArrayList.add(productDetailBeans);
                                            orderItemListBeans.setProductDetailBeansArrayList(productDetailBeansArrayList);
                                            orderItemListBeansArrayList.add(orderItemListBeans);
                                        }
                                        orderCollectionBean.setOrderItemListBeansArrayList(orderItemListBeansArrayList);
                                    }
                                }

                                ArrayList<StoreListBeans>storeListBeansArrayList=new ArrayList<>();
                                JSONObject storeListJObject = jObject.getJSONObject("labList");
                                Iterator<String> storeListkeys = storeListJObject.keys();
                                while (storeListkeys.hasNext()) {
                                    String storeListkey = storeListkeys.next();
                                    if(orderJObject.getString("lab_id").equalsIgnoreCase(storeListkey)) {
                                        StoreListBeans storeListBeans = new StoreListBeans();
                                        JSONObject storeJObject = storeListJObject.getJSONObject(storeListkey);
                                        storeListBeans.setId(storeJObject.getString("id"));
                                        storeListBeans.setLab_name(storeJObject.getString("lab_name"));
                                        storeListBeans.setReg_number(storeJObject.getString("reg_number"));

                                        storeListBeans.setLab_address(storeJObject.getString("lab_address"));
                                        // storeListBeans.setin(storeJObject.getString("lng"));

                                        storeListBeans.setLab_google_address(storeJObject.getString("lab_google_address"));

                                        storeListBeans.setLat(storeJObject.getString("lat"));
                                        storeListBeans.setLng(storeJObject.getString("lng"));
                                        storeListBeansArrayList.add(storeListBeans);
                                    }
                                }
                                orderCollectionBean.setStoreListBeansArrayList(storeListBeansArrayList);
                                orderCollectionBeanArrayList.add(orderCollectionBean);
                            }
                            System.out.println("dsdsdsc"+orderCollectionBeanArrayList);
                            pDialog.dismiss();
                            set(orderCollectionBeanArrayList); // This will make a callback to activity.
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Communication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error!", Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    void set(ArrayList<OrderCollectionBean> cartListBeanArrayList) {
        fAdapter = new CollectionFragmentAdapter(getActivity(),"completed",cartListBeanArrayList,recyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(fAdapter);
    }
}
