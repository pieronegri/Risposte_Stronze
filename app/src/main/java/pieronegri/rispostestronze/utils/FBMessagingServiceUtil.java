package pieronegri.rispostestronze.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;

import pieronegri.rispostestronze.R;
import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
import pieronegri.rispostestronze.data_source.Firebase.FBRepository;
import pieronegri.rispostestronze.ui.model.BottomNavigation;
import pieronegri.rispostestronze.utils.Utility;

public class FBMessagingServiceUtil  {
    public static void setToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        try {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            sendTokenToAppServer(task.getResult().getToken());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }});
    }
    public static void sendTokenToAppServer(String token){
        FBRepository rep = new FBRepository(FBNodeStructure.FCMClientToken);
        String id;
        try{
            rep.getFirebaseDatabaseReference().child(Utility.getCurrentUser().getDisplayName())
                    .setValue(token);
        }
        catch (Exception e){
            try{//if it's a system user
                e.printStackTrace();
                rep.getFirebaseDatabaseReference().child(Utility.getCurrentUser().getUid())
                        .setValue(token);
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
        }

    }

}
