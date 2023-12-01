package com.example.incivisme.ui.home;


import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.incivisme.R;
import com.example.incivisme.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ActivityResultLauncher<String[]> locationPermissionRequest;

    Location mLastLocation = new Location("LastLocation");
    FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient() {
        @NonNull
        @Override
        public Task<Void> flushLocations() {
            return null;
        }

        @NonNull
        @Override
        public Task<Location> getCurrentLocation(int i, @Nullable CancellationToken cancellationToken) {
            return null;
        }

        @NonNull
        @Override
        public Task<Location> getCurrentLocation(@NonNull CurrentLocationRequest currentLocationRequest, @Nullable CancellationToken cancellationToken) {
            return null;
        }

        @NonNull
        @Override
        public Task<Location> getLastLocation() {
            return null;
        }

        @NonNull
        @Override
        public Task<Location> getLastLocation(@NonNull LastLocationRequest lastLocationRequest) {
            return null;
        }

        @NonNull
        @Override
        public Task<LocationAvailability> getLocationAvailability() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> removeLocationUpdates(@NonNull PendingIntent pendingIntent) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> removeLocationUpdates(@NonNull LocationCallback locationCallback) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> removeLocationUpdates(@NonNull LocationListener locationListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull PendingIntent pendingIntent) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationCallback locationCallback, @Nullable Looper looper) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull LocationListener locationListener, @Nullable Looper looper) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationCallback locationCallback) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> requestLocationUpdates(@NonNull LocationRequest locationRequest, @NonNull Executor executor, @NonNull LocationListener locationListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> setMockLocation(@NonNull Location location) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> setMockMode(boolean b) {
            return null;
        }

        @NonNull
        @Override
        public ApiKey<Api.ApiOptions.NoOptions> getApiKey() {
            return null;
        }
    };
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button localizacion = view.findViewById(R.id.button_location);

        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        getLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        getLocation();
                    } else {
                        Toast.makeText(requireContext(), "No concedeixen permisos", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });



        final TextView textView = binding.buttonLocation;

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(requireContext(), "Request permisssions", Toast.LENGTH_SHORT).show();
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    location -> {
                        if (location != null) {
                            mLastLocation = location;
                            binding.localitzacio.setText(
                                    String.format("Latitud: %1$.4f \n Longitud: %2$.4f\n Hora: %3$tr",
                                            mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude(),
                                            mLastLocation.getTime()));
                        } else {
                            binding.localitzacio.setText("Sense localització coneguda");
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "getLocation: permissions granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}