package tech.oleks.pmtalk.service;

import com.google.inject.Singleton;

/**
 * Created by alexm on 12/10/16.
 */
@Singleton
public class Configuration {
    String codingTemplateName;
    String reportTemplateName;
    String templateDirName;
    String pmTalkDirName;
    String shareLinkTemplate;
    String shareResumeLinkT;
    String eventNameTemplate;
    String defaultUserId;
    String applicationName;
    String sheetRange;
    String reportRequestedT;
    String reportConductedT;
    String reportCandidateT;
    String reportResumeT;
    String reportMeetingT;
    String reportPositionT;
    String reportStaffingDeskT;
    String reportRequsitesT;
    String reportCodingT;
    String[] shareWithPeople;
    String[] shareWithDomains;
    String analyticsId;
    String emailSubjectT;

    public String getCodingTemplateName() {
        return codingTemplateName;
    }

    public void setCodingTemplateName(String codingTemplateName) {
        this.codingTemplateName = codingTemplateName;
    }

    public String getReportTemplateName() {
        return reportTemplateName;
    }

    public void setReportTemplateName(String reportTemplateName) {
        this.reportTemplateName = reportTemplateName;
    }

    public String getPmTalkDirName() {
        return pmTalkDirName;
    }

    public void setPmTalkDirName(String pmTalkDirName) {
        this.pmTalkDirName = pmTalkDirName;
    }

    public String getTemplateDirName() {
        return templateDirName;
    }

    public void setTemplateDirName(String templateDirName) {
        this.templateDirName = templateDirName;
    }

    public String getShareLinkTemplate() {
        return shareLinkTemplate;
    }

    public void setShareLinkTemplate(String shareLinkTemplate) {
        this.shareLinkTemplate = shareLinkTemplate;
    }

    public String getEventNameTemplate() {
        return eventNameTemplate;
    }

    public void setEventNameTemplate(String eventNameTemplate) {
        this.eventNameTemplate = eventNameTemplate;
    }

    public String getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSheetRange() {
        return sheetRange;
    }

    public void setSheetRange(String sheetRange) {
        this.sheetRange = sheetRange;
    }

    public String getReportRequestedT() {
        return reportRequestedT;
    }

    public void setReportRequestedT(String reportRequestedT) {
        this.reportRequestedT = reportRequestedT;
    }

    public String getReportConductedT() {
        return reportConductedT;
    }

    public void setReportConductedT(String reportConductedT) {
        this.reportConductedT = reportConductedT;
    }

    public String getReportResumeT() {
        return reportResumeT;
    }

    public void setReportResumeT(String reportResumeT) {
        this.reportResumeT = reportResumeT;
    }

    public String getReportMeetingT() {
        return reportMeetingT;
    }

    public void setReportMeetingT(String reportMeetingT) {
        this.reportMeetingT = reportMeetingT;
    }

    public String getReportPositionT() {
        return reportPositionT;
    }

    public void setReportPositionT(String reportPositionT) {
        this.reportPositionT = reportPositionT;
    }

    public String getReportStaffingDeskT() {
        return reportStaffingDeskT;
    }

    public void setReportStaffingDeskT(String reportStaffingDeskT) {
        this.reportStaffingDeskT = reportStaffingDeskT;
    }

    public String getReportRequsitesT() {
        return reportRequsitesT;
    }

    public void setReportRequsitesT(String reportRequsitesT) {
        this.reportRequsitesT = reportRequsitesT;
    }

    public String getReportCodingT() {
        return reportCodingT;
    }

    public void setReportCodingT(String reportCodingT) {
        this.reportCodingT = reportCodingT;
    }

    public String getShareResumeLinkT() {
        return shareResumeLinkT;
    }

    public void setShareResumeLinkT(String shareResumeLinkT) {
        this.shareResumeLinkT = shareResumeLinkT;
    }

    public String getAnalyticsId() {
        return analyticsId;
    }

    public void setAnalyticsId(String analyticsId) {
        this.analyticsId = analyticsId;
    }

    public String getEmailSubjectT() {
        return emailSubjectT;
    }

    public void setEmailSubjectT(String emailSubjectT) {
        this.emailSubjectT = emailSubjectT;
    }

    public String getReportCandidateT() {
        return reportCandidateT;
    }

    public void setReportCandidateT(String reportCandidateT) {
        this.reportCandidateT = reportCandidateT;
    }

    public String[] getShareWithPeople() {
        return shareWithPeople;
    }

    public void setShareWithPeople(String[] shareWithPeople) {
        this.shareWithPeople = shareWithPeople;
    }

    public String[] getShareWithDomains() {
        return shareWithDomains;
    }

    public void setShareWithDomains(String[] shareWithDomains) {
        this.shareWithDomains = shareWithDomains;
    }
}
