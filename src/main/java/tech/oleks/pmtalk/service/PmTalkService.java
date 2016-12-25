package tech.oleks.pmtalk.service;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import tech.oleks.pmtalk.bean.FileUpload;
import tech.oleks.pmtalk.bean.Order;
import tech.oleks.pmtalk.util.Ut;

import java.io.IOException;

/**
 * Created by alexm on 12/9/16.
 */

@Singleton
public class PmTalkService extends  ManagedService {

    @Inject DriveService driveService;
    @Inject CalendarService calendarService;
    @Inject SheetService sheetService;
    @Inject GmailService gmailService;

    /**
     *
     * @param order
     */
    public void minimal(Order order) {
        FileUpload r = order.getResume();
        if (StringUtils.isBlank(order.getCandidate()) || StringUtils.isBlank(order.getStaffingLink()) || r == null
                || StringUtils.isBlank(r.getFileName())) {
            order.setErrors("Please fill in Candidate, Position and Resume fields");
            return;
        }

        try {
            driveService.minimal(order);

            String codingLink = Ut.getLink(config.getShareLinkTemplate(), order.getCodingId());
            String resumeLink = Ut.getLink(config.getShareResumeLinkT(), order.getResumeId());

            String content = calendarService.getEventMessage(order.getCandidate(), codingLink, resumeLink);
            String meetingLink = calendarService.createEvent(order.getCandidate(), content);

            order.setCodingLink(codingLink);
            order.setMeetingLink(meetingLink);

            enqueue(order);
        } catch (IOException e) {
            e.printStackTrace();
            order.setErrors("Looks like something went wrong. Sorry about that. Error: " + e.getMessage());
        }
    }

    /**
     *
     * @param o
     * @throws IOException
     */
    public void complete(Order o) throws IOException {

        String reportId = driveService.complete(o);

        sheetService.fillInReport(reportId, o);
//        gmailService.sendMessage(o);
    }

    /**
     *
     * @param o
     */
    public void enqueue(Order o) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/worker")
                .param("candidate", o.getCandidate())
                .param("resumeId", o.getResumeId())
                .param("meetingLink", o.getMeetingLink())
                .param("staffingLink", o.getStaffingLink())
                .param("codingId", o.getCodingId())
        );
    }
}
