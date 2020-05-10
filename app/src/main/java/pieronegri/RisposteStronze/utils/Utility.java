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
import pieronegri.RisposteStronze.data_source.Firebase.FBRepository;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepositoryCallBack;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Utility {
    private static final String TAG = Utility.class.getName();
    private static String noSignal="no signal available, please try later";

    public Utility() {
    }

    public static void setCurrentUser() {
        try {
            SetOnLinePresence();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Boolean isUserSigned() {
        return DBPresence.getCurrentUser() != null;
    }
    public static FirebaseUser getCurrentUser() {
        return DBPresence.getCurrentUser();
    }

    public static void SetOnLinePresence() {
        try {
            DBPresence.SaveOnLineEvent();
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void signOut() {
        try{
            DBPresence.goOffLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onlinePlus(){
        new FBRepository(FBNodeStructure.Presence, true, true)
            .addListener(new FBRepositoryCallBack() {
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Integer currentOnLine=dataSnapshot.child(FBNodeStructure.OnLine).getValue(Integer.class);
                    Integer currentOffLine=dataSnapshot.child(FBNodeStructure.LastActiveOn).getValue(Integer.class);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(FBNodeStructure.OnLine, currentOnLine+1);
                    childUpdates.put(FBNodeStructure.LastActiveOn, currentOffLine-1);
                    FirebaseDatabase.getInstance().
                            getReference(FBNodeStructure.Presence).
                            updateChildren(childUpdates);
                }
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });}


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
    static class DBPresence{
        static FirebaseUser getCurrentUser(){
            return FirebaseAuth.getInstance().getCurrentUser();
        }

        static void SaveOnLineEvent() throws Exception {
            if (FirebaseAuth.getInstance().getCurrentUser()==null) {
                throw new Exception("can not call the method if no user is signed");
            }
            String path=String.format("/%1$s/%2$s",FBNodeStructure.Presence,FirebaseAuth.getInstance().getCurrentUser().getUid());
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
            Map<String,Object> m=new HashMap<>();
            m.put(path,newOffLineChild());
            FirebaseDatabase.getInstance().goOnline();
            databaseReference.onDisconnect().updateChildren(m);
            SaveOnFirebase(path,newOnLineChild());
        }

        static void goOffLine() {
            FirebaseDatabase.getInstance().goOffline();
        }

        private static Map<String, Boolean> newOnLineChild(){
            return newMap(FBNodeStructure.OnLine,true);
        }

        private static Map<String, Object> newOffLineChild(){
            return newMap(FBNodeStructure.LastActiveOn,Calendar.getInstance().getTime().toString());
        }

        private static <T> void  SaveOnFirebase(String path,Map<String, T> child){
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .updateChildren(newMap(path,child));
        }
        private static <T> Map<String, T> newMap(String _path, T obj){
            Map<String,T> m=new HashMap<>();
            m.put(_path,obj);
            return m;
        }
    }
}