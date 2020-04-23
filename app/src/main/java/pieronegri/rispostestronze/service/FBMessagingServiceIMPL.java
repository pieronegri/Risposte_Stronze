        package pieronegri.rispostestronze.service;
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
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

        import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
        import pieronegri.rispostestronze.data_source.Firebase.FBRepository;
        import pieronegri.rispostestronze.R;
        import pieronegri.rispostestronze.ui.model.BottomNavigation;
        import pieronegri.rispostestronze.utils.Utility;

public class FBMessagingServiceIMPL extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        sendTokenToAppServer(token);
    }

    public static void getToken(){
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
            rep.getFirebaseDatabaseReference().child(Utility.getCurrentUser().getUid())
                    .setValue(token);
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage.getNotification());
    }


    private void sendNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, BottomNavigation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId=notification.getChannelId();
        if(notification.getChannelId()==null) {
            channelId = getString(R.string.default_notification_channel_id);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.nobackground_palla120x120)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 , notificationBuilder.build());

    }

}

