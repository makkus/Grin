package grisu.jcommons.interfaces;

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

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		YnfoManager ym = new YnfoManager(
				"/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");

		grid = ym.getGrid();

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
	public void testGetAllApplicationsAtSite() {

		Site site = grid.getSite("Auckland");
		Collection<Application> apps = grid.getResources(Application.class,
				site);

		System.out.println(StringUtils.join(apps, "\n\t"));
	}

	@Test
	public void testGetAllApplicationsOnGrid() {
		fail("Not yet implemented");
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
