import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.grin.model.resources.Group;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GroupViewTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Grid grid;

	@Before
	public void setUp() throws Exception {
		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");

		grid = ym.getGrid();
		Group group = grid.getGroups().iterator().next();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

	}
}
