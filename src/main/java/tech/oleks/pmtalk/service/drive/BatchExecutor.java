package tech.oleks.pmtalk.service.drive;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.GenericJson;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import tech.oleks.pmtalk.bean.FileUpload;
import tech.oleks.pmtalk.util.Ut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/25/16.
 */
public class BatchExecutor {

    final Logger log = Logger.getLogger(getClass().getName());

    BatchRequest batch;
    private Drive drive;
    Map<String, String> result = new HashMap<String, String>();

    public BatchExecutor(Drive drive) {
        this.drive = drive;
        batch = drive.batch();
    }

    /**
     *
     * @param fileId
     * @param folderId
     * @param oldFolderIds
     * @return
     * @throws IOException
     */
    public BatchExecutor moveToFolder(String fileId, String folderId, String oldFolderIds) throws IOException {
        drive.files().update(fileId, null)
                .setAddParents(folderId)
                .setRemoveParents(oldFolderIds)
                .queue(batch, new GenericJsonBatchCallback());

        return this;
    }

    /**
     *
     * @param fileId
     * @param domains
     * @param role
     * @return
     * @throws IOException
     */
    public BatchExecutor addDomainPermission(String fileId, String[] domains, String role) throws IOException {
        for (String domain: domains) {
            drive.permissions().create(fileId, new Permission()
                    .setAllowFileDiscovery(false)
                    .setExpirationTime(Ut.toDateTime(new org.joda.time.DateTime().plusDays(14)))
                    .setType("domain")
                    .setDomain(domain)
                    .setRole(role))
                    .setSendNotificationEmail(false)
                    .setFields("id")
                    .queue(batch, new GenericJsonBatchCallback());
        }
        return this;
    }

    /**
     *
     * @param fileId
     * @param role
     * @return
     * @throws IOException
     */
    public BatchExecutor addPublicPermission(String fileId, String role) throws IOException {
        drive.permissions().create(fileId, new Permission()
                .setAllowFileDiscovery(false)
                .setExpirationTime(Ut.toDateTime(new org.joda.time.DateTime().plusDays(14)))
                .setAllowFileDiscovery(false)
                .setType("anyone")
                .setRole(role))
                .setSendNotificationEmail(false)
                .setFields("id")
                .queue(batch, new GenericJsonBatchCallback());
        return this;
    }

    /**
     *
     * @param fileId
     * @param users
     * @return
     * @throws IOException
     */
    public BatchExecutor addUserPermission(String fileId, String[] users, boolean notify) throws IOException {
        for (String user : users) {
            drive.permissions().create(fileId, new Permission()
                    .setType("user")
                    .setEmailAddress(user)
                    .setRole("writer"))
                    .setSendNotificationEmail(notify)
                    .setFields("id")
                    .queue(batch, new GenericJsonBatchCallback());
        }
        return this;
    }

    /**
     *
     * @param fileId
     * @param folderId
     * @param name
     * @param fields
     * @return
     * @throws IOException
     */
    public BatchExecutor copyFile(String fileId, String folderId, String name, String... fields) throws IOException {
        List<String> parents = new ArrayList<>();
        parents.add(folderId);
        File file = new File();
        file.setName(name);
        file.setParents(parents);
        drive.files().copy(fileId, file)
                .setFields("id")
                .queue(batch, new GenericJsonBatchCallback(fields));
        return this;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public BatchExecutor execute() throws IOException {
        batch.execute();
        return this;
    }

    class GenericJsonBatchCallback<T extends GenericJson> extends JsonBatchCallback<T> {

        String[] fields;

        public GenericJsonBatchCallback() {
        }

        public GenericJsonBatchCallback(String[] fields) {
            this.fields = fields;
        }

        @Override
        public void onFailure(GoogleJsonError googleJsonError, HttpHeaders httpHeaders) throws IOException {
            log.severe("onFailure: " + googleJsonError.getMessage());
        }

        @Override
        public void onSuccess(GenericJson json, HttpHeaders httpHeaders) throws IOException {
            log.info("onSuccess: " + json);
            if (fields != null) {
                for (String field: fields) {
                    result.put(field, Ut.notNull((String)json.get("id"), field));
                }
            }
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public String getResult(String key) {
        return result.get(key);
    }
}
