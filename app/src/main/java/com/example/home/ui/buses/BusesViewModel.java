package com.example.home.ui.buses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BusesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is buses fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}