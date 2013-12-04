//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0-SNAPSHOT.
//


package com.opentaxi.android;

import java.io.Serializable;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.opentaxi.android.R.id;
import com.opentaxi.android.R.layout;
import com.opentaxi.generated.mysql.tables.pojos.Feedback;
import com.opentaxi.models.NewCRequest;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RequestDetailsActivity_
    extends RequestDetailsActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String NEW_C_REQUEST_EXTRA = "newCRequest";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
        requestWindowFeature(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.request_details);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static RequestDetailsActivity_.IntentBuilder_ intent(Context context) {
        return new RequestDetailsActivity_.IntentBuilder_(context);
    }

    public static RequestDetailsActivity_.IntentBuilder_ intent(Fragment supportFragment) {
        return new RequestDetailsActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        requestNumber = ((TextView) hasViews.findViewById(id.requestNumber));
        rejectButton = ((Button) hasViews.findViewById(id.rejectButton));
        remaining_time = ((TextView) hasViews.findViewById(id.remaining_time));
        price_group = ((TextView) hasViews.findViewById(id.price_group));
        editButton = ((Button) hasViews.findViewById(id.editButton));
        arrive_time = ((TextView) hasViews.findViewById(id.arrive_time));
        address = ((TextView) hasViews.findViewById(id.address));
        feedBackButton = ((Button) hasViews.findViewById(id.feedBackButton));
        state = ((TextView) hasViews.findViewById(id.state));
        chosen_group = ((TextView) hasViews.findViewById(id.chosen_group));
        datecreated = ((TextView) hasViews.findViewById(id.datecreated));
        if (hasViews.findViewById(id.editButton)!= null) {
            hasViews.findViewById(id.editButton).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RequestDetailsActivity_.this.editButton();
                }

            }
            );
        }
        if (hasViews.findViewById(id.rejectButton)!= null) {
            hasViews.findViewById(id.rejectButton).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RequestDetailsActivity_.this.rejectButton();
                }

            }
            );
        }
        if (hasViews.findViewById(id.feedBackButton)!= null) {
            hasViews.findViewById(id.feedBackButton).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RequestDetailsActivity_.this.feedBackButton();
                }

            }
            );
        }
        if (hasViews.findViewById(id.okButton)!= null) {
            hasViews.findViewById(id.okButton).setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    RequestDetailsActivity_.this.okButton();
                }

            }
            );
        }
        afterRequestsActivity();
    }

    @SuppressWarnings("unchecked")
    private<T >T cast_(Object object) {
        return ((T) object);
    }

    private void injectExtras_() {
        Intent intent_ = getIntent();
        Bundle extras_ = intent_.getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(NEW_C_REQUEST_EXTRA)) {
                try {
                    newCRequest = cast_(extras_.get(NEW_C_REQUEST_EXTRA));
                } catch (ClassCastException e) {
                    Log.e("RequestDetailsActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void showFeedBack(final Feedback[] feedbacks) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                RequestDetailsActivity_.super.showFeedBack(feedbacks);
            }

        }
        );
    }

    @Override
    public void setFeedBack() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    RequestDetailsActivity_.super.setFeedBack();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void rejectRequest(final String reason) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    RequestDetailsActivity_.super.rejectRequest(reason);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void sendFeedBack(final String comment, final Map<Integer, Float> vote) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    RequestDetailsActivity_.super.sendFeedBack(comment, vote);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, RequestDetailsActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RequestDetailsActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public RequestDetailsActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (context_ instanceof Activity) {
                    ((Activity) context_).startActivityForResult(intent_, requestCode);
                } else {
                    context_.startActivity(intent_);
                }
            }
        }

        public RequestDetailsActivity_.IntentBuilder_ newCRequest(NewCRequest newCRequest) {
            intent_.putExtra(NEW_C_REQUEST_EXTRA, ((Serializable) newCRequest));
            return this;
        }

    }

}