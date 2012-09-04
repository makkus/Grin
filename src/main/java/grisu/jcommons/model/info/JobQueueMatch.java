package grisu.jcommons.model.info;

import grisu.jcommons.constants.Constants;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.utils.MemoryUtils;
import grisu.jcommons.utils.WalltimeUtils;
import grisu.model.info.dto.DtoProperties;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * A model to hold information about whether and why and how well a job will run
 * on a queue.
 * 
 * @author markus
 * 
 */
public class JobQueueMatch implements Comparable<JobQueueMatch> {

	public static JobQueueMatch getMatch(Collection<JobQueueMatch> matches,
			String submissionLocation) {

		if (matches == null) {
			return null;
		}

		for (JobQueueMatch m : matches) {
			if (m.getQueue().toString().equals(submissionLocation)) {
				return m;
			}
		}
		return null;
	}

	private Queue queue;
	private DtoProperties job;

	private boolean valid = true;
	private int rank = 100;

	private boolean checked = false;

	private DtoProperties propertiesDetails = new DtoProperties();

	public JobQueueMatch() {
	}

	public JobQueueMatch(Queue queue, DtoProperties job) {
		this.queue = queue;
		this.job = job;
		Thread t = new Thread() {
			@Override
			public void run() {
				checkAcceptsJob();
			}
		};
		t.setName("checking job for queue " + queue.toString());
		t.start();
	}

	private void addPropertyDetail(JobSubmissionProperty property, String detail) {
		propertiesDetails.addProperty(property.toString(), detail);
	}

	/**
	 * Same as {@link #acceptsJob(Map)}, but a bit slower since it also
	 * populates a {@link JobQueueMatch} object with reasons why this job
	 * possibly can't run on a queue (if applicable).
	 * 
	 * @param jobProperties
	 *            the properties
	 * @param match
	 *            the matcher object
	 * @return whether this queue accepts a job with the specified specs
	 */
	private synchronized boolean checkAcceptsJob() {

		if (!checked) {

			Map<JobSubmissionProperty, String> jobProperties = job
					.asJobPropertiesMap();

			for (JobSubmissionProperty p : jobProperties.keySet()) {
				switch (p) {
				case WALLTIME_IN_MINUTES:
					int w = Integer.parseInt(jobProperties.get(p));
					if (w > queue.getWalltimeInMinutes()) {
						valid = false;
						addPropertyDetail(
								JobSubmissionProperty.WALLTIME_IN_MINUTES,
								"Walltime too long (max: "
										+ WalltimeUtils.convertSeconds(queue
												.getWalltimeInMinutes() * 60)
												+ ")");
					}
					break;
				case NO_CPUS:
					int nocpus = Integer.parseInt(jobProperties.get(p));
					if (nocpus > queue.getCpus()) {
						valid = false;
						addPropertyDetail(
								JobSubmissionProperty.NO_CPUS,
								"Job specifies too many cpus, only "
										+ queue.getCpus()
										+ " cpus are available.");
					}
					break;
				case MEMORY_IN_B:
					long m = Long.parseLong(jobProperties.get(p));
					if (m > queue.getMemory()) {
						valid = false;
						addPropertyDetail(
								JobSubmissionProperty.MEMORY_IN_B,
								"Job specifies too much memory, only "
										+ MemoryUtils.humanReadableByteCount(
												queue.getMemory(), true)
												+ " available.");
					}
					break;
				case VIRTUAL_MEMORY_IN_B:
					long vm = Long.parseLong(jobProperties.get(p));
					if (vm > queue.getVirtualMemory()) {
						valid = false;
						addPropertyDetail(
								JobSubmissionProperty.VIRTUAL_MEMORY_IN_B,
								"Job specifies too much memory, only "
										+ MemoryUtils.humanReadableByteCount(
												queue.getVirtualMemory(), true)
												+ " available.");
					}
					break;
				case APPLICATIONNAME:
					String name = jobProperties.get(p);
					String version = jobProperties
							.get(JobSubmissionProperty.APPLICATIONVERSION);
					boolean appAvail = queue.providesPackage(name, version);
					if (!appAvail) {
						valid = false;
						String packageString = name;
						if (Constants.NO_VERSION_INDICATOR_STRING
								.equals(version)) {
							packageString = packageString + " (version: "
									+ version + ")";
						}
						addPropertyDetail(
								JobSubmissionProperty.APPLICATIONNAME,
								"Package " + packageString
								+ " not available on this queue.");
					}
					break;
				case HOSTCOUNT:
					int hostcount = Integer.parseInt(jobProperties.get(p));
					if (hostcount > queue.getHosts()) {
						valid = false;
						addPropertyDetail(
								JobSubmissionProperty.HOSTCOUNT,
								"Too many hosts specified, only "
										+ queue.getHosts() + " available.");
					}
					break;
				}

			}

			checked = true;
		}

		return valid;
	}

	@Override
	public int compareTo(JobQueueMatch o) {
		return ComparisonChain.start().compare(getRank(), o.getRank())
				.compare(getQueue(), getQueue()).result();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final JobQueueMatch other = (JobQueueMatch) obj;

		return Objects.equal(getQueue(), other.getQueue())
				&& Objects.equal(this.getRank(), other.getRank());
	}

	public DtoProperties getJob() {
		return job;
	}

	public DtoProperties getPropertiesDetails() {
		checkAcceptsJob();
		return propertiesDetails;
	}

	public Queue getQueue() {
		return queue;
	}

	public int getRank() {
		checkAcceptsJob();
		return rank;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getQueue(), getRank());
	}

	public boolean isValid() {
		checkAcceptsJob();
		return valid;
	}

	public void setJob(DtoProperties job) {
		this.job = job;
	}

	public void setPropertiesDetails(DtoProperties propertiesDetails) {
		this.propertiesDetails = propertiesDetails;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return getQueue().toString();
	}

}
