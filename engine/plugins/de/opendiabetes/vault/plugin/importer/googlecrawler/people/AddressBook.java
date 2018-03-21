package de.opendiabetes.vault.plugin.importer.googlecrawler.people;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Address book controller.
 */
public final class AddressBook {

    /**
     * Divisor to calculate seconds from milliseconds.
     */
    private static final int MILLISECONDS_TO_SECONDS_DIVISOR = 1000;

    /**
     * Singleton instance.
     */
    private static AddressBook instance;

    /**
     * List of contacts in the address book.
     */
    private List<Contact> contacts;

    /**
     * Constructor.
     */
    private AddressBook() {
        contacts = new ArrayList<>();
    }

    /**
     * Getter for the singleton instance.
     * @return the singleton instance.
     */
    public static AddressBook getInstance() {
        if (AddressBook.instance == null) {
            AddressBook.instance = new AddressBook();
        }
        return AddressBook.instance;
    }

    /**
     * Adds a contact to the address book.
     * @param cnt the contact object to be added
     */
    public void addContact(final Contact cnt) {
        contacts.add(cnt);
    }

    /**
     * Adds a list of contacts to the address book.
     * @param cnt the list of contacts that will be added
     */
    public void addMultipleContacts(final List<Contact> cnt) {
        contacts.addAll(cnt);
    }

    /**
     * Checks if the address book is empty.
     * @return true if the address book is empty, false otherwise
     */
    public boolean isEmpty() {
        return contacts.isEmpty();
    }

    /**
     * Gets the size of the address book.
     * @return the number of contacts in the address book
     */
    public int size() {
        return contacts.size();
    }

    /**
     * Gets a contact by its ID.
     * @param id the ID of the contact that should be returned
     * @return the contact with the given ID
     */
    public Contact getContactById(final int id) {
        return contacts.get(id);
    }

    /**
     * Gets a contact by its name.
     * @param name the name of the contact that should be returned
     * @return the contact with the given name
     */
    public Contact getContactByName(final String name) {
        return contacts.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Exports the recorded address book as JSON file.
     */
    public void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String output = gson.toJson(this);

        File file = new File("addressBook" + System.currentTimeMillis() / MILLISECONDS_TO_SECONDS_DIVISOR + ".json");

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, false),
                    StandardCharsets.UTF_8);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
