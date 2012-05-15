package grisu.jcommons.interfaces;

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.model.info.AbstractResource;
import grisu.model.info.dto.Application;
import grisu.model.info.dto.Directory;
import grisu.model.info.dto.JobQueueMatch;
import grisu.model.info.dto.Package;
import grisu.model.info.dto.Queue;
import grisu.model.info.dto.Site;
import grisu.model.info.dto.VO;
import grisu.model.info.dto.Version;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class GrinformationManager implements InformationManager {

	static final Logger myLogger = LoggerFactory
			.getLogger(GrinformationManager.class.getName());

	public static void main (String[] args) {

		GrinformationManager gm = new GrinformationManager("testbed");

		for (VO vo : gm.getAllVOs()) {
			System.out.println("VO: " + vo);
		}


		System.exit(0);
	}

	//	private final MapperFactory factory;

	private final YnfoManager ym;

	private MapperFacade mapperFacade = null;

	private final String path;

	public GrinformationManager(Map<String, String> config) {
		this(config.get("path"));
	}

	// public GrinformationManager(Grid grid) {
	// this.grid = grid;
	// this.path = null;
	// }

	public GrinformationManager(String path) {
		this.path = path;

		//		factory = new DefaultMapperFactory.Builder().build();

		// // configure mapper
		// factory.registerClassMap(ClassMapBuilder
		// .map(grisu.jcommons.model.info.Queue.class, Queue.class)
		// .byDefault().toClassMap());
		// factory.registerClassMap(ClassMapBuilder
		// .map(grisu.jcommons.model.info.Application.class,
		// Application.class).byDefault().toClassMap());
		//
		// factory.build();
		//		mapperFacade = factory.getMapperFacade();

		ym = new YnfoManager(path);
		myLogger.debug("Grinformationmanager created.");
	}

	public List<JobQueueMatch> findMatches(
			Map<JobSubmissionProperty, String> job, String fqan) {

		Collection<grisu.jcommons.model.info.JobQueueMatch> queues = getGrid()
				.findMatches(job, fqan);
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(queues, JobQueueMatch.class);
	}

	public List<Queue> findQueues(Map<JobSubmissionProperty, String> job,
			String fqan) {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.findQueues(job, fqan);
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}
		return getMapperFacade().mapAsList(queues, Queue.class);
	}

	public List<Application> getAllApplicationsAtSite(String site) {
		grisu.jcommons.model.info.Site s = getGrid().getSite(site);

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class, s);

		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}
		List<Application> apps = getMapperFacade().mapAsList(result,
				Application.class);
		return apps;
	}


	public List<Application> getAllApplicationsOnGrid() {

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getApplications();
		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(result, Application.class);

	}

	public List<Application> getAllApplicationsOnGridForVO(String fqan) {
		grisu.jcommons.model.info.Group g = getGrid().getGroup(fqan);

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class, g);
		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(result, Application.class);
	}


	public List<Queue> getAllQueues() {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getQueues();
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(queues, Queue.class);

	}

	public Collection<Queue> getAllQueues(String application,
			String version) {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getResources(grisu.jcommons.model.info.Queue.class,
						getGrid().getApplication(application),
						getGrid().getVersion(version));
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(queues, Queue.class);
	}

	public List<Queue> getAllQueuesForApplication(
			String application) {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getResources(grisu.jcommons.model.info.Queue.class,
						getGrid().getApplication(application));
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(queues, Queue.class);
	}

	public List<Queue> getAllQueuesForVO(String fqan) {

		final grisu.jcommons.model.info.Group group = getGrid().getGroup(fqan);
		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getResources(grisu.jcommons.model.info.Queue.class, group);

		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		Collection<grisu.jcommons.model.info.Queue> filtered = Collections2
				.filter(queues,
						new Predicate<grisu.jcommons.model.info.Queue>() {

					public boolean apply(grisu.jcommons.model.info.Queue input) {
						return input.getGroups().contains(group);
					}
				});

		if (CollectionUtils.isEmpty(filtered)) {
			return Lists.newArrayList();
		}
		// filter queues again, because recursive connections can include groups
		// that belong to Directories
		return getMapperFacade().mapAsList(filtered, Queue.class);

	}

	public List<Site> getAllSites() {
		Collection<grisu.jcommons.model.info.Site> sites = getGrid().getSites();
		if (CollectionUtils.isEmpty(sites)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(sites, Site.class);
	}

	public List<Version> getAllVersionsOfApplicationOnGrid(
			String application) {
		Collection<grisu.jcommons.model.info.Version> versions = getGrid()
				.getResources(grisu.jcommons.model.info.Version.class,
						getGrid().getApplication(application));
		if (CollectionUtils.isEmpty(versions)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(versions, Version.class);
	}


	public Set<VO> getAllVOs() {
		Set<grisu.jcommons.model.info.VO> vos = getGrid().getVos();
		if (CollectionUtils.isEmpty(vos)) {
			return Sets.newHashSet();
		}

		return getMapperFacade().mapAsSet(vos, VO.class);
	}


	public List<Application> getApplicationsThatProvideExecutable(
			String executable) {

		Collection<grisu.jcommons.model.info.Application> apps = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class,
						getGrid().getExecutable(executable));
		if (CollectionUtils.isEmpty(apps)) {
			return Lists.newArrayList();
		}
		return getMapperFacade().mapAsList(apps, Application.class);
	}

	public List<Directory> getDirectoriesForVO(String fqan) {
		Collection<grisu.jcommons.model.info.Directory> directories = getGrid()
				.getResources(grisu.jcommons.model.info.Directory.class,
						getGrid().getGroup(fqan));
		if (CollectionUtils.isEmpty(directories)) {
			return Lists.newArrayList();
		}
		return getMapperFacade().mapAsList(directories, Directory.class);
	}

	public Grid getGrid() {
		return ym.getGrid();
	}


	public String getJobmanagerOfQueueAtSite(String site, String queue) {
		throw new NotImplementedException();
	}

	private synchronized MapperFacade getMapperFacade() {
		if (mapperFacade == null) {
			MapperFactory f = new DefaultMapperFactory.Builder().build();
			mapperFacade = f.getMapperFacade();
		}
		return mapperFacade;
	}

	public Package getPackage(String application,
			String version, String submissionLocation) {

		Collection<grisu.jcommons.model.info.Package> p = getGrid()
				.getResources(grisu.jcommons.model.info.Package.class,
						getGrid().getApplication(application),
						getGrid().getVersion(version),
						getGrid().getQueue(submissionLocation));

		if ((p == null) || (p.size() == 0)) {
			return null;
		}

		return getMapperFacade().map(p.iterator().next(), Package.class);
	}

	public Queue getQueue(String submissionLocation) {
		return getMapperFacade().map(getGrid().getQueue(submissionLocation),
				Queue.class);
	}

	public <T> T getResource(Class<T> type, String name) {

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
			Collection<AbstractResource> result = (Collection<AbstractResource>) m
					.invoke(getGrid());

			for (AbstractResource ar : result) {
				if (ar.toString().equals(name)) {
					return getMapperFacade().map(ar, type);
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

		return getMapperFacade().map(getGrid().getSiteForUrl(host_or_url),
				Site.class);
	}

	public Set<Directory> getStagingFileSystemForSubmissionLocation(
			String subLoc) {
		grisu.jcommons.model.info.Queue queue = getGrid().getQueue(subLoc);
		if ((queue == null) || CollectionUtils.isEmpty(queue.getDirectories())) {
			return Sets.newLinkedHashSet();
		}

		return getMapperFacade().mapAsSet(queue.getDirectories(), Directory.class);
	}

	public List<Version> getVersionsOfApplicationOnSite(
			String application, String site) {

		Collection<grisu.jcommons.model.info.Version> versions = getGrid()
				.getResources(grisu.jcommons.model.info.Version.class,
						getGrid().getApplication(application), getGrid().getSite(site));

		versions.remove(Version.ANY_VERSION);

		if (CollectionUtils.isEmpty(versions)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(versions, Version.class);

	}

	public List<Version> getVersionsOfApplicationOnSubmissionLocation(
			String application, String submissionLocation) {

		Collection<grisu.jcommons.model.info.Version> versions = getGrid()
				.getResources(grisu.jcommons.model.info.Version.class,
						getGrid().getApplication(application),
						getGrid().getQueue(submissionLocation));

		versions.remove(Version.ANY_VERSION);

		if (CollectionUtils.isEmpty(versions)) {
			return Lists.newArrayList();
		}

		return getMapperFacade().mapAsList(versions, Version.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grisu.jcommons.interfaces.InformationManager#refresh()
	 */
	public void refresh() {
		ym.refreshAndWait();
	}

}