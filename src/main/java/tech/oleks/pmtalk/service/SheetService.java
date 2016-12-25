package tech.oleks.pmtalk.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import tech.oleks.pmtalk.bean.Order;
import tech.oleks.pmtalk.util.Ut;

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
        log.info("SheetService start...");
        String fileId = driveService.getDriveMap().getReportId();

        valueRange = api.getSheets().spreadsheets().values()
                .get(fileId, config.getSheetRange())
                .execute();

        log.info("SheetService start DONE.");
    }

    /**
     *
     * @param fileId
     * @param o
     * @return
     * @throws IOException
     */
    public String fillInReport(String fileId, Order o) throws IOException {

        List<List<Object>> values = new ArrayList<>();
        for (List<Object> row: valueRange.getValues()) {
            List<Object> r = new ArrayList<>();
            for (Object value: row) {
                String v = String.valueOf(value);
                if (StringUtils.equalsIgnoreCase(v, config.getReportRequestedT())) {
                    r.add(new DateTime().withZone(DateTimeZone.forID("America/Los_Angeles")).toString("dd/MM/yyyy h:mm:ss a z"));
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportResumeT())) {
                    r.add(Ut.getLink(config.getShareResumeLinkT(), o.getResumeId()));
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportCandidateT())) {
                    r.add(o.getCandidate());
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportMeetingT())) {
                    r.add(o.getMeetingLink());
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportStaffingDeskT())) {
                    r.add(o.getStaffingLink());
                } else
                if (StringUtils.equalsIgnoreCase(v, config.getReportCodingT())) {
                    r.add(Ut.getLink(config.getShareLinkTemplate(), o.getCodingId()));
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

        return config.getShareLinkTemplate().replaceAll("%FILE_ID%", fileId);
    }

}
