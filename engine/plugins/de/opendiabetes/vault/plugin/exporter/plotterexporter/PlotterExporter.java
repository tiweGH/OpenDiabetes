package de.opendiabetes.vault.plugin.exporter.plotterexporter;

import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.plugin.exporter.VaultExporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Wrapper class for the PlotterExporter plugin.
 *
 * @author ocastx
 */
public class PlotterExporter extends Plugin {

    /**
     * Path of the plugin directory.
     */
    private static Path pluginPath = null;

    /**
     * Constructor for the {@link org.pf4j.PluginManager}.
     *
     * @param wrapper The {@link org.pf4j.PluginWrapper}.
     */
    public PlotterExporter(final PluginWrapper wrapper) {
        super(wrapper);
        pluginPath = this.wrapper.getPluginPath().toAbsolutePath();
    }

    /**
     * Actual implementation of the PlotterExporter.
     */
    @Extension
    public static final class PlotterExporterImplementation extends VaultExporter {

        /**
         * The default temporary directory to use.
         */
        private static final String DEFAULT_TEMP_DIR = System.getProperty("java.io.tmpdir") + "PlotterExporter";

        /**
         * The default temporary filename to use.
         */
        private static final String DEFAULT_TEMP_FILENAME = "export.csv";

        /**
         * Supported plot formats.
         */
        private enum PlotFormats {

            /**
             * Image plot format.
             */
            IMAGE,

            /**
             * LaTeX PDF plot format.
             */
            PDF
        }

        /**
         * Selected plot format read from the configuration.
         */
        private PlotFormats plotFormat;

        /**
         * Path to the plotting script.
         */
        private String scriptPath = PlotterExporter.pluginPath.resolve("assets/plot.py").toString();

        /**
         * Runs the plot script.
         * @param dataPath path to the data that should be plotted
         * @param outputPath path where the plot should be written to
         * @return boolean value indicating whether the command was successful or not
         */
        private boolean plotData(final String dataPath, final String outputPath) {
            BufferedReader stdInput = null;
            BufferedReader stdError = null;
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"python", this.scriptPath, "-f", dataPath, "-o", outputPath});

                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                InputStreamReader errorStreamReader = new InputStreamReader(errorStream, StandardCharsets.UTF_8);

                stdInput = new BufferedReader(inputStreamReader);
                stdError = new BufferedReader(errorStreamReader);

                // read the output from the command
                String line = null;
                while ((line = stdInput.readLine()) != null) {
                    LOG.log(Level.INFO, line);
                }

                // read any errors from the attempted command
                int errorCounter = 0;
                while ((line = stdError.readLine()) != null) {
                    LOG.log(Level.SEVERE, line);
                    errorCounter++;
                }


                return (errorCounter == 0);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error while executing command");
                return false;
            } finally {
                try {
                    if (stdInput != null) {
                        stdInput.close();
                    }
                } catch (IOException exception) {
                    LOG.log(Level.FINE, "Input Stream was already closed");
                }
                try {
                    if (stdError != null) {
                        stdError.close();
                    }
                } catch (IOException exception) {
                    LOG.log(Level.FINE, "Error Stream was already closed");
                }
            }
        }

        /**
         * Checks if the given command outputs the expected value.
         * @param cmds array of command arguments that should be executed
         * @param check string value which will be used as check argument
         * @return boolean value indicating whether the command is installed or not
         */
        private static boolean isCommandInstalled(final String[] cmds, final String check) {
            BufferedReader stdInput = null;
            BufferedReader stdError = null;
            try {
                Process process = Runtime.getRuntime().exec(cmds);

                stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                stdError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

                // read the output from the command
                String line = null;
                boolean installed = false;
                while ((line = stdInput.readLine()) != null) {
                    LOG.log(Level.INFO, line);
                    if (line.contains(check)) {
                        installed = true;
                        break;
                    }
                }

                if (!installed) {
                    // read any errors from the attempted command
                    LOG.log(Level.SEVERE, "Could not check command with errors:\n");
                    while ((line = stdError.readLine()) != null) {
                        LOG.log(Level.SEVERE, line);
                    }
                    return false;
                }

                return true;
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Error while checking for command " + check);
                return false;
            } finally {
                try {
                    if (stdInput != null) {
                        stdInput.close();
                    }
                } catch (IOException exception) {
                    LOG.log(Level.FINE, "Input Stream was already closed");
                }
                try {
                    if (stdError != null) {
                        stdError.close();
                    }
                } catch (IOException exception) {
                    LOG.log(Level.FINE, "Error Stream was already closed");
                }
            }
        }

        /**
         * Checks if python is installed on the current machine.
         * @return true if python is installed, false if not
         */
        private static boolean isPythonInstalled() {
            return isCommandInstalled(new String[]{"python", "--version"}, "Python");
        }

        /**
         * Checks if LaTeX is installed on the current machine.
         * @return true if LaTeX is installed, false if not
         */
        private static boolean isLaTeXInstalled() {
            return isCommandInstalled(new String[]{"pdflatex", "--version"}, "pdfTeX");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void writeToFile(final String filePath, final List<ExportEntry> csvEntries) throws IOException {

            boolean python = isPythonInstalled();
            if (!python) {
                throw new IOException("Cannot plot data because python was not found");
            }

            if (plotFormat == PlotFormats.PDF) {
                boolean latex = isLaTeXInstalled();

                if (!latex) {
                    throw new IOException("Cannot plot data to pdf file because pdflatex was not found");
                }
            }

            String tempPath = DEFAULT_TEMP_DIR + File.pathSeparator + DEFAULT_TEMP_FILENAME;

            super.writeToFile(tempPath, csvEntries);
            if (!plotData(tempPath, filePath)) {
                LOG.log(Level.SEVERE, "Failed to plot data");
            }


            File file = new File(tempPath);
            if (!file.delete()) {
                LOG.log(Level.SEVERE, "Failed to delete temp export file");
            }
        }

        /**
         * Loads the configuration for the exporter plugin.
         *
         * @param configuration The configuration object.
         * @return True if configuration can be loaded, false otherwise.
         */
        @Override
        public boolean loadPluginSpecificConfiguration(final Properties configuration) {

            this.notifyStatus(0, "Loading configuration");

            String format = configuration.getProperty("plotFormat");

            if (format.equals("pdf")) {
                plotFormat = PlotFormats.PDF;
            } else {
                // Default is always "image"
                plotFormat = PlotFormats.IMAGE;
            }

            return true;
        }

    }
}
