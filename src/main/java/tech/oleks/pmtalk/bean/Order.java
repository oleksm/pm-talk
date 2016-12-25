package tech.oleks.pmtalk.bean;

/**
 * Created by alexm on 12/11/16.
 */
public class Order {
    private String codingLink;
    private String codingId;
    private String meetingLink;
    private String candidate;
    private String staffingLink;
    private FileUpload resume;
    private String resumeId;
    String errors;

    @Override
    public String toString() {
        return "Order{" +
                "codingLink='" + codingLink + '\'' +
                ", codingId='" + codingId + '\'' +
                ", meetingLink='" + meetingLink + '\'' +
                ", candidate='" + candidate + '\'' +
                ", staffingLink='" + staffingLink + '\'' +
                ", resume=" + resume +
                ", resumeId='" + resumeId + '\'' +
                ", errors='" + errors + '\'' +
                '}';
    }

    public String getCodingLink() {
        return codingLink;
    }

    public void setCodingLink(String codingLink) {
        this.codingLink = codingLink;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getStaffingLink() {
        return staffingLink;
    }

    public void setStaffingLink(String staffingLink) {
        this.staffingLink = staffingLink;
    }

    public FileUpload getResume() {
        return resume;
    }

    public void setResume(FileUpload resume) {
        this.resume = resume;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public String getCodingId() {
        return codingId;
    }

    public void setCodingId(String codingId) {
        this.codingId = codingId;
    }
}
