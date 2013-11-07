package grisu.grin.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import grisu.jcommons.constants.Constants;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.model.info.*;
import grisu.jcommons.model.info.Package;
import grisu.jcommons.model.info.Queue;
import grisu.model.info.dto.DtoProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class Grid {

	public static final Logger myLogger = LoggerFactory.getLogger(Grid.class);

	private Set<FileSystem> filesystems = Collections
			.synchronizedSet(new HashSet<FileSystem>());
	private Set<Directory> directories = Collections
			.synchronizedSet(new HashSet<Directory>());
	private Set<Site> sites = Collections
			.synchronizedSet(new HashSet<Site>());
	private Set<Gateway> gateways = Collections
			.synchronizedSet(new HashSet<Gateway>());
	private Set<Queue> queues = Collections
			.synchronizedSet(new HashSet<Queue>());
	private Set<Application> applications = Collections
			.synchronizedSet(new HashSet<Application>());
	private Set<Package> packages = Collections
			.synchronizedSet(new HashSet<Package>());
	private Set<Group> groups = Collections
			.synchronizedSet(new HashSet<Group>());
	private Set<VO> vos = Collections.synchronizedSet(new HashSet<VO>());
	private Set<Version> versions = Collections
			.synchronizedSet(new HashSet<Version>());
	private Set<Executable> executables = Collections
			.synchronizedSet(new HashSet<Executable>());
	private Set<Middleware> middlewares = Collections
			.synchronizedSet(new HashSet<Middleware>());

	private void addApplication(Application a) {
		applications.add(a);
	}

	public void addDirectory(Directory d) {
		addFileSystem(d.getFilesystem());
		addGroups(d.getGroups());
		directories.add(d);
	}

	private void addExecutable(Executable e) {
		this.executables.add(e);
	}

	private void addExecutables(Collection<Executable> exes) {
		for ( Executable e : exes ) {
			addExecutable(e);
		}
	}

	private void addFileSystem(FileSystem fs) {
		addSite(fs.getSite());
		filesystems.add(fs);
	}

	private void addGateway(Gateway gw) {
		addSite(gw.getSite());
		addMiddleware(gw.getMiddleware());
		gateways.add(gw);
	}

	private void addGroup(Group g) {
		addVo(g.getVO());
		groups.add(g);
	}

	private void addGroups(Collection<Group> groups) {
		for (Group g : groups) {
			addGroup(g);
		}
	}

	private void addMiddleware(Middleware mw) {
		middlewares.add(mw);
	}

	private void addPackage(Package p) {
		addApplication(p.getApplication());
		addVersion(p.getVersion());
		addExecutables(p.getExecutables());
		packages.add(p);
	}

	public void addQueue(Queue q) {
		q.getPackages().add(Package.GENERIC);
		addGateway(q.getGateway());
		final Collection<Package> temp = Sets.newHashSet(q.getPackages());
		if (temp != null) {
			for (Package p : temp) {
				Application app = p.getApplication();
				Package any_version_package = new Package(app,
						Version.ANY_VERSION);
				q.getPackages().add(any_version_package);
				addPackage(any_version_package);

				addPackage(p);
			}
		}
		queues.add(q);
	}

	private void addSite(Site site) {
		sites.add(site);
	}

	private void addVersion(Version v) {
		versions.add(v);
	}

	private void addVo(VO vo) {
		vos.add(vo);
	}

	public List<JobQueueMatch> findMatches(
			final Map<JobSubmissionProperty, String> jobProps, String group) {

		ArrayList<JobQueueMatch> result = Lists.newArrayList();

		final Group g = getGroup(group);

		Collection<Queue> availQueues = getResources(Queue.class, g);
		// necessary because connections can include wrong groups
		Collection<grisu.jcommons.model.info.Queue> filtered = Collections2
				.filter(getQueues(),
						new Predicate<grisu.jcommons.model.info.Queue>() {

					@Override
					public boolean apply(
							grisu.jcommons.model.info.Queue input) {
						return input.getGroups().contains(g);
					}
				});

		for (Queue q : filtered) {

			JobQueueMatch match = new JobQueueMatch(
					q,
					DtoProperties
					.createPropertiesFromJobSubmissionProperties(jobProps));

			result.add(match);
		}

		return result;

	}


	public Collection<Queue> findQueues(
			Map<JobSubmissionProperty, String> jobProps, String group) {

		Group g = getGroup(group);

		Collection<Queue> q = Collections2.filter(getQueues(),
				Filters.acceptsJob(jobProps, getGroup(group)));

		return q;

	}

	public Application getApplication(String string) {

		if (StringUtils.isBlank(string)) {
			string = Constants.GENERIC_APPLICATION_NAME;
		}

		for (Application a : getApplications()) {
			if (a.getName().equalsIgnoreCase(string)) {
				return a;
			}
		}
		return null;

	}

	public Set<Application> getApplications() {
		return applications;
	}

	public Set<Directory> getDirectorys() {
		return directories;
	}

	public Executable getExecutable(String exe) {
		for (Executable e : getExecutables()) {
			if ( e.getExecutable().equals(exe)) {
				return e;
			}
		}
		return null;
	}

	public Set<Executable> getExecutables() {
		return executables;
	}

	public Set<FileSystem> getFilesystems() {
		return filesystems;
	}

	public Set<Gateway> getGateways() {
		return gateways;
	}

	public Group getGroup(String fqan) {

		if (StringUtils.isBlank(fqan)) {
			fqan = Constants.NON_VO_FQAN;
		}

		for (Group g : getGroups()) {
			if (g.getFqan().equals(fqan)) {
				return g;
			}
		}
		return null;
	}

	public Set<Group> getGroups() {
		return groups;
	}


	public Set<Package> getPackages() {
		return packages;
	}


	public Set<AbstractPhysicalResource> getPhysicalResources() {
		Set<AbstractPhysicalResource> result = Sets.newHashSet();
		result.addAll(getGateways());
		result.addAll(getDirectorys());
		result.addAll(getFilesystems());

		return result;
	}

	public Queue getQueue(String subLoc) {

		if (StringUtils.isBlank(subLoc)) {
			subLoc = Constants.NO_SUBMISSION_LOCATION_INDICATOR_STRING;
		}

		for (Queue v : getQueues()) {
			if (v.toString().equals(subLoc)) {
				return v;
			}
		}
		return null;
	}

	public Set<Queue> getQueues() {
		return queues;
	}

	public <T extends AbstractResource> Collection<T> getResources(
			Class<T> resourceClass, AbstractResource... filters) {

		String filterName = resourceClass.getSimpleName();

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
			Collection<T> result = (Collection<T>) m.invoke(this);

			if ((filters == null) || (filters.length == 0)) {
				return result;
			}

			Predicate<AbstractResource> p = Filters.filterResource(filters);

			Collection<T> filtered = Collections2.filter(result, p);

			return filtered;
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.debug("Can't execute method to retrieve: " + filterName);
			throw new RuntimeException("Can't execute method to retrieve: "
					+ filterName);
		}

	}

	public Site getSite(String string) {
		for (Site s : getSites()) {
			if (s.getName().equals(string)) {
				return s;
			}
		}
		return null;
	}

	public Site getSiteForUrl(String host_or_url) {

		for (AbstractPhysicalResource apr : getPhysicalResources()) {
			if (apr.isAccessibleVia(host_or_url)) {
				return apr.getSite();
			}
		}

		return null;

	}

	public Set<Site> getSites() {
		return sites;
	}

	public Version getVersion(String version) {

		if (StringUtils.isBlank(version)) {
			version = Constants.NO_VERSION_INDICATOR_STRING;
		}

		for (Version v : getVersions()) {
			if (v.getVersion().equalsIgnoreCase(version)) {
				return v;
			}
		}
		return null;

	}

	public Set<Version> getVersions() {
		return versions;
	}

	public Set<VO> getVos() {
		return vos;
	}

	public void validate() {

		// // adding special "any" queue
		// int maxCpus = Integer.MIN_VALUE;
		// long maxMem = Long.MIN_VALUE;
		// long maxVMem = Long.MIN_VALUE;
		// int maxWalltime = Integer.MIN_VALUE;
		//
		// Set<Group> g = Collections.synchronizedSet(new TreeSet<Group>());
		// Set<Directory> d = Collections
		// .synchronizedSet(new TreeSet<Directory>());
		// Set<Package> p = Collections.synchronizedSet(new TreeSet<Package>());
		//
		// for (Queue q : getQueues()) {
		// d.addAll(q.getDirectories());
		// p.addAll(q.getPackages());
		// g.addAll(q.getGroups());
		//
		// if (q.getNoCpus() > maxCpus) {
		// maxCpus = q.getNoCpus();
		// }
		// if (q.getMemory() > maxMem) {
		// maxMem = q.getMemory();
		// }
		// if (q.getVirtualMemory() > maxVMem) {
		// maxVMem = q.getVirtualMemory();
		// }
		// if (q.getWalltimeInMinutes() > maxWalltime) {
		// maxWalltime = q.getWalltimeInMinutes();
		// }
		// }
		//
		// Queue anyQueue = new Queue(Gateway.ANY_GATEWAY,
		// Constants.NO_SUBMISSION_LOCATION_INDICATOR_STRING, g, d, p,
		// maxCpus, maxMem, maxVMem, maxWalltime);
		//
		// addQueue(anyQueue);

		validateSet(getPackages());
		validateSet(getApplications());
		validateSet(getVersions());
		validateSet(getQueues());
		validateSet(getGateways());
		validateSet(getDirectorys());
		validateSet(getFilesystems());
		validateSet(getGroups());
		validateSet(getVos());
		validateSet(getSites());

		validateSet2ndGo(getPackages());
		validateSet2ndGo(getApplications());
		validateSet2ndGo(getVersions());
		validateSet2ndGo(getQueues());
		validateSet2ndGo(getGateways());
		validateSet2ndGo(getDirectorys());
        validateSet2ndGo(getFilesystems());
		validateSet2ndGo(getGroups());
		validateSet2ndGo(getVos());
		validateSet2ndGo(getSites());



		// now we make everything immutable
		filesystems = ImmutableSet.copyOf(filesystems);
		directories = ImmutableSet.copyOf(directories);
		sites = ImmutableSet.copyOf(sites);
		gateways = ImmutableSet.copyOf(gateways);
		queues = ImmutableSet.copyOf(queues);
		applications = ImmutableSet.copyOf(applications);
		packages = ImmutableSet.copyOf(packages);
		groups = ImmutableSet.copyOf(groups);
		vos = ImmutableSet.copyOf(vos);
		versions = ImmutableSet.copyOf(versions);
		executables = ImmutableSet.copyOf(executables);
		middlewares = ImmutableSet.copyOf(middlewares);

	}

	private <T extends AbstractResource> void validateSet(Set<T> set) {
		for (T res : set) {
			res.popluateConnections();
		}
	}

	private <T extends AbstractResource> void validateSet2ndGo(Set<T> set) {
		for (T res : set) {
			res.populateConnections2();
		}
	}

}
