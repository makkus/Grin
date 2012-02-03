

import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GridQueryTests {

	private static Grid grid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");
		// "/home/markus/Workspaces/Goji/grin/src/test/resources/test_2_sites.config.groovy");

		grid = ym.getGrid();

		// im = new GrinformationManager(grid);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testQuery() {

		// Map<JobSubmissionProperty, String> jobProps = Maps.newHashMap();
		//
		// jobProps.put(JobSubmissionProperty.NO_CPUS, "4");
		// jobProps.put(JobSubmissionProperty.APPLICATIONNAME, "java");
		// jobProps.put(JobSubmissionProperty.MEMORY_IN_B,
		// Long.toString(1073741825L));
		//
		// Collection<Queue> queues = grid.findQueues(jobProps,
		// grid.getGroup("/nz/nesi"));
		//
		// System.out.println(StringUtils.join(queues, "\n"));

	}

}
