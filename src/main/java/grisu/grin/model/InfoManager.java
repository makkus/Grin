package grisu.grin.model;


import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.FileSystem;
import grisu.grin.model.resources.Group;
import grisu.grin.model.resources.Queue;
import grisu.grin.model.resources.Site;

import java.util.Collection;
import java.util.Map;

public interface InfoManager {

	public Map<String, String> getApplicationDetails(String application,
			String version, String subLoc);

	/**
	 * Returns a set of all {@link Directory}s for a given VO.
	 * 
	 * @param group
	 *            the group
	 * @return the set of directories
	 */
	public Collection<Directory> getDirectoriesForVO(Group group);

	/**
	 * Returns a list of hosts that are accessible with the specified VO.
	 * 
	 * @param group
	 *            the group
	 * @return the list of file hosts
	 */
	public Collection<FileSystem> getFileSystemsForVO(Group group);

	/**
	 * Gets the site where the host or url specified is located.
	 * 
	 * @param host_or_url
	 *            a hostname or url
	 * @return the site
	 */
	public Site getSiteForHostOrUrl(String host_or_url);

	/**
	 * Returns all Queues for a certain group.
	 * 
	 * @param group
	 *            the group
	 * @return the queues accessible for this group
	 */
	public Collection<Queue> getQueues(Group group);

}
