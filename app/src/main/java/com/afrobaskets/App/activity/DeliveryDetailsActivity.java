package com.afrobaskets.App.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afrobaskets.App.adapter.CollectionDetailAdapter;
import com.afrobaskets.App.bean.CartBean;
import com.afrobaskets.App.bean.OrderCollectionBean;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webistrasoft.org.ecommerce.R;
import com.webistrasoft.org.ecommerce.databinding.DeliverydetailsactivityBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP-PC on 11/24/2017.
 */

public class DeliveryDetailsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {


    //Any random number you can take
    public static final int REQUEST_PERMISSION_LOCATION = 10;

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */




    private static String TAG = "MAP LOCATION";
    private LatLng mCenterLatLong;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    /**
     * The formatted location address.
     */
    JSONObject sendJson;

    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    String current_city;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION};

    Button mLocationText, btn_location;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    String subLocality;



    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    int updateLocation=0;
    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    protected int RQS_GooglePlayServices = 0;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        } else {
            googleAPI.getErrorDialog(this, resultCode, RQS_GooglePlayServices);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            //  Toast.makeText(FusedLocationWithSettingsDialog.this, "location was already on so detecting location now", Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }

        checkLocationSettings();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    //step 2
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your

        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    //step 3
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    //step 4

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            goAndDetectLocation();
        }

    }

    public void goAndDetectLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                (LocationListener) this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
                //     setButtonsEnabledState();
            }
        });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
                //   setButtonsEnabledState();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goAndDetectLocation();
                }
                break;
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null)
        {
            return;
        }
        mCurrentLocation = location;
        if(updateLocation<=0)
        {

            updateLocation++;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     * Invoked when settings dialog is opened and action taken
     * @param locationSettingsResult
     *	This below OnResult will be used by settings dialog actions.
     */

    //step 5
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");

                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //

                    //move to step 6 in onActivityResult to check what action user has taken on settings dialog
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }


    /**
     *	This OnActivityResult will listen when
     *	case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: is called on the above OnResult
     */
    //step 6:

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                // Check that the result was from the autocomplete widget.
                if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
                    if (resultCode == RESULT_OK) {
                        // Get the user's selected place from the Intent.
                        Place place = PlaceAutocomplete.getPlace(DeliveryDetailsActivity.this, data);
                        if (place == null) {
                            return;
                        }
                        // TODO call location based filter
                        LatLng latLong;
                        latLong = place.getLatLng();
                        //mLocationText.setText(place.getName() + "");

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLong).zoom(15f).tilt(70).build();

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                    }


                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(DeliveryDetailsActivity.this, data);
                } else if (resultCode == RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }

                break;
        }
    }

    //step 1
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    private List<CartBean> foodList = new ArrayList<>();
    JSONObject jsonObject;
    ProgressDialog pDialog;
    List<OrderCollectionBean> orderCollectionBeen;
    DeliverydetailsactivityBinding deliverydetailsactivityBinding;
    int position;


    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+orderCollectionBeen.get(position).getUserDetailArrayList().get(0).getMobile_number().toString())));
        }
    }
    int payment_collected=1;
void showPaymentDialog()
{

    final CharSequence[] items = {" Yes "," No "};

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Do you collect the payment")
             .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                        payment_collected=which;

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //  Your code when user clicked on Cancel
                    dialog.dismiss();
                }
            })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if(payment_collected==0) {
                      deliverd(DeliveryDetailsActivity.this, "sample_collected");
                        return;
                    }
                    Toast.makeText(getApplicationContext(),"Please collect payment",Toast.LENGTH_SHORT).show();


                }
            });
    builder.create().show();

            /*.setSingleChoiceItems(items, null, new DialogInterface.ons {
                @Override
                public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                    if (isChecked) {
                        if(indexSelected==0)
                        {
payment_collected=indexSelected;
                        }
                    }
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if(payment_collected==0) {
                        deliverd(DeliveryDetailsActivity.this, "sample_collected");
                    return;
                    }
                    Toast.makeText(getApplicationContext(),"Please collect payment",Toast.LENGTH_SHORT).show();

                    //  Your code when user clicked on OK
                    //  You can write the code  to save the selected item here
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //  Your code when user clicked on Cancel
                }
            }).create();*/




}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deliverydetailsactivityBinding = DataBindingUtil.setContentView(this, R.layout.deliverydetailsactivity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ImageView back = findViewById(R.id.toolbar_back);
        Gson gson = new Gson();
        Type type = new TypeToken<List<OrderCollectionBean>>() {
        }.getType();


        deliverydetailsactivityBinding.trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* LatLng latLng= getLocationFromAddress(DeliveryDetailsActivity.this,orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getHouse_number() + "," + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getStreet_detail() + "," + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getCity_name());*/
                LatLng latLng;
                if(getIntent().getStringExtra("type").equalsIgnoreCase("completed"))
                {
                     latLng= getLocationFromAddress(DeliveryDetailsActivity.this,orderCollectionBeen.get(position).getStoreListBeansArrayList().get(0).getLab_address());
                }
                else
                    {
                     latLng= getLocationFromAddress(DeliveryDetailsActivity.this,orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getStreet_detail() + "," + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getCity_name());
                }

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,

                       Uri.parse("http://maps.google.com/maps?saddr="+mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude()+"&daddr="+latLng.latitude+","+latLng.longitude));
                startActivity(intent);
            }
        });
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.

        //step 1
        buildGoogleApiClient();

        //step 2
        createLocationRequest();

        //step 3
        buildLocationSettingsRequest();
