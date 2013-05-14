package nz.org.nesi.gridinfo;

import com.beust.jcommander.Parameter;

public class GrinTestParameters {
	
	@Parameter(names = {"-c", "--config"}, description = "the url or path to the config", required = true)
	private String path_or_url;
	
	public String getPathOrUrl() {
		return path_or_url;
	}

}
