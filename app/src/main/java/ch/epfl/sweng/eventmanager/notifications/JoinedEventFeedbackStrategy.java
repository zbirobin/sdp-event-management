package ch.epfl.sweng.eventmanager.notifications;

import android.app.Notification;
import android.content.Context;
import ch.epfl.sweng.eventmanager.repository.data.Event;

public class JoinedEventFeedbackStrategy extends NotificationStrategy<Event> {
    private static final long ONE_DAY = 86_400_000;
    private static final String titletext = "Rate this event : ";

    public JoinedEventFeedbackStrategy(Context context) {
        super(context);
    }

    @Override
    void scheduleNotification(Event event) {
        // get Notification based on scheduled item
        Notification notification = getNotificationFromEvent(event);

        SchedulerHelper.scheduleNotification(context, event.getId(), notification, SchedulerHelper.getTimeTo(event.getEndDate()) + ONE_DAY);
    }

    @Override
    void unscheduleNotification(Event event) {
        SchedulerHelper.unscheduleNotification(context, event.getId());
    }

    private Notification getNotificationFromEvent(Event event){
        //TODO make the notification on click action send the user to the feedback fragment
         return NotificationBuilder.getNotificationFromItem(context, titletext + event.getName(), "");
    }
}
