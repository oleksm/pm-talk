package tech.oleks.pmtalk.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import tech.oleks.pmtalk.bean.FileUpload;
import tech.oleks.pmtalk.bean.Order;

import java.io.IOException;

/**
 * Created by alexm on 12/9/16.
 */

@Singleton
public class PmTalkService {

    @Inject DriveService driveService;
    @Inject CalendarService calendarService;
    @Inject SheetService sheetService;

    public void initiate(Order order) {
        FileUpload r = order.getResume();
        if (StringUtils.isBlank(order.getCandidate()) || StringUtils.isBlank(order.getPosition()) || r == null
                || StringUtils.isBlank(r.getFileName())) {
            order.setErrors("Please fill in Candidate, Position and Resume fields");
            return;
        }

        try {
            String folderId = driveService.createFolder(order.getCandidate());
            String codingLink = driveService.copyCodingDoc(folderId, order.getCandidate());
            String resumeLink = driveService.uploadResume(order.getResume(), folderId);
            String reportId = driveService.copyReport(folderId, order.getCandidate());
            String content = calendarService.getEventMessage(order.getCandidate(), codingLink, reportId, resumeLink);
            String meetingLink = calendarService.createEvent(order.getCandidate(), content);
            sheetService.fillInReport(reportId, resumeLink, meetingLink, order.getPosition(), codingLink);

            order.setCodingLink(codingLink);
            order.setMeetingLink(meetingLink);
            order.setFolderId(folderId);
            submitOrder(order);
        } catch (IOException e) {
            e.printStackTrace();
            order.setErrors("Looks like something went wrong. Sorry about that. Error: " + e.getMessage());
        }
    }

    public void submitOrder(Order o) {

    }
}
