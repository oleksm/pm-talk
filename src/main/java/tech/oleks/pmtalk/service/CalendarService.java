package tech.oleks.pmtalk.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.time.DateTime;
import tech.oleks.pmtalk.util.Ut;

import java.io.IOException;

@Singleton
public class CalendarService extends ManagedService {

    Calendar calendar;

    @Inject
    GoogleApiService api;

    /**
     *
     * @param candidate
     * @return
     * @throws IOException
     */
    public String createEvent(String candidate, String content) throws IOException {
        Event event = new Event()
                .setSummary(config.getEventNameTemplate().replaceAll("%NAME%", candidate))
                .setDescription(content);

        DateTime start = new DateTime().plusDays(5);

        EventDateTime eventStart = new EventDateTime()
                .setDateTime(Ut.toDateTime(start));
        event.setStart(eventStart);

        EventDateTime eventEnd = new EventDateTime()
                .setDateTime(Ut.toDateTime(start.plusHours(1)));
        event.setEnd(eventEnd);

        String calendarId = "primary";

        Event result = api.getCalendar().events()
                .insert(calendarId, event)
                .execute();

        return Ut.notNull(result.getHangoutLink(), "Hangout Link - " + result.getSummary());
    }

    /**
     *
     * @param codingLink
     * @param resumeLink
     * @return
     */
    public String getEventMessage(String candidate, String codingLink, String resumeLink) {
        return String.format("Candidate: %s\n" +
                "Coding Doc: %s\n" +
                "Resume: %s",
                candidate, codingLink, resumeLink
        );
    }
}