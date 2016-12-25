package tech.oleks.pmtalk.service;


import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.bean.DriveMap;
import tech.oleks.pmtalk.bean.FileUpload;
import tech.oleks.pmtalk.bean.Order;

import tech.oleks.pmtalk.service.drive.BatchExecutor;

import tech.oleks.pmtalk.util.Ut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tech.oleks.pmtalk.util.Ut.notNull;

/**
 * Created by alexm on 12/9/16.
 */
@Singleton
public class DriveService extends ManagedService {

    @Inject GoogleApiService api;

    DriveMap driveMap;

    @Override
    public void start() throws IOException {
        log.info("Starting Drive Service...");
        driveMap = exploreDrive();
        log.info("Starting Drive Service... DONE.");
    }

    /**
     *
     * @param o
     * @throws IOException
     */
    public void minimal(Order o) throws IOException {
        String candidate = o.getCandidate();
        String codingTId = driveMap.getCodingId();
        String pmTalkId = driveMap.getPmTalkId();

        BatchExecutor batch = new BatchExecutor(api.getDrive())
                .copyFile(codingTId, pmTalkId, config.getCodingTemplateName().replaceAll("%NAME%", candidate), "codingId")
                .execute();

        o.setCodingId(batch.getResult("codingId"));


        String resumeId = uploadResume(o.getResume());
        o.setResumeId(resumeId);
    }

    /**
     *
     * @param o
     * @throws IOException
     */
    public String complete(Order o) throws IOException {
        String candidate = o.getCandidate();
        String folderId = createFolder(candidate);
        String codingId = o.getCodingId();
        String resumeId = o.getResumeId();
        String reportTId = driveMap.getReportId();

        BatchExecutor batch = new BatchExecutor(api.getDrive())
                .moveToFolder(codingId, folderId, driveMap.getPmTalkId())
                .addPublicPermission(codingId, "writer")
                .moveToFolder(resumeId, folderId, driveMap.getPmTalkId())
                .addDomainPermission(resumeId, config.getShareWithDomains(), "commenter")
                .addUserPermission(resumeId, config.getShareWithPeople(), false)
                .copyFile(reportTId, folderId, config.getReportTemplateName().replaceAll("%NAME%", candidate), "reportId")
                .execute();

        String reportId = batch.getResult("reportId");

        new BatchExecutor(api.getDrive())
                .addUserPermission(reportId, config.getShareWithPeople(), true)
                .execute();

        return reportId;
    }

    /**
     *
     * @param resume
     * @return 1Fyl2mjAAmTyRo-0FV_fGdySlmJ0jSQSQF5aw3XnSCAA
     * @throws IOException
     */
    public String uploadResume(FileUpload resume) throws IOException {
        List<String> parents = new ArrayList<>();
        parents.add(driveMap.getPmTalkId());
        File file = new File();
        file.setName(resume.getFileName());
        file.setParents(parents);
        file.setMimeType(resume.getContentType());
        InputStreamContent content = new InputStreamContent(resume.getContentType(), resume.getStream());
        File result = api.getDrive().files().create(file, content)
                .setFields("id")
                .execute();
        return notNull(result.getId(), "Upload Resume");
    }

    /**
     *
     * @param candidate
     * @return 1Fyl2mjAAmTyRo-0FV_fGdySlmJ0jSQSQF5aw3XnSCAA
     * @throws IOException
     */
    public String createCodingDoc(String candidate) throws IOException {
        String name = config.getCodingTemplateName().replaceAll("%NAME%", candidate);
        List<String> parents = new ArrayList<>();
        parents.add(driveMap.getPmTalkId());
        File file = new File();
        file.setName(name);
        file.setParents(parents);
        File copied = api.getDrive().files().copy(driveMap.getCodingId(), file)
                .setFields("id")
                .execute();
        return notNull(copied.getId(), "Copy Coding Doc " + candidate);
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
        return Ut.notNull(file.getId(), name);
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

        log.info("Drive Mapped");
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
        log.info(String.format("Extracting %s from result %d", fn, fl.getFiles().size()));
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
