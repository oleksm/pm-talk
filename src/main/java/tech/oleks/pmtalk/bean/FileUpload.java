package tech.oleks.pmtalk.bean;

import java.io.InputStream;

/**
 * Created by alexm on 12/18/16.
 */
public class FileUpload {
    String fileName;
    String contentType;
    InputStream stream;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
