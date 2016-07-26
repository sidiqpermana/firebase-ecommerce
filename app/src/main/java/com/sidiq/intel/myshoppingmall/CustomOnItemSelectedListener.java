package com.sidiq.intel.myshoppingmall;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by inte on 7/22/2016.
 */
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private OnItemSelectedCallback onItemSelectedCallback;
    private int position;
    private TextView tvSubtotal;

    public CustomOnItemSelectedListener(int position, TextView tvSubtotal,
                                        OnItemSelectedCallback onItemSelectedCallback){
        this.position = position;
        this.tvSubtotal = tvSubtotal;
        this.onItemSelectedCallback = onItemSelectedCallback;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        onItemSelectedCallback.onItemSelected(view, tvSubtotal, position, i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnItemSelectedCallback{
        void onItemSelected(View view, TextView textView, int itemRowPosition, int itemArrayPosition);
    }
}
