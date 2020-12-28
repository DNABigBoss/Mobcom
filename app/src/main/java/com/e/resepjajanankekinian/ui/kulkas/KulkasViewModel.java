package com.e.resepjajanankekinian.ui.kulkas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KulkasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public KulkasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}