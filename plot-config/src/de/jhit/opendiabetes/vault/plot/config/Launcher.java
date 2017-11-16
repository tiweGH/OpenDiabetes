package de.jhit.opendiabetes.vault.plot.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Launcher {

	public static void main(String[] args) {
		PlotteriaConfig sampleConfig = new PlotteriaConfig();
		
		try (Writer writer = new FileWriter("config.json")) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(sampleConfig, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
