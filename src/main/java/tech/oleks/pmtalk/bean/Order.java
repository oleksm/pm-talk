package tech.oleks.pmtalk.bean;

import java.io.InputStream;

/**
 * Created by alexm on 12/11/16.
 */
public class Order {
    private String folderId;
    private String codingLink;
    private String meetingLink;
    private String candidate;
    private String position;
    private FileUpload resume;
    String errors;

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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
