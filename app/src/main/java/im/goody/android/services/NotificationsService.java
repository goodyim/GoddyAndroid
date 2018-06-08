package im.goody.android.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.root.RootActivity;

import static im.goody.android.Constants.NotificationExtra.AUTHOR_NAME;
import static im.goody.android.Constants.NotificationExtra.FOLLOWEE_NEW_EVENT;
import static im.goody.android.Constants.NotificationExtra.ID;
import static im.goody.android.Constants.NotificationExtra.MESSAGE;
import static im.goody.android.Constants.NotificationExtra.TAGS;
import static im.goody.android.Constants.NotificationExtra.TITLE;
import static im.goody.android.Constants.NotificationExtra.TYPE;
import static im.goody.android.Constants.NotificationExtra.TYPE_CLOSE_EVENT;
import static im.goody.android.Constants.NotificationExtra.TYPE_COMMENT;
import static im.goody.android.Constants.NotificationExtra.TYPE_MENTION;
import static im.goody.android.Constants.NotificationExtra.TYPE_NEW_EVENT;
import static im.goody.android.Constants.NotificationExtra.TYPE_NEW_PARTICIPATOR;


public class NotificationsService extends FirebaseMessagingService {


    @Inject
    PreferencesManager preferencesManager;

    private static final String GOODY_CHANNEL = "GOODY_CHANNEL";
    private static final int GOODY_SESSION = 1337;

    @Override
    public void onCreate() {
        super.onCreate();

        App.getDataComponent().inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0 && data.get(TYPE) != null) {
            switch (data.get(TYPE)) {
                case TYPE_COMMENT:
                    processComment(data);
                    break;
                case TYPE_MENTION:
                    processMention(data);
                    break;
                case TYPE_NEW_EVENT:
                    processNewEvent(data);
                    break;
                case TYPE_CLOSE_EVENT:
                    processCloseEvent(data);
                    break;
                case TYPE_NEW_PARTICIPATOR:
                    processNewParticipator(data);
                    break;
                case FOLLOWEE_NEW_EVENT:
                    processFolloweeNewEvent(data);
                    break;
            }
        }
    }

    private void processFolloweeNewEvent(Map<String, String> data) {
        if (isNotNull(data, ID, AUTHOR_NAME, TITLE)) {
            Long id = Long.valueOf(data.get(ID));
            String author = data.get(AUTHOR_NAME);
            String title = data.get(TITLE);

            String content = getString(R.string.follow_new_event_content, author);

            sendNotification(title, content, id);
        }
    }

    private void processNewParticipator(Map<String, String> data) {
        if (isNotNull(data, ID, TITLE, MESSAGE)) {
            Long id = Long.valueOf(data.get(ID));
            String title = data.get(TITLE);
            String participatorName = data.get(MESSAGE);

            String content = getString(R.string.new_participator_content, participatorName);

            sendNotification(title, content, id);
        }
    }

    private void processCloseEvent(Map<String, String> data) {
        if (isNotNull(data, ID, MESSAGE, AUTHOR_NAME)) {
            Long id = Long.valueOf(data.get(ID));
            String helperNames = data.get(MESSAGE);
            String author = data.get(AUTHOR_NAME);

            String title = getString(R.string.title_event_finished);
            String content = getString(R.string.event_finished_message, author, helperNames);

            sendNotification(title, content, id);
        }
    }

    private void processNewEvent(Map<String, String> data) {
        if (isNotNull(data, ID, TAGS, MESSAGE)) {
            Long id = Long.valueOf(data.get(ID));
            String tags = data.get(TAGS);
            String title = data.get(MESSAGE);
            String content = getString(R.string.new_event_notification_content, tags);
            sendNotification(title, content, id);
        }
    }

    private void processComment(Map<String, String> data) {
        if (isNotNull(data, TITLE, AUTHOR_NAME, MESSAGE, ID) && preferencesManager.isCommentNotificationsEnabled()) {
            String notificationTitle = data.get(TITLE);
            String notificationContent = getString(
                    R.string.comment_notification_format,
                    data.get(AUTHOR_NAME),
                    data.get(MESSAGE));
            Long id = Long.valueOf(data.get(ID));

            sendNotification(notificationTitle, notificationContent, id);
        }
    }

    private void processMention(Map<String, String> data) {
        if (isNotNull(data, TITLE, AUTHOR_NAME, ID) && preferencesManager.isMentionNotificationsEnabled()) {
            String notificationTitle = data.get(TITLE);
            String notificationContent = getString(
                    R.string.mention_notification_format,
                    data.get(AUTHOR_NAME));
            Long id = Long.valueOf(data.get(ID));

            sendNotification(notificationTitle, notificationContent, id);
        }
    }

    private void sendNotification(String notificationTitle, String notificationContent, Long id) {
        Notification notification = new NotificationCompat.Builder(this, GOODY_CHANNEL)
                .setContentIntent(getDetailIntent(id))
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContent))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(GOODY_SESSION, notification);
        }
    }

    private PendingIntent getDetailIntent(Long id) {
        Intent intent = new Intent(this, RootActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(RootActivity.EXTRA_POST_ID, id);

        return PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean isNotNull(Map<String, String> data, String... keys) {
        boolean result = true;

        for (String key : keys) {
            if (data.get(key) == null)
                result = false;
        }

        return result;
    }
}
