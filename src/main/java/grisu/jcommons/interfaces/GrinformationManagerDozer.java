package grisu.jcommons.interfaces;

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.model.info.AbstractResource;
import grisu.model.info.dto.Application;
import grisu.model.info.dto.Directory;
import grisu.model.info.dto.FileSystem;
import grisu.model.info.dto.JobQueueMatch;
import grisu.model.info.dto.Package;
import grisu.model.info.dto.Queue;
import grisu.model.info.dto.Site;
import grisu.model.info.dto.VO;
import grisu.model.info.dto.Version;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class GrinformationManagerDozer implements InformationManager {

	public class DozerTransformer<S, T> implements Function<S, T> {

		public T apply(S input) {
			try {
				return (T) getMapper().map(
						input,
						Class.forName("grisu.model.info.dto."
								+ input.getClass().getSimpleName()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	static final Logger myLogger = LoggerFactory
			.getLogger(GrinformationManagerDozer.class.getName());

	private static DozerBeanMapper mapper;

	public static synchronized Mapper getMapper() {
		if ( mapper == null ) {
			List myMappingFiles = new ArrayList();
			myMappingFiles.add("dozerMapping.xml");
			mapper = new DozerBeanMapper();
			mapper.setMappingFiles(myMappingFiles);
		}
		return mapper;
	}

	//	private final MapperFactory factory;

	public static void main (String[] args) {

		GrinformationManagerDozer gm = new GrinformationManagerDozer("/data/src/config/nesi-grid-info/nesi_info.groovy");

		for (Directory d : gm.getDirectoriesForVO("/nz/nesi") ) {
			
			System.out.println(d.toUrl()+": "+d.getOptions().size());

		}


		System.exit(0);
	}

	private final YnfoManager ym;

	private final String path;

	// public GrinformationManager(Grid grid) {
	// this.grid = grid;
	// this.path = null;
	// }

	public GrinformationManagerDozer(Map<String, String> config) {
		this(config.get("path"));
	}

	public GrinformationManagerDozer(String path) {
		this.path = path;

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

		return Lists
				.newArrayList(Collections2
						.transform(
								queues,
								new DozerTransformer<grisu.jcommons.model.info.JobQueueMatch, JobQueueMatch>()));

	}

	public List<Queue> findQueues(Map<JobSubmissionProperty, String> job,
			String fqan) {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.findQueues(job, fqan);
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return Lists
				.newArrayList(Collections2
						.transform(
								queues,
								new DozerTransformer<grisu.jcommons.model.info.Queue, Queue>()));

	}

	public List<Application> getAllApplicationsAtSite(String site) {
		grisu.jcommons.model.info.Site s = getGrid().getSite(site);

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class, s);

		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}

		List<Application> apps = Lists
				.newArrayList(Collections2
						.transform(
								result,
								new DozerTransformer<grisu.jcommons.model.info.Application, Application>()));

		return apps;
	}


	public List<Application> getAllApplicationsOnGrid() {

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getApplications();
		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}

		List<Application> apps = Lists
				.newArrayList(Collections2
						.transform(
								result,
								new DozerTransformer<grisu.jcommons.model.info.Application, Application>()));

		return apps;
	}

	public List<Application> getAllApplicationsOnGridForVO(String fqan) {
		grisu.jcommons.model.info.Group g = getGrid().getGroup(fqan);

		Collection<grisu.jcommons.model.info.Application> result = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class, g);
		if (CollectionUtils.isEmpty(result)) {
			return Lists.newArrayList();
		}

		List<Application> apps = Lists
				.newArrayList(Collections2
						.transform(
								result,
								new DozerTransformer<grisu.jcommons.model.info.Application, Application>()));

		return apps;
	}


	public List<Queue> getAllQueues() {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getQueues();
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}

		return Lists
				.newArrayList(Collections2
						.transform(
								queues,
								new DozerTransformer<grisu.jcommons.model.info.Queue, Queue>()));

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

		return Lists
				.newArrayList(Collections2
						.transform(
								queues,
								new DozerTransformer<grisu.jcommons.model.info.Queue, Queue>()));
	}

	public List<Queue> getAllQueuesForApplication(
			String application) {

		Collection<grisu.jcommons.model.info.Queue> queues = getGrid()
				.getResources(grisu.jcommons.model.info.Queue.class,
						getGrid().getApplication(application));
		if (CollectionUtils.isEmpty(queues)) {
			return Lists.newArrayList();
		}
		return Lists
				.newArrayList(Collections2
						.transform(
								queues,
								new DozerTransformer<grisu.jcommons.model.info.Queue, Queue>()));
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

		return Lists
				.newArrayList(Collections2
						.transform(
								filtered,
								new DozerTransformer<grisu.jcommons.model.info.Queue, Queue>()));

	}

	public List<Site> getAllSites() {
		Collection<grisu.jcommons.model.info.Site> sites = getGrid().getSites();
		if (CollectionUtils.isEmpty(sites)) {
			return Lists.newArrayList();
		}

		return Lists.newArrayList(Collections2.transform(sites,
				new DozerTransformer<grisu.jcommons.model.info.Site, Site>()));
	}

	public List<Version> getAllVersionsOfApplicationOnGrid(
			String application) {
		Collection<grisu.jcommons.model.info.Version> versions = getGrid()
				.getResources(grisu.jcommons.model.info.Version.class,
						getGrid().getApplication(application));
		if (CollectionUtils.isEmpty(versions)) {
			return Lists.newArrayList();
		}

		return Lists
				.newArrayList(Collections2
						.transform(
								versions,
								new DozerTransformer<grisu.jcommons.model.info.Version, Version>()));
	}


	public Set<VO> getAllVOs() {
		Set<grisu.jcommons.model.info.VO> vos = getGrid().getVos();
		if (CollectionUtils.isEmpty(vos)) {
			return Sets.newHashSet();
		}

		return Sets.newTreeSet(Collections2.transform(vos,
				new DozerTransformer<grisu.jcommons.model.info.VO, VO>()));
	}


	public List<Application> getApplicationsThatProvideExecutable(
			String executable) {

		Collection<grisu.jcommons.model.info.Application> apps = getGrid()
				.getResources(grisu.jcommons.model.info.Application.class,
						getGrid().getExecutable(executable));
		if (CollectionUtils.isEmpty(apps)) {
			return Lists.newArrayList();
		}
		return Lists
				.newArrayList(Collections2
						.transform(
								apps,
								new DozerTransformer<grisu.jcommons.model.info.Application, Application>()));
	}

	public List<Directory> getDirectoriesForVO(String fqan) {
		Collection<grisu.jcommons.model.info.Directory> directories = getGrid()
				.getResources(grisu.jcommons.model.info.Directory.class,
						getGrid().getGroup(fqan));
		if (CollectionUtils.isEmpty(directories)) {
			return Lists.newArrayList();
		}
		return Lists
				.newArrayList(Collections2
						.transform(
								directories,
								new DozerTransformer<grisu.jcommons.model.info.Directory, Directory>()));
	}

	public Grid getGrid() {
		return ym.getGrid();
	}



	public String getJobmanagerOfQueueAtSite(String site, String queue) {
		throw new NotImplementedException();
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

		return getMapper().map(p.iterator().next(), Package.class);
	}

	public Queue getQueue(String submissionLocation) {
		return getMapper().map(getGrid().getQueue(submissionLocation),
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
					return getMapper().map(ar, type);
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

		return getMapper().map(getGrid().getSiteForUrl(host_or_url),
				Site.class);
	}

	public Set<Directory> getStagingFileSystemForSubmissionLocation(
			String subLoc) {
		grisu.jcommons.model.info.Queue queue = getGrid().getQueue(subLoc);
		if ((queue == null) || CollectionUtils.isEmpty(queue.getDirectories())) {
			return Sets.newLinkedHashSet();
		}

		return Sets
				.newHashSet(Collections2
						.transform(
								queue.getDirectories(),
								new DozerTransformer<grisu.jcommons.model.info.Directory, Directory>()));
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

		return Lists
				.newArrayList(Collections2
						.transform(
								versions,
								new DozerTransformer<grisu.jcommons.model.info.Version, Version>()));

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

		return Lists
				.newArrayList(Collections2
						.transform(
								versions,
								new DozerTransformer<grisu.jcommons.model.info.Version, Version>()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grisu.jcommons.interfaces.InformationManager#refresh()
	 */
	public void refresh() {
		ym.refreshAndWait();
	}

	@Override
	public List<Directory> getDirectories() {
		
		Collection<grisu.jcommons.model.info.Directory> directories = getGrid().getDirectorys();

		if (CollectionUtils.isEmpty(directories)) {
			return Lists.newArrayList();
		}
		return Lists
				.newArrayList(Collections2
						.transform(
								directories,
								new DozerTransformer<grisu.jcommons.model.info.Directory, Directory>()));
	}

	@Override
	public List<FileSystem> getFileSystems() {
		Collection<grisu.jcommons.model.info.FileSystem> filesystems = getGrid().getFilesystems();

		if (CollectionUtils.isEmpty(filesystems)) {
			return Lists.newArrayList();
		}
		return Lists
				.newArrayList(Collections2
						.transform(
								filesystems,
								new DozerTransformer<grisu.jcommons.model.info.FileSystem, FileSystem>()));

	}

}