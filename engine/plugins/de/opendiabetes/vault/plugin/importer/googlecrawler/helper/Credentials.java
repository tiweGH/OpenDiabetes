package de.opendiabetes.vault.plugin.importer.googlecrawler.helper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fitness.FitnessScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Class to authenticate against the Google services.
 */
public final class Credentials {

    /**
     * Singleton instance.
     */
    private static Credentials instance;

    /**
     * The application name of the Google Application used.
     */
    private static final String APPLICATION_NAME =
            "BachelorArbeit";

    /**
     * HttpTransport that holds the cookies etc. from the started connection.
     */
    private static HttpTransport httpTransport;

    /**
     * JsonFactory for parsing json objects needed by Google.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Scopes which are requested at authentication.
     */
    private static final List<String> SCOPES =
            Arrays.asList(
                    "https://www.googleapis.com/auth/contacts.readonly",
                    "https://www.googleapis.com/auth/plus.login",
                    FitnessScopes.FITNESS_ACTIVITY_READ,
                    FitnessScopes.FITNESS_BODY_READ,
                    FitnessScopes.FITNESS_LOCATION_READ
            );

    /**
     * File which holds the access tokens issued by Google after successful authentication.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"),
            ".credentials/googleapis.de-nkpyck-googledatagatherer");

    /**
     * Data storage instance needed by Google.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /**
     * Singleton instance.
     */
    private static Credential credential;

    /**
     * API Key used for Google Services.
     */
    private static String apiKey;


    /**
     * Constructor.
     */
    private Credentials() {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for singleton instance.
     * @return singleton credentials instance
     */
    public static Credentials getInstance() {
        if (Credentials.instance == null) {
            Credentials.instance = new Credentials();
        }
        return Credentials.instance;
    }

    /**
     * Authorizes the client against the Google services.
     * @param path - path to the credentials file
     * @throws IOException - thrown if the credentials file could not be read/written
     */
    public void authorize(final String path) throws IOException {
        File file = new File(Paths.get(path).toAbsolutePath().toString());
        // Load client secrets.
        FileInputStream fileInput = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInput, Charset.defaultCharset());

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, reader);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");

    }

    /**
     * Getter for http transport.
     * @return the http transport
     */
    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    /**
     * Getter for JsonFactory.
     * @return the json factory.
     */
    public JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }

    /**
     * Getter for the application name.
     * @return the application name string
     */
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    /**
     * Getter for the singleton instance.
     * @return the singleton instance
     */
    public Credential getCredential() {
        return credential;
    }

    /**
     * Getter for the API key.
     * @return the api key for the google services
     */
    public String getAPIKey() {
        return apiKey;
    }

    /**
     * Setter for the API key.
     * @param apiKey the API key used by the google services
     */
    public static void setAPIkey(final String apiKey) {
        Credentials.apiKey = apiKey;
    }
}

