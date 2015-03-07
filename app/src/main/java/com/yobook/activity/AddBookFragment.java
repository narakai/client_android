package com.yobook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yobook.R;
import com.yobook.eventbus.EventBus;
import com.yobook.eventbus.entry.ISBNCodeEvent;
import com.yobook.util.YLog;

/**
 *
 */
public class AddBookFragment  extends BaseFragment {
    public static final String TAG = AddBookFragment.class.getName();
    EditText mBookNameEd;
    TextView mBookISBNView;
    EditText mBookDesc;
    View mSubmitView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addbook,container,false);
        return root;
    }


    @Override
    public void onStart() {
        View rootView = getView();
        mBookNameEd = (EditText)rootView.findViewById(R.id.addBookFragment_book);
        mBookISBNView = (TextView)rootView.findViewById(R.id.addBookFragment_isbn);
        mSubmitView = rootView.findViewById(R.id.addBookFragment_submit);

        mBookISBNView.setOnClickListener(mClickListener);

        super.onStart();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.addBookFragment_isbn:
                    startActivity(new Intent(getActivity(), ScanISBNCodeActivity.class));
                    break;
                case R.id.addBookFragment_submit:
                    if(TextUtils.isEmpty(mBookISBNView.getText())
                            || TextUtils.isEmpty(mBookNameEd.getText())) {
                        Toast.makeText(getActivity(), R.string.toast_name_sn_both_null , Toast.LENGTH_LONG).show();
                        return ;
                    }
                    break;
            }

        }
    };

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(android.os.Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ISBNCodeEvent event) {
        YLog.i(TAG, "ISBNCode:" + event.getISBNCode() + "\tMainThread:" +
                (Looper.myLooper() == Looper.getMainLooper()));
        mBookISBNView.setText(event.getISBNCode());
    }
}
