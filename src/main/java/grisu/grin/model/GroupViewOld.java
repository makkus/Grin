package grisu.grin.model;

import grisu.grin.exceptions.KeyException;
import grisu.grin.model.resources.Application;
import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.Gateway;
import grisu.grin.model.resources.Group;
import grisu.grin.model.resources.Package;
import grisu.grin.model.resources.Queue;
import grisu.grin.model.resources.Site;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;

public class GroupViewOld {

	private final LoadingCache<Filter, Set<Package>> packageCache = CacheBuilder
			.newBuilder().build(new CacheLoader<Filter, Set<Package>>() {
				@Override
				public Set<Package> load(Filter key) throws KeyException {
					System.out.println("HIT CACHE: " + key.toString());
					return getPackagesUncached(key);
				}
			});

	private final Grid grid;
	private final Group group;

	public static final Logger myLogger = LoggerFactory
			.getLogger(GroupViewOld.class);

	public GroupViewOld(Grid grid, Group group) {
		this.grid = grid;
		this.group = group;
	}

	public Set<Package> getAllPackages() {

		Set<Package> result = Sets.newTreeSet();
		for (Package p : grid.getPackages()) {
			if (p.getGroups().contains(group)) {
				result.add(p);
			}
		}
		return result;
	}

	private Set<Queue> getAllQueues() {
		Set<Queue> result = Sets.newTreeSet();
		for (Queue q : grid.getQueues()) {
			if (q.getGroups().contains(group)) {
				result.add(q);
			}
		}
		return result;
	}

	public Set<Application> getApplications() {
		Set<Application> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {
			temp.add(p.getApplication());
		}
		return temp;
	}

	public Set<Directory> getDirectories() {
		Set<Directory> temp = Sets.newTreeSet();
		for (Directory d : grid.getDirectories()) {
			if (d.getGroups().contains(getGroup())) {
				temp.add(d);
			}
		}
		return temp;
	}

	public Group getGroup() {
		return group;
	}

	public Set<Package> getPackages() {
		try {
			return getPackages(Filter.defaultKey);
		} catch (KeyException e) {
			myLogger.error("Error retrieving default values.", e);
			return null;
		}
	}

	private Set<Package> getPackages(Application app) {

		Set<Package> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {
			if (p.getApplication().equals(app)) {
				temp.add(p);
			}
		}
		return temp;
	}

	private Set<Package> getPackages(Directory d) {
		Set<Package> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {

		}
		return temp;
	}

	public Set<Package> getPackages(Filter key) throws KeyException {
		try {
			if (key == null) {
				key = Filter.defaultKey;
			}
			return packageCache.get(key);
		} catch (ExecutionException e) {
			throw (KeyException) e.getCause();
		}
	}

	private Set<Package> getPackages(Gateway gw) {
		Set<Package> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {
			if (p.getGateways().contains(gw)) {
				temp.add(p);
			}
		}
		return temp;
	}

	private Set<Package> getPackages(Site site) {
		Set<Package> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {
			if (p.getSites().contains(site)) {
				temp.add(p);
			}
		}
		return temp;
	}

	private Set<Package> getPackages(String version) {

		Set<Package> temp = Sets.newTreeSet();
		for (Package p : getPackages()) {
			if (p.getVersion().equals(version)) {
				temp.add(p);
			}
		}
		return temp;
	}

	private Set<Package> getPackagesUncached(Filter key) throws KeyException {

		if (key == null || key.equals(key.defaultKey)) {
			return getAllPackages();
		}

		for (Object o : key.getKeys()) {
			try {
				Method m = this.getClass().getMethod("getPackages",
						o.getClass());
				Set<Package> p = (Set<Package>) m.invoke(this, o);
				return p;
			} catch (Exception e) {
				throw new KeyException("Can't lookup packages using "
						+ o.toString(), e);
			}
		}

		throw new KeyException("No valid key provided.");
	}

}
