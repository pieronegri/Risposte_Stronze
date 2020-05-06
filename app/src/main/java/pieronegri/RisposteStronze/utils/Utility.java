package pieronegri.RisposteStronze.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pieronegri.RisposteStronze.R;
import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    private static final String TAG = Utility.class.getName();
    private static String userOnLine="%1$s is online";
    private static String noSignal="no signal available, please try later";
    private static String SetOnLinePresenceError="can not call the method if no user is signed";
    public Utility() {
    }

    public static void setCurrentUser() {
        try {
            SetOnLinePresence();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public static void SetOnLinePresence() throws Exception {
        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
            throw new Exception("can not call the method if no user is signed");
        }
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            FBMessagingServiceUtil.setToken();
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Boolean> online = new HashMap<>();
            online.put("online", true);
            childUpdates.put("/" + FBNodeStructure.Presence + "/" + currentUser.getDisplayName(), online);
            FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

            Map<String, Object> disconnectChild = new HashMap<>();
            Map<String, Object> lastActiveOn = new HashMap<>();
            DatabaseReference messageRef = database.getReference();
            disconnectChild.put("/" + FBNodeStructure.Presence + "/" + currentUser.getDisplayName(), lastActiveOn);
            lastActiveOn.put(FBNodeStructure.LastActiveOn, Calendar.getInstance().getTime().toString());
            messageRef.onDisconnect().updateChildren(disconnectChild);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            Log.w("Utility", noSignal);
            return false;
        }
    }

    public static Boolean isUserSigned() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void _toast(Context ctx, String message) {
        Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(ctx.getResources().getColor(R.color.colorText));
        toast.show();
    }
    public static Bitmap loadBitmapFromView(View view) {
        //and create a bitmap of the same size
        int width = view.getWidth();
        int height = (int) (0.8*view.getHeight());

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        //Cause the view to re-layout
        view.measure(measuredWidth, measuredHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
        view.draw(c);

        return b;
    }
}