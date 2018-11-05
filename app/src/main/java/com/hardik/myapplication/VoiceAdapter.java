package com.hardik.myapplication;

/**
 * Created by hardik on 05/11/18.
 */
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import java.util.HashMap;
import java.util.Locale;


import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class VoiceAdapter extends AccessibilityService {
    @NotNull
    private final String TAG = "WhatsappAccessExample";
    @NotNull
    public TextToSpeech t1;

    @NotNull
    public final String getTAG() {
        return this.TAG;
    }

    @NotNull
    public final TextToSpeech getT1() {
        TextToSpeech var10000 = this.t1;
        if (this.t1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("t1");
        }

        return var10000;
    }

    public final void setT1(@NotNull TextToSpeech var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.t1 = var1;
    }

    protected void onServiceConnected() {
        Log.d(this.TAG, "AccessibilityService Connected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = 64;
        info.feedbackType = -1;
        info.notificationTimeout = 100L;
        this.setServiceInfo(info);
    }

    // public void onInterrupt() {
  //  String var1 = "not implemented";
    //    throw(Throwable)(new

//    NotImplementedError("An operation is not implemented: "+var1));
//}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onAccessibilityEvent(@Nullable AccessibilityEvent event) {
        Log.d(this.TAG, "FML");
        if(event != null && event.getEventType() == 64 && event.getPackageName().equals("com.whatsapp")) {
            Log.d(this.TAG, "Recieved event");
            Parcelable data = event.getParcelableData();
            if(data instanceof Notification) {
                Log.d(this.TAG, "Recieved notification");
                Log.d(this.TAG, (new StringBuilder()).append("ticker: ").append(((Notification)data).tickerText).toString());
                Log.d(this.TAG, "icon: " + ((Notification)data).iconLevel);
                Log.d(this.TAG, "notification: " + event.getText());
                Log.d(this.TAG, (new StringBuilder()).append("pkg name: ").append(event.getPackageName()).toString());
                Log.d(this.TAG, (new StringBuilder()).append("before text: ").append(event.getBeforeText()).toString());
                final ObjectRef latestMessage = new ObjectRef();
                latestMessage.element = "";
                CharSequence[] lines = ((Notification)data).extras.getCharSequenceArray("android.textLines");
                if(lines != null) {
                    CharSequence var10001 = lines[((Object[])lines).length - 1];
                    if(lines[((Object[])lines).length - 1] == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                    }

                    latestMessage.element = (String)var10001;
                } else {
                    String var5 = ((Notification)data).extras.getString("android.text");
                    if(var5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                    }

                    latestMessage.element = var5;
                }

                Log.d(this.TAG, "MESSAGE: " + (String)latestMessage.element);
                this.t1 = new TextToSpeech(this.getApplicationContext(), (OnInitListener)(new OnInitListener() {
                    public final void onInit(int status) {
                        if(status != -1) {
                            VoiceAdapter.this.getT1().setLanguage(Locale.UK);
                            VoiceAdapter.this.speakOut((String)latestMessage.element);
                        }

                    }
                }));
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

    private final void speakOut(String message) {
        TextToSpeech var10000;
        if(VERSION.SDK_INT >= 21) {
            var10000 = this.t1;
            if(this.t1 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("t1");
            }

            var10000.speak((CharSequence)message, 0, (Bundle)null, "");
        } else {
            var10000 = this.t1;
            if(this.t1 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("t1");
            }

            var10000.speak(message, 0, (HashMap)null);
        }

    }
}
