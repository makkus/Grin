package grisu.jcommons.interfaces;

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.jcommons.model.info.Application;
import grisu.jcommons.model.info.Directory;
import grisu.jcommons.model.info.Group;
import grisu.jcommons.model.info.Package;
import grisu.jcommons.model.info.Queue;
import grisu.jcommons.model.info.Site;
import grisu.jcommons.model.info.Version;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;

public class GrinformationManager implements InformationManager {

	private final Grid grid;

	public GrinformationManager() {
		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");
		// "/home/markus/Workspaces/Goji/grin/src/test/resources/test_2_sites.config.groovy");

		grid = ym.getGrid();
	}

	public GrinformationManager(Grid grid) {
		this.grid = grid;
	}

	public GrinformationManager(Map<String, String> config) {
		this();
	}


	public String[] getAllApplicationsAtSite(String site) {
		Site s = grid.getSite(site);

		Collection<Application> result = grid
				.getResources(
						Application.class, s);

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllApplicationsOnGrid() {

		Collection<Application> result = grid.getApplications();

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});

	}


	public String[] getAllApplicationsOnGridForVO(String fqan) {
		Group g = grid.getGroup(fqan);

		Collection<Application> result = grid
				.getResources(Application.class, g);

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllSites() {
		Collection<Site> sites = grid.getSites();

		return Collections2.transform(sites, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllSubmissionLocations() {

		Collection<Queue> queues = grid.getQueues();

		return Collections2.transform(queues, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public Collection<String> getAllSubmissionLocations(String application,
			String version) {

		Collection<Queue> queues = grid.getResources(Queue.class,
				grid.getApplication(application), grid.getVersion(version));

		return Collections2.transform(queues, Functions.toStringFunction());
	}

	public Collection<String> getAllSubmissionLocationsForApplication(
			String application) {

		Collection<Queue> queues = grid.getResources(Queue.class,
				grid.getApplication(application));
		return Collections2.transform(queues, Functions.toStringFunction());

	}

	public Collection<Queue> getAllSubmissionLocationsForVO(String fqan) {

		Collection<Queue> queues = grid.getResources(Queue.class,
				grid.getGroup(fqan));

		return queues;
	}


	public Collection<String> getAllVersionsOfApplicationOnGrid(
			String application) {
		Collection<Version> versions = grid.getResources(Version.class,
				grid.getApplication(application));
		return Collections2.transform(versions, Functions.toStringFunction());
	}


	public Package getApplicationDetails(String application,
			String version, String submissionLocation) {

		Collection<Package> p = grid.getResources(Package.class,
				grid.getApplication(application), grid.getVersion(version),
				grid.getQueue(submissionLocation));

		if ((p == null) || (p.size() == 0)) {
			return null;
		}

		return p.iterator().next();
	}

	public Collection<Application> getApplicationsThatProvideExecutable(
			String executable) {

		Collection<Application> apps = grid.getResources(Application.class,
				grid.getExecutable(executable));
		return apps;
	}

	public Collection<Directory> getDataLocationsForVO(String fqan) {
		Collection<Directory> directories = grid.getResources(Directory.class,
				grid.getGroup(fqan));
		return directories;
	}

	public Queue getGridResource(String submissionLocation) {
		return grid.getQueue(submissionLocation);
	}

	public String getJobmanagerOfQueueAtSite(String site, String queue) {
		throw new NotImplementedException();
	}

	public Site getSiteForHostOrUrl(String host_or_url) {

		return grid.getSiteForUrl(host_or_url);
	}

	public Set<Directory> getStagingFileSystemForSubmissionLocation(
			String subLoc) {
		Queue queue = grid.getQueue(subLoc);

		return queue.getDirectories();
	}

	public Collection<Version> getVersionsOfApplicationOnSite(
			String application,
			String site) {

		Collection<Version> versions = grid.getResources(Version.class,
				grid.getApplication(application), grid.getSite(site));

		return versions;

	}

	public Collection<Version> getVersionsOfApplicationOnSubmissionLocation(
			String application, String submissionLocation) {

		Collection<Version> versions = grid.getResources(Version.class,
				grid.getApplication(application),
				grid.getQueue(submissionLocation));

		return versions;

	}


}