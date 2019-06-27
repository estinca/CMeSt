package com.est.utils.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.est.utils.config.exception.JsonConfigException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPropertiesConfigUtils {

	private JsonPropertiesConfigUtils() {}
	
	public static <T> T loadJsonConfig(String filePath, Class<T> clazz) throws JsonConfigException {
		Path path = Paths.get(filePath);
		
		if(!Files.exists(path)) {
			System.out.println(path.toAbsolutePath());
			throw new JsonConfigException("File " + filePath + " does not exists");
		}
		
		if(Files.isDirectory(path) || !FilenameUtils.getExtension(filePath).equalsIgnoreCase("json")) {
			throw new JsonConfigException("File " + filePath +  " is not a json file format");
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(path.toFile(), clazz);
		} catch (Exception e) {
			throw new JsonConfigException("Error parsing config file " + filePath);
		}
		
	}
}
