package tech.oleks.pmtalk.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.time.DateTime;
import tech.oleks.pmtalk.bean.DriveMap;
import tech.oleks.pmtalk.bean.FileUpload;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexm on 12/9/16.
 */
@Singleton
public class DriveService extends ManagedService {

    @Inject GoogleApiService api;

    DriveMap driveMap;

    @Override
    public void start() throws IOException {
        System.out.println("Starting Drive Service...");
        driveMap = exploreDrive();
        System.out.println("Starting Drive Service... DONE.");
    }

    /**
     *
     * @param resume
     * @param folderId
     * @return https://docs.google.com/document/d/1Fyl2mjAAmTyRo-0FV_fGdySlmJ0jSQSQF5aw3XnSCAA/edit?usp=sharing
     * @throws IOException
     */
    public String uploadResume(FileUpload resume, String folderId) throws IOException {
        List<String> parents = new ArrayList<>();
        parents.add(folderId);
        File file = new File();
        file.setName(resume.getFileName());
        file.setParents(parents);
        file.setMimeType(resume.getContentType());
        InputStreamContent content = new InputStreamContent(resume.getContentType(), resume.getStream());
        File result = api.getDrive().files().create(file, content)
                .setFields("id")
                .execute();
        return config.getShareResumeLinkT().replaceAll("%FILE_ID%", result.getId());
    }

    /**
     *
     * @param folderId
     * @param candidate
     * @return https://docs.google.com/document/d/1Fyl2mjAAmTyRo-0FV_fGdySlmJ0jSQSQF5aw3XnSCAA/edit?usp=sharing
     */
    public String copyCodingDoc(String folderId, String candidate) throws IOException {
        String name = config.getCodingTemplateName().replaceAll("%NAME%", candidate);
        List<String> parents = new ArrayList<>();
        parents.add(folderId);
        File file = new File();
        file.setName(name);
        file.setParents(parents);
        File copied = api.getDrive().files().copy(driveMap.getCodingId(), file)
                .setFields("id")
                .execute();
        notNull(copied.getId(), "Copy Coding Doc " + candidate);
        api.getDrive().permissions().create(copied.getId(), new Permission()
                .setAllowFileDiscovery(false)
                .setExpirationTime(toDateTime(new org.joda.time.DateTime().plusDays(14)))
                .setType("anyone")
                .setRole("writer"))
                .setFields("id")
                .execute();
        System.out.println(toDateTime(new DateTime().plusDays(14)));
        return config.getShareLinkTemplate().replaceAll("%FILE_ID%", copied.getId());
    }

    /**
     *
     * @param folderId
     * @param candidate
     * @return
     * @throws IOException
     */
    public String copyReport(String folderId, String candidate) throws IOException {
        String name = config.getReportTemplateName().replaceAll("%NAME%", candidate);
        List<String> parents = new ArrayList<>();
        parents.add(folderId);
        File file = new File();
        file.setName(name);
        file.setParents(parents);
        File copied = api.getDrive().files().copy(driveMap.getReportId(), file)
                .setFields("id")
                .execute();
        notNull(copied.getId(), "PM Talk Doc " + candidate);
        return copied.getId();
    }

    /**
     *
     * @param name
     * @return
     */
    public String createFolder(String name) throws IOException {
        File folder = new File();
        List<String> parents = new ArrayList<>();
        parents.add(driveMap.getPmTalkId());
        folder.setName(name);
        folder.setMimeType("application/vnd.google-apps.folder");
        folder.setParents(parents);
        File file = api.getDrive().files().create(folder)
                .setFields("id")
                .execute();
        return notNull(file.getId(), name);
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private DriveMap exploreDrive() throws IOException {
        DriveMap driveMap = new DriveMap();

        String q = String.format("not trashed and (('root' in parents and name = '%s') or name = '%s' or name = '%s' or name = '%s')",
                config.getPmTalkDirName(),
                config.getCodingTemplateName(),
                config.getReportTemplateName(),
                config.getTemplateDirName());

        FileList result = api.getDrive().files().list()
                .setQ(q)
                .setFields("files(id, name, parents, properties, appProperties)")
                .execute();

        String pmTalkId = extract(result, config.getPmTalkDirName(), null);
        String templateId = extract(result, config.getTemplateDirName(), pmTalkId);
        String reportId = extract(result, config.getReportTemplateName(), templateId);
        String codingId = extract(result, config.getCodingTemplateName(), templateId);
        driveMap.setCodingId(codingId);
        driveMap.setPmTalkId(pmTalkId);
        driveMap.setReportId(reportId);
        driveMap.setTemplateId(templateId);

        System.out.println("Drive Mapped");
        return driveMap;
    }

    /**
     *
     * @param fl
     * @param fn
     * @param pfn
     * @return
     */
    private String extract(FileList fl, String fn, String pfn) {
        System.out.println(String.format("Extracting %s from result %d", fn, fl.getFiles().size()));
        for (File f: fl.getFiles()) {
            if (fn.equalsIgnoreCase(f.getName())) {
                if (pfn == null || f.getParents().contains(pfn)) {
                    return notNull(f.getId(), fn);
                }
            }
        }
        return notNull(null, fn);
    }

    public DriveMap getDriveMap() {
        return driveMap;
    }

    public void setDriveMap(DriveMap driveMap) {
        this.driveMap = driveMap;
    }
}
