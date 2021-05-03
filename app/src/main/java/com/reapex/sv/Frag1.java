package com.reapex.sv;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.reapex.sv.asrshort.Chat_Recycler;

public class Frag1 extends Fragment implements View.OnClickListener {
    final String TAG = this.getClass().getSimpleName();

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.a_frag1, container, false);

        RelativeLayout rGeneral = view.findViewById(R.id.relative_general);
        RelativeLayout rInHouse = view.findViewById(R.id.relative_in_house);
        RelativeLayout rOutside = view.findViewById(R.id.relative_outside);
        RelativeLayout rNoise   = view.findViewById(R.id.relative_noise);
        RelativeLayout rASRLong = view.findViewById(R.id.relative_asrlong);
        RelativeLayout rSoundect= view.findViewById(R.id.relative_sound_detect);

        rASRLong.setOnClickListener(this);
        rSoundect.setOnClickListener(this);
        rOutside.setOnClickListener(this);
        rNoise.setOnClickListener(this);
        rInHouse.setOnClickListener(this);
        rGeneral.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MySP.getInstance().init(getActivity());
        Log.e(TAG, "启动.");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.relative_general) {
            Intent intent = new Intent(getActivity(), Chat_Recycler.class);
            intent.putExtra("from1", getString(R.string.code1));
            intent.putExtra("from2", getString(R.string.contact1));
            intent.putExtra("from3", R.mipmap.icons81);
            startActivity(intent);
        } else if (view.getId() == R.id.relative_in_house) {
            Intent intent = new Intent(getActivity(), Chat_Recycler.class);
            intent.putExtra("from1", getString(R.string.code2));
            intent.putExtra("from2", getString(R.string.contact2));
            intent.putExtra("from3", R.mipmap.icons82);
            startActivity(intent);
        } else if (view.getId() == R.id.relative_outside) {
            Intent intent = new Intent(getActivity(), Chat_Recycler.class);
            intent.putExtra("from1", getString(R.string.code3));
            intent.putExtra("from2", getString(R.string.contact3));
            intent.putExtra("from3", R.mipmap.icons83);
            startActivity(intent);
        } else if (view.getId() == R.id.relative_noise) {
            Intent intent = new Intent(getActivity(), Chat_Recycler.class);
            intent.putExtra("from1", getString(R.string.code4));
            intent.putExtra("from2", getString(R.string.contact4));
            intent.putExtra("from3", R.mipmap.icons84);
            startActivity(intent);
        } else if (view.getId() == R.id.relative_sound_detect) {
            EditText editText = new EditText(getContext());
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(getString(R.string.input_code));
            dialog.setView(editText);
            dialog.setCancelable(false);
            dialog.setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar sb = Snackbar.make(view, editText.getText().toString()+getString(R.string.code_wrong), Snackbar.LENGTH_SHORT);
                    sb.getView().setBackgroundColor(Color.RED);
                    sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sb.setAnchorView(view.findViewById(R.id.relative_sound_detect));
                    sb.show();
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            dialog.show();
        } else if (view.getId() == R.id.relative_asrlong) {
            EditText editText = new EditText(getContext());
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(getString(R.string.input_code));
            dialog.setView(editText);
            dialog.setCancelable(false);
            dialog.setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar sb = Snackbar.make(view, editText.getText().toString()+getString(R.string.code_wrong), Snackbar.LENGTH_SHORT);
                    sb.getView().setBackgroundColor(Color.RED);
                    sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sb.setAnchorView(view.findViewById(R.id.relative_asrlong));
                    sb.show();
                }
            });
            dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            dialog.show();        }
    }
}