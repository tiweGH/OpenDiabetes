package de.opendiabetes.vault.plugin.importer.googlecrawler.people;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Contact;

import java.io.IOException;
import java.util.List;

/**
 * Google People service.
 */
public final class GooglePeople {

    /**
     * Page size of the address book to get.
     */
    private static final int PAGE_SIZE = 500;

    /**
     * Singleton instance.
     */
    private static GooglePeople instance;

    /**
     * Current logged in user's profile.
     */
    private Person profile;

    /**
     * Google People service.
     */
    private PeopleService peopleService;


    /**
     * Constructor.
     */
    private GooglePeople() {
        construct();
    }

    /**
     * Getter for the singleton instance.
     * @return the singleton instance.
     */
    public static GooglePeople getInstance() {
        if (GooglePeople.instance == null) {
            GooglePeople.instance = new GooglePeople();
        }
        return GooglePeople.instance;
    }

    /**
     * Fetches the profile of the currently logged in user.
     */
    private void getOwnProfile() {
        try {
            profile = peopleService.people().get("people/me")
                    .setPersonFields("names,addresses")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the list of all profiles from the currently logged in user and passes them into the address book controller.
     */
    public void getAllProfiles() {
        try {
            List<Person> persons = peopleService
                    .people()
                    .connections()
                    .list("people/me")
                    .setPersonFields("names,addresses")
                    .setPageSize(PAGE_SIZE)
                    .execute()
                    .getConnections();

            for (Person p : persons) {
                if (p.getAddresses() != null) {
                    Contact c = new Contact(p.getNames().get(0).getDisplayName(), p.getAddresses());
                    AddressBook.getInstance().addContact(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs the Google People Service.
     */
    private void construct() {
        peopleService = new PeopleService.Builder(
                Credentials.getInstance().getHttpTransport(),
                Credentials.getInstance().getJsonFactory(),
                Credentials.getInstance().getCredential())
                .setApplicationName(Credentials.getInstance().getApplicationName())
                .build();
        getOwnProfile();
    }

    /**
     * Getter for the currently logged in user's profile.
     * @return a user profile
     */
    public Person getProfile() {
        return profile;
    }
}
