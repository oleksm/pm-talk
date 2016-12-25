package tech.oleks.pmtalk.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by alexm on 12/11/16.
 */
@Singleton
public class GoogleApiService extends ManagedService {

    private Drive drive;
    private Calendar calendar;
    private Sheets sheets;
    private Credential credential;
    private Gmail gmail;

    /** Global instance of the HTTP transport. */
    private HttpTransport httpTransport;

    /** Global instance of the {@link FileDataStoreFactory}. */
    public AbstractDataStoreFactory dataStoreFactory;

    /** Application name. */
    private static final String APPLICATION_NAME = "PM Talk2";

//    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(
//            System.getProperty("user.home"), ".credentials/pm-talk");

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, CalendarScopes.CALENDAR,
            SheetsScopes.SPREADSHEETS, GmailScopes.MAIL_GOOGLE_COM);

    public GoogleApiService() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = AppEngineDataStoreFactory.getDefaultInstance();

    }

//    static {
//        try {
////            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//            DATA_STORE_FACTORY = new AppEngineDataStoreFactory();
//        } catch (Throwable t) {
//            t.printStackTrace();
//            System.exit(1);
//        }
//    }

    @Override
    public void start() throws IOException {
        drive = new Drive.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(config.getApplicationName())
                .build();
        calendar = new Calendar.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(config.getApplicationName())
                .build();

        sheets = new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(config.getApplicationName())
                .build();

         gmail = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        log.info("Api Service Up and Running");
    }

    public GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
        // Load client secrets.
        InputStream in = GoogleApiService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.

        Collection<CredentialRefreshListener> ls = new ArrayList<>();
        ls.add(new CredentialRefreshListener() {
            @Override
            public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
                log.info("onTokenResponse!");
            }

            @Override
            public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                log.info("onTokenResponse ERROR! " + tokenErrorResponse.getError());
            }
        });

        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .setRefreshListeners(ls)
                .setApprovalPrompt("force")
                .build();
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Sheets getSheets() {
        return sheets;
    }

    public void setSheets(Sheets sheets) {
        this.sheets = sheets;
    }

    public Gmail getGmail() {
        return gmail;
    }

    public void setGmail(Gmail gmail) {
        this.gmail = gmail;
    }
}
