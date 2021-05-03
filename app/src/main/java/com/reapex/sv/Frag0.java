package com.reapex.sv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.reapex.sv.asrshort.Chat_ListView;
import com.reapex.sv.asrshort.Chat_Recycler;

public class Frag0 extends Fragment  implements View.OnClickListener {
    static final String TAG = Frag0.class.getSimpleName();

    RelativeLayout mRLCircle, mRLButton;
    TextView mTextViewWifi, mTextViewFontSize, mTextViewBrightness, mTextViewConnection;
    ImageView mImageViewWifi, mImageViewConnect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.a_frag0, container, false);
        Log.e(TAG, "启动.");

        mRLCircle = v.findViewById(R.id.relative_layout_circle);
        mRLButton = v.findViewById(R.id.relative_layout_button_connection);

        mTextViewConnection = v.findViewById(R.id.text_view_connection);
        mTextViewBrightness = v.findViewById(R.id.text_view_brightness);
        mTextViewFontSize = v.findViewById(R.id.text_view_font_size);
        mTextViewWifi = v.findViewById(R.id.text_view_wifi);

        mImageViewWifi    = v.findViewById(R.id.image_view_wifi);
        mImageViewConnect = v.findViewById(R.id.start_connect_iv);

        mRLCircle.setOnClickListener(this);
        mRLButton.setOnClickListener(this);

        showWifi();
        return v;
    }

    private void showWifi(){
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            mTextViewWifi.setText(getString(R.string.network_isnot_available));
            mTextViewConnection.setText(getString(R.string.connect_failuer_toast));
            mTextViewConnection.setTextColor(ContextCompat.getColor(getActivity(), R.color.btn_red));
            mRLCircle.setBackgroundResource(R.drawable.background_unconnected);
            mImageViewWifi.setImageResource(R.drawable.icons8noconnection);
            mImageViewConnect.setVisibility(View.GONE);
        }else{
            NetworkCapabilities info = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
            if (info == null) {
                mTextViewWifi.setText(getString(R.string.network_isnot_available));
                mTextViewConnection.setText(getString(R.string.connect_failuer_toast));
                mTextViewConnection.setTextColor(ContextCompat.getColor(getActivity(), R.color.btn_red));
                mRLCircle.setBackgroundResource(R.drawable.background_unconnected);
                mImageViewWifi.setImageResource(R.drawable.icons8noconnection);
                mImageViewConnect.setVisibility(View.GONE);
            }else if(info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                mTextViewWifi.setText(getString(R.string.wifi));
                mTextViewConnection.setText(getString(R.string.system_ready));
                mImageViewWifi.setImageResource(R.drawable.icons8wifi);
            } else if (info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                mTextViewWifi.setText(getString(R.string.data_internent));
                mTextViewConnection.setText(getString(R.string.system_ready));
                mImageViewWifi.setImageResource(R.drawable.icons8data);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link @Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        showWifi();
        Log.e(TAG, "fragment0 resumed.");
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link @Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "fragment0 onstart.");
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "fragment0 onPause.");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), Chat_ListView.class);
        intent.putExtra("from1", getString(R.string.code1));
        intent.putExtra("from2", getString(R.string.contact1));
        intent.putExtra("from3", R.mipmap.icons81);
        startActivity(intent);
    }
}