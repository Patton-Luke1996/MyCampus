package com.my_campus.mycampus_application.ui.discounts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscountViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiscountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Student Discounts");
    }

    public LiveData<String> getText() {
        return mText;
    }
}