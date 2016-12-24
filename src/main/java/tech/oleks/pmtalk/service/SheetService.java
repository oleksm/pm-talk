package tech.oleks.pmtalk.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexm on 12/23/16.
 */
@Singleton
public class SheetService extends ManagedService {

    @Inject GoogleApiService api;
    @Inject DriveService driveService;

    ValueRange valueRange;

    @Override
    public void start() throws IOException {
        System.out.println("SheetService start...");
        String fileId = driveService.getDriveMap().getReportId();

        valueRange = api.getSheets().spreadsheets().values()
                .get(fileId, config.getSheetRange())
                .execute();

        System.out.println("SheetService start DONE.");
    }

    public void fillInReport(String fileId, String resumeLink, String meetingLink, String positionLink,
                             String codingLink) throws IOException {

        List<List<Object>> values = new ArrayList<>();
        for (List<Object> row: valueRange.getValues()) {
            List<Object> r = new ArrayList<>();
            for (Object value: row) {
                String v = String.valueOf(value);
                if (StringUtils.equalsIgnoreCase(v, config.getReportRequestedT())) {
                    r.add(new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).toString("dd/MM/yyyy h:mm:ss a z"));
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportResumeT())) {
                    r.add(resumeLink);
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportMeetingT())) {
                    r.add(meetingLink);
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportStaffingDeskT())) {
                    r.add(positionLink);
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportCodingT())) {
                    r.add(codingLink);
                }
                else {
                    r.add(value);
                }
            }
            values.add(r);
        }

        ValueRange range = new ValueRange();
        range.setValues(values);
        range.setMajorDimension(valueRange.getMajorDimension());

        api.getSheets().spreadsheets().values()
                .update(fileId, config.getSheetRange(), range)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

}
