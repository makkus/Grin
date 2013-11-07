package nz.org.nesi.gridinfo;

import com.beust.jcommander.Parameter;

import java.util.List;

public class GrinInfoDisplayParameters {
	
	@Parameter(names = {"-c", "--config"}, description = "the url or path to the config", required = true)
	private String path_or_url;

    @Parameter(names = {"-r", "--resource"}, description = "the resource to display (e.g. Package, Directory, ...")
    private List<String> resource_to_display;
	
	public String getPathOrUrl() {
		return path_or_url;
	}

    public List<String> getResourceToDisplay() {
        return resource_to_display;
    }
}
