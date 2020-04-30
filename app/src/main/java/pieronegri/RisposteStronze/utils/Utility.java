package pieronegri.RisposteStronze.utils;

import android.util.Log;

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
        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            return false;
        }
        return true;
    }
}

