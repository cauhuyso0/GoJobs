package vn.com.gojobs.controller;

import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import vn.com.gojobs.R;

import static android.content.Context.WINDOW_SERVICE;

public class CommonMethodFragment extends Fragment {


    private float DEVICE_HEIGHT = 0;
    private float DEVICE_WIDTH = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWidthHeightDevice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_common_method, container, false);
    }

    private void setWidthHeightDevice(){
        WindowManager wm = (WindowManager) this.getActivity()
                .getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);

        DEVICE_WIDTH = size.x;
        DEVICE_HEIGHT = size.y;
    }

    public float getDeviceHeight(){
        return DEVICE_HEIGHT;
    }

    public float getDeviceWidth(){
        return DEVICE_WIDTH;
    }
}