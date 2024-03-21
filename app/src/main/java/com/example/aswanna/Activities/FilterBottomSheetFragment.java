package com.example.aswanna.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.aswanna.Model.FilterData;
import com.example.aswanna.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private EditText editText1;
    private EditText editText2;
    private Button show;
    // Add this newInstance method to pass the search query
    public static FilterBottomSheetFragment newInstance(String searchQuery) {
        FilterBottomSheetFragment fragment = new FilterBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("searchQuery", searchQuery);
        fragment.setArguments(args);
        return fragment;
    }
    public FilterBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet, container, false);
        // Initialize your spinners and text views here and add their behavior.
        // For example, add a click listener to apply filters.
        spinner1 = view.findViewById(R.id.locationSpinner);
        spinner2 = view.findViewById(R.id.TypeSpinner);
        spinner3 = view.findViewById(R.id.farmerLevelSpinner);
        editText1 = view.findViewById(R.id.editTextText);
        editText2 = view.findViewById(R.id.editTextText3);
        show=view.findViewById(R.id.showbuttonfilter);
       // Button applyButton = view.findViewById(R.id.applyButton);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's selections
                String spinner1Value = spinner1.getSelectedItem().toString();
                String spinner2Value = spinner2.getSelectedItem().toString();
                String spinner3Value = spinner3.getSelectedItem().toString();
                String editText1Value = editText1.getText().toString();
                String editText2Value = editText2.getText().toString();

                // Create a FilterData object and set the selected values
                FilterData filterData = new FilterData();
                filterData.setSpinner1Value(spinner1Value);
                filterData.setSpinner2Value(spinner2Value);
                filterData.setSpinner3Value(spinner3Value);
                filterData.setEditText1Value(editText1Value);
                filterData.setEditText2Value(editText2Value);
                // Retrieve the search query from the arguments
                String searchQuery = getArguments().getString("searchQuery");

                // Pass the data to the next activity via Intent
                Intent intent = new Intent(getActivity(), SearchResultInvestor.class);
                intent.putExtra("filterData", filterData);
                intent.putExtra("searchQuery", searchQuery); // Pass the search query

                startActivity(intent);
            }
        });
        return view;
    }
}
