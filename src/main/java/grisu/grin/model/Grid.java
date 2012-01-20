package grisu.grin.model;

import grisu.grin.model.resources.AbstractResource;
import grisu.grin.model.resources.Application;
import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.FileSystem;
import grisu.grin.model.resources.Gateway;
import grisu.grin.model.resources.Group;
import grisu.grin.model.resources.Package;
import grisu.grin.model.resources.Queue;
import grisu.grin.model.resources.Site;
import grisu.grin.model.resources.VO;
import grisu.grin.model.resources.Version;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class Grid {

	public static final Logger myLogger = LoggerFactory.getLogger(Grid.class);

	private final Set<FileSystem> filesystems = Sets.newHashSet();
	private final Set<Directory> directories = Sets.newHashSet();
	private final Set<Site> sites = Sets.newHashSet();
	private final Set<Gateway> gateways = Sets.newHashSet();
	private final Set<Queue> queues = Sets.newHashSet();
	private final Set<Application> applications = Sets.newHashSet();
	private final Set<Package> packages = Sets.newHashSet();
	private final Set<Group> groups = Sets.newHashSet();
	private final Set<VO> vos = Sets.newTreeSet();
	private final Set<Version> versions = Sets.newTreeSet();

	public void addApplication(Application a) {
		applications.add(a);
	}

	public void addDirectory(Directory d) {
		addFileSystem(d.getFilesystem());
		addGroups(d.getGroups());
		directories.add(d);
	}

	public void addFileSystem(FileSystem fs) {
		addSite(fs.getSite());
		filesystems.add(fs);
	}

	public void addGateway(Gateway gw) {
		addSite(gw.getSite());
		gateways.add(gw);
	}

	public void addGroup(Group g) {
		addVo(g.getVO());
		groups.add(g);
	}

	public void addGroups(Collection<Group> groups) {
		for (Group g : groups) {
			addGroup(g);
		}
	}

	public void addPackage(Package p) {
		addApplication(p.getApplication());
		addVersion(p.getVersion());
		addGroups(p.getGroups());
		packages.add(p);
	}

	public void addQueue(Queue q) {
		addGateway(q.getGateway());
		queues.add(q);
	}

	public void addSite(Site site) {
		sites.add(site);
	}

	public void addVersion(Version v) {
		versions.add(v);
	}

	public void addVo(VO vo) {
		vos.add(vo);
	}

	public Set<Application> getApplications() {
		return applications;
	}

	public Set<Directory> getDirectories() {
		return directories;
	}

	public Set<FileSystem> getFilesystems() {
		return filesystems;
	}

	public Set<Gateway> getGateways() {
		return gateways;
	}

	public Group getGroup(String fqan) {
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
			return null;
		}

		try {
			Collection<T> result = (Collection<T>) m.invoke(this);

			Predicate<AbstractResource> p = Filters.filterResource(filters);

			Collection<T> filtered = Collections2.filter(result, p);

			return filtered;
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.debug("Can't execute method to retrieve: " + filterName);
			return null;
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

	public Set<Site> getSites() {
		return sites;
	}

	public Set<Version> getVersions() {
		return versions;
	}

	public Set<VO> getVos() {
		return vos;
	}

	public void validate() {
		validateSet(getPackages());
		validateSet(getApplications());
		validateSet(getVersions());
		validateSet(getQueues());
		validateSet(getGateways());
		validateSet(getDirectories());
		validateSet(getFilesystems());
		validateSet(getGroups());
		validateSet(getVos());
		validateSet(getSites());

		validateSet2ndGo(getPackages());
		validateSet2ndGo(getApplications());
		validateSet2ndGo(getVersions());
		validateSet2ndGo(getQueues());
		validateSet2ndGo(getGateways());
		validateSet2ndGo(getDirectories());
		validateSet2ndGo(getFilesystems());
		validateSet2ndGo(getGroups());
		validateSet2ndGo(getVos());
		validateSet2ndGo(getSites());

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
