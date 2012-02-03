package grisu.jcommons.interfaces;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.fail;
import grisu.grin.YnfoManager;
import grisu.grin.model.Grid;
import grisu.grin.model.resources.Application;
import grisu.grin.model.resources.Site;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GrinformationManagerTest {

	private static Grid grid;
	private static GrinformationManager im;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");
		// "/home/markus/Workspaces/Goji/grin/src/test/resources/test_2_sites.config.groovy");

		grid = ym.getGrid();

		im = new GrinformationManager(grid);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	private Site site1;
	private Application app1;
	private Application app2;

	@Before
	public void setUp() throws Exception {

		site1 = grid.getSite("Site1");
		app1 = grid.getApplication("App1");
		app2 = grid.getApplication("App2");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllApplicationsAtSite() {

		Collection<Application> apps = grid.getResources(Application.class,
				site1);
		System.out.println(StringUtils.join(apps, "\n\t"));

		assertThat(apps, hasItem(app1));
		assertThat(apps, not(hasItem(app2)));
	}

	@Test
	public void testGetAllApplicationsOnGrid() {
		Collection<Application> apps = grid.getResources(Application.class);
		assertThat(apps, hasItems(app1, app2));
	}

	@Test
	public void testGetAllApplicationsOnGridForVO() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllGridResources() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllHosts() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSites() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSubmissionLocations() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSubmissionLocationsForApplication() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSubmissionLocationsForSite() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSubmissionLocationsForVO() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllSubmissionLocationsStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllVersionsOfApplicationOnGrid() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllVersionsOfApplicationOnGridForVO() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetApplicationDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetApplicationsThatProvideExecutable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDataLocationsForVO() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGridResource() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetJobmanagerOfQueueAtSite() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSiteForHostOrUrl() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStagingFileSystemForSubmissionLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVersionsOfApplicationOnSite() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVersionsOfApplicationOnSubmissionLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsVolatileDataLocation() {
		fail("Not yet implemented");
	}

}
