package grisu.jcommons.interfaces;

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.grin.model.resources.Application;
import grisu.grin.model.resources.Site;

import java.util.Map;

public class GrinformationManager implements InformationManager {

	private final Grid grid;

	public GrinformationManager() {
		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");

		grid = ym.getGrid();
	}

	public Application[] getAllApplicationsAtSite(Site site) {

		grid.getResources(Application.class, site);
		return null;
	}

	public String[] getAllApplicationsOnGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllApplicationsOnGridForVO(String fqan) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, GridResource> getAllGridResources() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getAllHosts() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSites() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSubmissionLocations() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSubmissionLocations(String application, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSubmissionLocationsForApplication(String application) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSubmissionLocationsForSite(String site) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllSubmissionLocationsForVO(String fqan) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllVersionsOfApplicationOnGrid(String application) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllVersionsOfApplicationOnGridForVO(String application,
			String vo) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getApplicationDetails(String application,
			String version, String submissionLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getApplicationsThatProvideExecutable(String executable) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String[]> getDataLocationsForVO(String fqan) {
		// TODO Auto-generated method stub
		return null;
	}

	public GridResource getGridResource(String submissionLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getJobmanagerOfQueueAtSite(String site, String queue) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSiteForHostOrUrl(String host_or_url) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getStagingFileSystemForSubmissionLocation(String subLoc) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getVersionsOfApplicationOnSite(String application,
			String site) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getVersionsOfApplicationOnSubmissionLocation(
			String application, String submissionLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isVolatileDataLocation(String host, String path, String fqan) {
		// TODO Auto-generated method stub
		return false;
	}

}
