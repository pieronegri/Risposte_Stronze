package pieronegri.rispostestronze.utils;

import android.util.Log;

import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
import pieronegri.rispostestronze.service.FBMessagingServiceIMPL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    private static final String TAG = Utility.class.getName();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static String userOnLine="%1$s is online";
    private static String noSignal="no signal available, please try later";
    private static String SetOnLinePresenceError="can not call the method if no user is signed";
    public Utility() {
    }

    public static void setCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            SetOnLinePresence();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static Boolean isUserSigned() {
        //if not sign there is nothing to signOut
        return getCurrentUser() != null;
    }

    public static void SetOnLinePresence() throws Exception {
        if (!isUserSigned()) {
            throw new Exception(SetOnLinePresenceError);
        }
        try {
            Log.w(TAG, String.format(userOnLine, currentUser.getUid()));//if null throws exception
            FirebaseDatabase database = FirebaseDatabase.getInstance();
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

    public static Boolean isUserSigned(FirebaseUser currentUser) {
        try {
            currentUser.getDisplayName(); //if not sign there is nothing to signOut
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void SetOnLinePresence(FirebaseUser currentUser, FirebaseDatabase database) throws Exception {
        if (!isUserSigned()) {
            throw new Exception("can not call the method if no user is signed");
        }
        try {
            String message = String.format(userOnLine, currentUser.getDisplayName()); //if null throws exception
            Log.w(TAG, message);
            FBMessagingServiceIMPL.getToken();
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Boolean> online = new HashMap<>();
            online.put("online", true);
            childUpdates.put("/" + FBNodeStructure.Presence + "/" + currentUser.getDisplayName(), online);
            database.getReference().updateChildren(childUpdates);

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
}

