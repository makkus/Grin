package grisu.jcommons.interfaces;

import grisu.jcommons.model.info.Application;
import grisu.jcommons.model.info.Directory;
import grisu.jcommons.model.info.Package;
import grisu.jcommons.model.info.Queue;
import grisu.jcommons.model.info.Site;
import grisu.jcommons.model.info.Version;

import java.util.Collection;
import java.util.Set;

/**
 * An information manager provides Grisu with information about the grid and
 * it's resources. This will normally happen via mds. But I could imagine
 * writing an InformationManager that reads it's data from config files or such.
 * 
 * @author markus
 * 
 */
public interface InformationManager {

	// /**
	// * Returns all the queues that are able to execute a job with the
	// specified
	// * job properties.
	// *
	// * @param jobProps
	// * the job properties
	// * @param group
	// * the group for which the job is going to be submitted
	// * @return the queues.
	// */
	// public Collection<Queue> findQueues(
	// Map<JobSubmissionProperty, String> jobProps, String group);

	/**
	 * Returns the names of all the applications at the site.
	 * 
	 * @param site
	 *            name of the site
	 * @return names of the applications at the site
	 */
	String[] getAllApplicationsAtSite(String site);

	/**
	 * Returns all the available applications on the Grid.
	 * 
	 * @return all the applications on the Grid
	 */
	String[] getAllApplicationsOnGrid();




	/**
	 * Calculates all the applications that are available for the specified VO
	 * grid-wide.
	 * 
	 * @param fqan
	 *            the vo
	 * @return all available applications
	 */
	String[] getAllApplicationsOnGridForVO(String fqan);

	/**
	 * Return all the names of all the sites from MDS.
	 * 
	 * @return names of all the sites from MDS
	 */
	String[] getAllSites();

	/**
	 * Returns all submission locations on all the sites from MDS.
	 * 
	 * @return all the available submissionlocations on the Grid
	 */
	String[] getAllSubmissionLocations();

	/**
	 * Returns all the submission locations of all the sites that have the given
	 * application installed on their resources.
	 * 
	 * @param application
	 *            name of the software package
	 * @param version
	 *            version number of the software package
	 * @return submission locations of sites which have the software application
	 *         installed
	 */
	Collection<String> getAllSubmissionLocations(String application,
			String version);


	/**
	 * Returns all the submissionlocations for the given application. The entry
	 * for the submissionlocations are in this format:
	 * 
	 * <queue-name>:<grid-submission-host>[#<job-manager>]
	 * 
	 * example: hydra@hydra:ng2.sapac.edu.au#PBS sque:ng2.vpac.org
	 * 
	 * If jobmanager is not specified, it is assumed that the submission queue
	 * is of type 'PBS'
	 * 
	 * @param application
	 *            name of the application
	 * @return all the submission queues for the given application.
	 */
	Collection<String> getAllSubmissionLocationsForApplication(
			String application);

	/**
	 * Returns all submission locations or a specific VO.
	 * 
	 * @param fqan
	 *            the vo
	 * @return the submission locations
	 */
	Collection<Queue> getAllSubmissionLocationsForVO(String fqan);

	/**
	 * Returns the list of available versions of the software application on the
	 * Grid.
	 * 
	 * @param application
	 *            name of the software package
	 * @return a string array of versions of the software application on the
	 *         Grid
	 */
	Collection<String> getAllVersionsOfApplicationOnGrid(String application);

	/**
	 * Returns a map of the attribute details of the given application at the
	 * site. This method will give the client the values of the following
	 * attributes: - Module - SerialAvail - ParallelAvail - Executables (comma
	 * separated list) *
	 * 
	 * @param application
	 *            name of the software package
	 * @param version
	 *            version of the software package
	 * @param submissionLocation
	 *            the submissionlocation
	 * @return a map of the attribute details of the given application at the
	 *         site.
	 */
	Package getApplicationDetails(String application,
			String version, String submissionLocation);

	/**
	 * Returns an array of Strings with codes that provide the specified
	 * executable.
	 * 
	 * @param executable
	 *            the executable
	 * @return the codes
	 */
	Collection<Application> getApplicationsThatProvideExecutable(
			String executable);

	/**
	 * Returns all the data locations (mount points) on the grid available for
	 * the given VO.
	 * 
	 * @param fqan
	 *            fully qualified attribute name of the VO
	 * @return all the data locations for the VO
	 */
	Collection<Directory> getDataLocationsForVO(String fqan);

	/**
	 * Returns the GridResource object for the submissionLocation string.
	 * 
	 * The GridResource object only has dummy values for dynamic stuff like
	 * freejobslots (for now).
	 * 
	 * @param submissionLocation
	 *            the submission location string
	 * @return the grid resource
	 */
	public Queue getGridResource(String submissionLocation);


	/**
	 * Returns the jobmanager that submits to the specified queue/site.
	 * 
	 * @param site
	 *            the site
	 * @param queue
	 *            the queue
	 * @return the jobmanager
	 */
	String getJobmanagerOfQueueAtSite(String site, String queue);

	/**
	 * Returns the name of the site where this host or URL belongs.
	 * 
	 * @param host_or_url
	 *            the host or the service's URI
	 * @return the name of the site where this host or URL belongs
	 */
	Site getSiteForHostOrUrl(String host_or_url);

	Set<Directory> getStagingFileSystemForSubmissionLocation(String subLoc);

	/**
	 * Returns the list of available versions of the software application at a
	 * given site.
	 * 
	 * @param application
	 *            name of the software application
	 * @param site
	 *            name of the site
	 * @return an array of string representing the available versions of the
	 *         software application at the site
	 */
	Collection<Version> getVersionsOfApplicationOnSite(String application,
			String site);

	/**
	 * Returns the list of available versions of the software application at a
	 * given submissionlocation.
	 * 
	 * @param application
	 *            name of the software application
	 * @param submissionLocation
	 *            name of the submissionlocation
	 * @return an array of string representing the available versions of the
	 *         software application at the submissionlocation
	 */
	Collection<Version> getVersionsOfApplicationOnSubmissionLocation(
			String application,
			String submissionLocation);


}