package grisu.grin.model;


import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.FileSystem;
import grisu.grin.model.resources.Group;

import java.util.Set;

public interface InfoManager {

	/**
	 * Returns a set of all {@link Directory}s for a given VO.
	 * 
	 * @param vo
	 *            the vo
	 * @return the set of directories
	 */
	public Set<Directory> getDirectoriesForVO(Group vo);

	/**
	 * Returns a list of hosts that are accessible with the specified VO.
	 * 
	 * @param vo
	 *            the VO
	 * @return the list of file hosts
	 */
	public Set<FileSystem> getFileSystemsForVO(Group fqan);

}
