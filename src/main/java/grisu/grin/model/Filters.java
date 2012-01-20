package grisu.grin.model;

import grisu.grin.model.resources.AbstractResource;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

public class Filters {

	// private static class IsAccessibleForGroup implements
	// Predicate<GroupRestrictions> {
	// private final Group group;
	//
	// private IsAccessibleForGroup(final Group group) {
	// this.group = group;
	// }
	//
	// public boolean apply(final GroupRestrictions s) {
	// return s.getGroups().contains(group);
	// }
	// }

	private static class ResourceFilter implements Predicate<AbstractResource> {

		private final AbstractResource[] filters;

		public ResourceFilter(AbstractResource[] f) {
			this.filters = f;
		}

		public boolean apply(AbstractResource resource) {

			Collection<AbstractResource> r = resource.getConnections();

			if (r.contains(resource)) {
				return true;
			}

			return false;
		}

	}

	private static Logger myLogger = LoggerFactory.getLogger(Filters.class);

	public static Predicate<AbstractResource> filterResource(
			AbstractResource[] o) {
		return new ResourceFilter(o);
	}
}