/*
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLocationSettings();
            }
        });*/
        orderCollectionBeen = gson.fromJson(getIntent().getStringExtra("data"), type);
        position = Integer.parseInt(getIntent().getStringExtra("position"));

        deliverydetailsactivityBinding.patientDetails.setText(orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getContact_name() + "\n" + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getHouse_number() + "," + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getStreet_detail() + "," + orderCollectionBeen.get(position).getShippingAddressListBeansArrayList().get(0).getCity_name());
TextView title=(TextView)findViewById(R.id.title);
deliverydetailsactivityBinding.trackOrder.setVisibility(View.VISIBLE);
        deliverydetailsactivityBinding.labDetails.setText(orderCollectionBeen.get(position).getStoreListBeansArrayList().get(0).getLab_name() + "\n" + orderCollectionBeen.get(position).getStoreListBeansArrayList().get(0).getLab_address());

title.setText("Order Details");
        deliverydetailsactivityBinding.date.setText(orderCollectionBeen.get(position).getCreated_date());

deliverydetailsactivityBinding.orderId.setText("order id:" + orderCollectionBeen.get(position).getOrder_id());
        deliverydetailsactivityBinding.phoneNumber.setText(orderCollectionBeen.get(position).getUserDetailArrayList().get(0).getMobile_number());
        deliverydetailsactivityBinding.outForDelivery.setText(getIntent().getStringExtra("type"));
       /* if(out_for_delivery.getText().toString().equalsIgnoreCase("collected"))
        {
            getUploadStatus(DeliveryDetailsActivity.this,"fresh");
        }*/
        deliverydetailsactivityBinding.phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCall();
            }
        });
        deliverydetailsactivityBinding.amount.setText("₹ " + orderCollectionBeen.get(position).getPayable_amount());
      //  deliverydetailsactivityBinding.mail.setText(orderCollectionBeen.get(position).getUserDetailArrayList().get(0).getEmail());
        //  deliverydetailsactivityBinding.amount.setText(orderCollectionBeen.get(position).get);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout rel_bottoms = findViewById(R.id.rel_bottoms);
        try {
            if (getIntent().getStringExtra("type").equalsIgnoreCase("c_history"))
            {
                deliverydetailsactivityBinding.trackOrder.setVisibility(View.GONE);
                rel_bottoms.setVisibility(View.GONE);
            }

            if (getIntent().getStringExtra("type").equalsIgnoreCase("out for delivery"))
            {
                deliverydetailsactivityBinding.trackOrder.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CollectionDetailAdapter fAdapter = new CollectionDetailAdapter(DeliveryDetailsActivity.this, orderCollectionBeen.get(position).getOrderItemListBeansArrayList(), orderCollectionBeen.get(position).getImageRootPath());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);
        if(deliverydetailsactivityBinding.outForDelivery.getText().toString().equalsIgnoreCase("collected"))
        {
            getUploadStatus(DeliveryDetailsActivity.this,"fresh");
        }



        deliverydetailsactivityBinding.outForDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(deliverydetailsactivityBinding.outForDelivery.getText().toString().equalsIgnoreCase("preUpload"))
                {
                        Intent intent=new Intent(DeliveryDetailsActivity.this, UploadPhotoActivity.class);
                        intent.putExtra("image_type","fresh");
                        intent.putExtra("order_id",orderCollectionBeen.get(position).getOrder_id());
                        startActivity(intent);
                        return;
                    }
                if(deliverydetailsactivityBinding.outForDelivery.getText().toString().equalsIgnoreCase("postUpload"))
                {       Intent intent=new Intent(DeliveryDetailsActivity.this, UploadPhotoActivity.class);
                        intent.putExtra("image_type","used");
                        intent.putExtra("order_id",orderCollectionBeen.get(position).getOrder_id());

                        startActivity(intent);
                        return;
                    }


                if(deliverydetailsactivityBinding.outForDelivery.getText().toString().equalsIgnoreCase("collected"))
                {
                    if(orderCollectionBeen.get(position).getPayment_status().equalsIgnoreCase("unpaid"))
                    {
                   showPaymentDialog();

                       return;
                    }
                    deliverd(DeliveryDetailsActivity.this,"sample_collected");
                    return;
                }
                if(deliverydetailsactivityBinding.outForDelivery.getText().toString().equalsIgnoreCase("completed"))
                {
                     deliverd(DeliveryDetailsActivity.this,"sample_submited_to_lab");
                    return;
                }
                else {
                    deliverd(DeliveryDetailsActivity.this,"out_for_collection");

                }
            }

        });
        if(orderCollectionBeen.get(position).getPayment_status().equalsIgnoreCase("unpaid")) {
            String sourceString = "Cash";
            deliverydetailsactivityBinding.paymentMode.setText(Html.fromHtml(sourceString));
            // deliverydetailsactivityBinding.paymentMode.setText();
        }
        else
        {
            String sourceString = "Razorpay";
            deliverydetailsactivityBinding.paymentMode.setText(Html.fromHtml(sourceString));


        }
    }

    void deliverd(final Context context, final String status_type)
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "updateorderbyrider");
            sendJson.put("rider_id", SavePref.getPref(DeliveryDetailsActivity.this,SavePref.User_id));
            sendJson.put("order_id", orderCollectionBeen.get(position).getOrder_id());
            sendJson.put("order_status", status_type);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("dsdfd"+response);
                        try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {


                                startActivity(new Intent(DeliveryDetailsActivity.this, HomeActivity.class));
                                Toast.makeText(getApplicationContext(),"Status  Updated",Toast.LENGTH_SHORT).show();
                                if(status_type.equalsIgnoreCase("out_for_collection"))
                                {
                                    Intent intents = new Intent(DeliveryDetailsActivity.this, LocationMonitoringService.class);
                                    startService(intents);

                                }
                                finish();
                                HomeActivity.activity.finish();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
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






    void getUploadStatus(final Context context, final String status_type)
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try
        {
           /* http://localhost/lab/application/customer?parameters={"method":"getsubscription","user_id":"3","page":"1","order_id":"sdf","test_id":"1","type":"fresh"}
            http://localhost/lab/application/customer?parameters={"method":"getsubscription","user_id":"3","page":"1","order_id":"sdf","test_id":"1","type":"used"}*/

            sendJson = new JSONObject();
            sendJson.put("method", "getsubscription");
            sendJson.put("user_id", SavePref.getPref(DeliveryDetailsActivity.this,SavePref.User_id));
            sendJson.put("order_id", orderCollectionBeen.get(position).getOrder_id());
            sendJson.put("page", "1");
            sendJson.put("test_id", "");
            sendJson.put("type", status_type);


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL+"application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("dsdfd"+response);
                        try {
                            pDialog.dismiss();
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                if (status_type.equalsIgnoreCase("fresh")) {
                                    getUploadStatus(context, "used");
                                }
                                if (status_type.equalsIgnoreCase("used")) {

                                    deliverydetailsactivityBinding.outForDelivery.setText("Collected");
                                    deliverydetailsactivityBinding.trackOrder.setVisibility(View.GONE);

                                }
                            }
                            else {
                                if (status_type.equalsIgnoreCase("fresh")) {

                                    deliverydetailsactivityBinding.outForDelivery.setText("PreUpload");
                                }
                                if (status_type.equalsIgnoreCase("used")) {
                                    deliverydetailsactivityBinding.trackOrder.setVisibility(View.GONE);
                                    deliverydetailsactivityBinding.outForDelivery.setText("PostUpload");
                                }
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                        pDialog.dismiss();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}