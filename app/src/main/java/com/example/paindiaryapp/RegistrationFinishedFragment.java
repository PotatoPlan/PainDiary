package com.example.paindiaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.paindiaryapp.databinding.FragmentRegistrationFinishedBinding;

public class RegistrationFinishedFragment extends Fragment {
    private FragmentRegistrationFinishedBinding binding;

    public RegistrationFinishedFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// --------------------Make this Fragment's layout as a global variable called "layoutView", and it will load the fragment's xml.--------------------
        binding = FragmentRegistrationFinishedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

// --------------------Set an onClickListener, once hitting that button, it will bring us back to the login page--------------------
        binding.finishedLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Authentication.class));
            }
        });

// --------------------This layoutView must be returned in the end--------------------
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
