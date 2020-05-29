package com.bsu.stoninois.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bsu.stoninois.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_ip_address)
public class IpAddressDialog extends DialogFragment {
    private DialogActionListener listener;

    // 1. Defines the listener interface with a method passing back data result.
    public interface DialogActionListener {
        void onSave(String ipAddress,String port);
    }
    Context context;

    String ipAddress;
    String port;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @ViewById
    Button btnSave;

    @ViewById
    TextView tvFormTitle;

    @ViewById
    EditText etIpAddress;
    @ViewById
    EditText etPort;
    @Click
    void btnSave(){
        listener.onSave(etIpAddress.getText().toString(),etPort.getText().toString());
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.requestWindowFeature(STYLE_NO_TITLE);
        d.getContext().getTheme().applyStyle(R.style.dialog,true);
        return d;
    }

    /**@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_form_dialog, container, false);
        return v;
    }*/

    @AfterViews
    void afterViews(){
        listener = (DialogActionListener) getActivity();
        //tvFormTitle.setText(formTitle);
        etIpAddress.setText(ipAddress);
        etPort.setText(port);

    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
