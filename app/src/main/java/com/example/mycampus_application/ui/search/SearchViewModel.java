package com.example.mycampus_application.ui.search;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mycampus_application.R;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Search");



    }

    public LiveData<String> getText() {
        return mText;
    }



}