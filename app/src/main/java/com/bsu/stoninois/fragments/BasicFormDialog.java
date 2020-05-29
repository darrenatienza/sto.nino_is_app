package com.bsu.stoninois.fragments;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bsu.stoninois.R;
import com.bsu.stoninois.activities.AccomplishmentListActivity_;
import com.fasterxml.jackson.core.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

@EFragment
public class BasicFormDialog extends DialogFragment {

    private EditNameDialogListener listener;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
        void onActionDelete();
    }
    Context context;

    String formTitle;
    int id;
    String title;

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @ViewById
    Button btnSave;

    @ViewById
    TextView tvFormTitle;

    @ViewById
    EditText etTitle;

    @Click
    void btnSave(){
        listener.onFinishEditDialog(etTitle.getText().toString());
        dismiss();
    }
    @Click
    void btnDelete(){

        listener.onActionDelete();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_form_dialog, container, false);
        return v;
    }
    @AfterViews
    void afterViews(){
        listener = (EditNameDialogListener) getActivity();
        tvFormTitle.setText(formTitle);
        etTitle.setText(title);


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
