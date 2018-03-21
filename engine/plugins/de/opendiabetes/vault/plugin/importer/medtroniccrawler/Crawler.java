package de.opendiabetes.vault.plugin.importer.medtroniccrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * Actual class for crawling the minimed web page.
 */
public class Crawler {

    /**
     * Time until the medtroniccrawler should try to finish the action before timing out.
     */
    private static final int CONNECT_TIMEOUT = 60000;

    /**
     * The user agent when crawling the web page.
     */
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/56.0.2924.87 Safari/537.36";

    /**
     * Checks if the login and the entered dates are valid and then downloads the csv file.
     *
     * @param loginCookies - the login cookies to be sent.
     * @param startDate - start date from when the csv file data should begin.
     * @param endDate - end date until when the csv file data should go.
     * @param userWorkingDirectory - the directory where the csv file should be downloaded to.
     * @throws IOException - Thrown if there was something wrong while executing the request.
     * @throws UnsupportedEncodingException - Thrown if the encoding of the request is not supported.
     * @throws FileNotFoundException - Thrown if the path at which the file should be written to was not found.
     */
    public void generateDocument(final Map<String, String> loginCookies,
                                 final String startDate,
                                 final String endDate,
                                 final String userWorkingDirectory) throws
            UnsupportedEncodingException,
            FileNotFoundException,
            IOException {
            Connection.Response reportDocument = Jsoup
                    .connect("https://carelink.minimed.eu/patient/main/selectCSV.do?t=11?t=11?t=11?t=11").timeout(CONNECT_TIMEOUT)
                    /* .ignoreContentType(false).userAgent(userAgent)*/.cookies(loginCookies)
                    /* .header("Content-Type", "text/csv; charset=UTF-8")
                    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,;q=0.8")
                    .header("Content-length", "101")
                    .data("org.apache.struts.taglib.html.TOKEN", "53f325519c62adcec1a3128908017474")*/
                    .data("report", "11").data("listSeparator", ",")
                    //.data("customerID","50577452") // customer Id can be
                    // optional.
                    .data("datePicker2", startDate) // start date
                    .data("datePicker1", endDate) // End date
                    /*.header("X-Requested-With", "XMLHttpRequest")*/.method(Connection.Method.GET).execute();

            String outputFolder = userWorkingDirectory + File.separator + "careLink-Export";

            PrintWriter printWriter = new PrintWriter(new File(outputFolder + (new Date().getTime()) + ".csv"), "UTF-8");
            printWriter.write(reportDocument.body());
            printWriter.close();
            System.out.println("Export Sucessfull!");
            System.out.println("File will be saved to location " + userWorkingDirectory + " with name: " + "\"careLink-Export"
                    + (new Date().getTime()) + ".csv\"");

    }
}
