package tech.oleks.pmtalk.bean;

import com.google.inject.Singleton;

/**
 * Created by alexm on 12/10/16.
 */
public class DriveMap {
    String pmTalkId;
    String codingId;
    String reportId;
    String templateId;

    public String getPmTalkId() {
        return pmTalkId;
    }

    public void setPmTalkId(String pmTalkId) {
        this.pmTalkId = pmTalkId;
    }

    public String getCodingId() {
        return codingId;
    }

    public void setCodingId(String codingId) {
        this.codingId = codingId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
