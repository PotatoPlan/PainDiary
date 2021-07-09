package com.example.paindiaryapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.paindiaryapp.databinding.FragmentMapsBinding;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    private FragmentMapsBinding binding;
    private MapView mapView;
    private MapboxGeocoding mapboxGeocoding;
    public static final String USER_MARKER = "user-marker";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String token = getString(R.string.mapbox_access_token);
        Mapbox.getInstance(getActivity(), token);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // --------------------Get user's searched coordinates data (by using SharedPreference)--------------------
        SharedPreferences sharedPre = requireActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
        float userLongitude = sharedPre.getFloat("User Longitude", (float) 34.45429497393134);
        float userLatitude = sharedPre.getFloat("User Latitude", (float) 83.12689744379429);
        LatLng userLatLng = new LatLng(userLatitude, userLongitude);


        // Default coordinates once entered the Map Fragment
        final LatLng latLng = new LatLng(-37.8142176, 144.9631608);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        CameraPosition position = new CameraPosition.Builder().target(latLng).zoom(9).build();
                        mapboxMap.setCameraPosition(position);

                        // Add the Marker style (call this method)
                        addUserMarkerToStyle(style);

                        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);

                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setTextAllowOverlap(true);

                        // Use user's searched coordinates to draw the marker
                        SymbolOptions symbolOptions = new SymbolOptions().withLatLng(userLatLng).withIconImage(USER_MARKER).withIconSize(1.5f);
                        symbolManager.create(symbolOptions);
                    }
                });
            }
        });


        binding.addressConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the relevant map view when user hit the confirm button
                displayLocation();
            }
        });


        return view;
    }

// --------------------Get user input longitude and latitude and locate them on the map--------------------
    private void displayLocation() {
        // Get user's input address
        String address = binding.address.getText().toString().trim();
        // Build the Mapbox Geocoder
        mapboxGeocoding = MapboxGeocoding.builder().accessToken(getString(R.string.mapbox_access_token)).query(address).build();

        // Make the API call to find the location
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();
                if (results.size() > 0) {
                    Point firstResultPoint = results.get(0).center();
                    // Get the longitude and latitude if the API call is success
                    double longitude = firstResultPoint.longitude();
                    double latitude = firstResultPoint.latitude();

                    // Put the user searched coordinates data to this SharedPreference again (for later use)
                    SharedPreferences sharedPre = requireActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sharedPre.edit();
                    spEditor.putFloat("User Longitude", (float) longitude);
                    spEditor.putFloat("User Latitude", (float) latitude);
                    spEditor.apply();

                    // Store the latitude and longitude in a LatLng variable
                    LatLng userAddressLatLng = new LatLng(latitude,longitude);

                    // Draw the map (zooming 13)
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull MapboxMap mapboxMap) {
                            mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    CameraPosition position = new CameraPosition.Builder().target(userAddressLatLng).zoom(13).build();
                                    mapboxMap.setCameraPosition(position);

                                    // Add the marker and put the marker to the correct location
                                    addUserMarkerToStyle(style);

                                    // Use SymbolManager and SymbolOptions
                                    SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);

                                    symbolManager.setIconAllowOverlap(true);
                                    symbolManager.setTextAllowOverlap(true);

                                    SymbolOptions symbolOptions = new SymbolOptions().withLatLng(userAddressLatLng).withIconImage(USER_MARKER).withIconSize(1.5f);
                                    symbolManager.create(symbolOptions);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                // Inform the user that the API call is failed
                Toast.makeText(getActivity(), "Failed to find your location", Toast.LENGTH_LONG).show();
            }
        });
    }
// --------------------Get user input longitude and latitude and locate them on the map--------------------

    private void addUserMarkerToStyle (Style style) {
        // Convert this drawable icon to the Bitmap, and then add it to the style with the name as the pre-set id: USER_MARKER
        style.addImage(USER_MARKER, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_baseline_location_marker)), true);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        // Cancel the call when the user is leaving the application before the call is completed
//        mapboxGeocoding.cancelCall();
    }
}
