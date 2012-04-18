package grisu.jcommons.interfaces;

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.model.info.AbstractResource;
import grisu.jcommons.model.info.Application;
import grisu.jcommons.model.info.Directory;
import grisu.jcommons.model.info.Group;
import grisu.jcommons.model.info.Package;
import grisu.jcommons.model.info.Queue;
import grisu.jcommons.model.info.Site;
import grisu.jcommons.model.info.VO;
import grisu.jcommons.model.info.Version;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class GrinformationManager implements InformationManager {

	static final Logger myLogger = LoggerFactory
			.getLogger(GrinformationManager.class.getName());

	private YnfoManager ym;

	private final String path;


	// public GrinformationManager(Grid grid) {
	// this.grid = grid;
	// this.path = null;
	// }

	public GrinformationManager(Map<String, String> config) {
		this(config.get("path"));

	}

	public GrinformationManager(String path) {
		this.path = path;
		refresh();
		myLogger.debug("Grinformationmanager created.");
	}

	public Collection<Queue> findQueues(Map<JobSubmissionProperty, String> job,
			String fqan) {

		return getGrid().findQueues(job, fqan);
	}


	public String[] getAllApplicationsAtSite(String site) {
		Site s = getGrid().getSite(site);

		Collection<Application> result = getGrid()
				.getResources(
						Application.class, s);

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllApplicationsOnGrid() {

		Collection<Application> result = getGrid().getApplications();

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});

	}


	public String[] getAllApplicationsOnGridForVO(String fqan) {
		Group g = getGrid().getGroup(fqan);

		Collection<Application> result = getGrid()
				.getResources(Application.class, g);

		return Collections2.transform(result, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllSites() {
		Collection<Site> sites = getGrid().getSites();

		return Collections2.transform(sites, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public String[] getAllSubmissionLocations() {

		Collection<Queue> queues = getGrid().getQueues();

		return Collections2.transform(queues, Functions.toStringFunction())
				.toArray(new String[] {});
	}

	public Collection<String> getAllSubmissionLocations(String application,
			String version) {

		Collection<Queue> queues = getGrid().getResources(Queue.class,
				getGrid().getApplication(application),
				getGrid().getVersion(version));

		return Collections2.transform(queues, Functions.toStringFunction());
	}

	public Collection<String> getAllSubmissionLocationsForApplication(
			String application) {

		Collection<Queue> queues = getGrid().getResources(Queue.class,
				getGrid().getApplication(application));
		return Collections2.transform(queues, Functions.toStringFunction());

	}

	public Collection<Queue> getAllSubmissionLocationsForVO(String fqan) {

		final Group group = getGrid().getGroup(fqan);
		Collection<Queue> queues = getGrid().getResources(Queue.class, group);

		// filter queues again, because recursive connections can include groups
		// that belong to Directories
		return Collections2.filter(queues, new Predicate<Queue>() {

			public boolean apply(Queue input) {
				return input.getGroups().contains(group);
			}
		});

	}


	public Collection<String> getAllVersionsOfApplicationOnGrid(
			String application) {
		Collection<Version> versions = getGrid().getResources(Version.class,
				getGrid().getApplication(application));
		return Collections2.transform(versions, Functions.toStringFunction());
	}


	public Set<VO> getAllVOs() {
		return getGrid().getVos();
	}

	public Package getApplicationDetails(String application,
			String version, String submissionLocation) {

		Collection<Package> p = getGrid().getResources(Package.class,
				getGrid().getApplication(application),
				getGrid().getVersion(version),
				getGrid().getQueue(submissionLocation));

		if ((p == null) || (p.size() == 0)) {
			return null;
		}

		return p.iterator().next();
	}

	public Collection<Application> getApplicationsThatProvideExecutable(
			String executable) {

		Collection<Application> apps = getGrid().getResources(
				Application.class, getGrid().getExecutable(executable));
		return apps;
	}

	public Collection<Directory> getDataLocationsForVO(String fqan) {
		Collection<Directory> directories = getGrid().getResources(
				Directory.class, getGrid().getGroup(fqan));
		return directories;
	}

	public Grid getGrid() {
		return ym.getGrid();
	}

	public Queue getGridResource(String submissionLocation) {
		return getGrid().getQueue(submissionLocation);
	}

	public String getJobmanagerOfQueueAtSite(String site, String queue) {
		throw new NotImplementedException();
	}

	public <T extends AbstractResource> T getResource(Class<T> type, String name) {

		String filterName = type.getSimpleName();

		Method m = null;
		try {
			m = Grid.class.getMethod("get" + filterName + "s");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			myLogger.debug("Can't get method to retrieve: " + filterName);
			throw new RuntimeException("Can't execute method to retrieve: "
					+ filterName);
		}

		try {
			Collection<T> result = (Collection<T>) m.invoke(getGrid());

			for (AbstractResource ar : result) {
				if (ar.toString().equals(name)) {
					return (T) ar;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			myLogger.debug("Can't execute method to retrieve: " + name);
			throw new RuntimeException("Can't execute method to retrieve: "
					+ name);
		}

		return null;
	}

	public <T extends AbstractResource> Collection<T> getResources(
			Class<T> resourceClass, AbstractResource... filters) {

		return getGrid().getResources(resourceClass, filters);
	}


	public Site getSiteForHostOrUrl(String host_or_url) {

		return getGrid().getSiteForUrl(host_or_url);
	}

	public Set<Directory> getStagingFileSystemForSubmissionLocation(
			String subLoc) {
		Queue queue = getGrid().getQueue(subLoc);

		return queue.getDirectories();
	}

	public Collection<Version> getVersionsOfApplicationOnSite(
			String application, String site) {

		Collection<Version> versions = getGrid().getResources(Version.class,
				getGrid().getApplication(application), getGrid().getSite(site));

		return versions;

	}

	public Collection<Version> getVersionsOfApplicationOnSubmissionLocation(
			String application, String submissionLocation) {

		Collection<Version> versions = getGrid().getResources(Version.class,
				getGrid().getApplication(application),
				getGrid().getQueue(submissionLocation));

		return versions;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grisu.jcommons.interfaces.InformationManager#refresh()
	 */
	public void refresh() {
		ym.refresh();
	}

}