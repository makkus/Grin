package grisu.grin

import grisu.grin.model.Grid
import grisu.jcommons.configuration.CommonGridProperties
import grisu.jcommons.constants.GridEnvironment
import grisu.jcommons.git.GitRepoUpdater
import grisu.jcommons.model.info.*

import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.collect.Maps



class YnfoManager  {
	
	static final Logger log = LoggerFactory.getLogger(YnfoManager.class);
	
	public static String CURRENT_CONFIG;
	public static String CURRENT_LOCAL_CONFIG;
	
	class UpdateInfoTask extends TimerTask {
		public void run() {
			log.debug("Kicking of automated info refresh...");
			refreshAndWait()
			log.debug("Automated info refresh finished.");
		}
	}


	static void printConnections(def res) {

		res.getConnections().each { c ->

			println '\t\t'+c.toString()

			//			AbstractResource.getRecursiveConnections(c).each { it ->
			//				println '\t\t\t'+c+': '+ it.toString()
			//			}
		}
	}

	static void main (args) {

		//def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/main/resources/nesi.groovy')
		//		def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/main/resources/testbed.groovy')
		//		def ym = new YnfoManager('https://raw.github.com/nesi/nesi-grid-info/develop/nesi.groovy')
		//		def ym = new YnfoManager(null)
		//		def ym = new YnfoManager('/home/markus/Workspaces/Goji/grin/src/test/resources/test_2_sites.config.groovy')

		//		ym.startPeriodicRefresh(2)

		if (args.length == 0 ) {
			println 'No url specified, using default testbed config...'
			args = [
				//'https://raw.github.com/nesi/nesi-grid-info/develop/testbed_info.groovy'
                    'git://github.com/nesi/nesi-grid-info.git/nesi/nesi_info_dyn.groovy'
			]
		}

		def url = args[0]
		println 'Using info config from: "'+url+'..."'
		def ym = new YnfoManager(url)

		Grid grid = ym.getGrid()

		println 'Sites:'
		for ( def site : grid.getSites() ) {
			println '\t'+site.toString()
			//			printConnections(site)
		}

		println 'VOs:'
		for ( def vo : grid.getVos() ) {
			println '\t'+vo.getVoName()
			//			printConnections(vo)
		}

		println 'Groups:'
		for ( def group : grid.getGroups() ) {
			println '\t' + group.getFqan()
			//			printConnections(group)
		}

		println 'Filesystems:'
		for ( def fs : grid.getFilesystems() ) {
			println '\t' + fs.toUrl()
			//			printConnections(fs)
		}

		println 'Directories:'
		for ( def dir : grid.getDirectorys() ) {
			println '\t' + dir.toUrl()
			//			printConnections(dir)
		}

		println 'Gateways:'
		for ( def gw : grid.getGateways() ) {
			println '\t' + gw.toString()
			//			printConnections(gw)
		}

		println 'Queues:'
		for ( def q : grid.getQueues() ) {
			println '\t' + q.toString()
			//			printConnections(q)
		}

		println 'Applications:'
		for ( def app : grid.getApplications() ) {
			println '\t' + app.getName()
			//			printConnections(app)
		}

		println 'Versions:'
		for ( def v : grid.getVersions() ) {
			println '\t' + v.getVersion()
			//			printConnections(v)
		}

		println 'Packages:'
		for ( def p : grid.getPackages() ) {
			println '\t' + p.getName()
			//			printConnections(p)
		}

        println ym.getConfigString()

		System.exit(0)
	}


	// min threshold for updating info (in seconds)
	static def threshold = 4


	def grid = new Grid()
	String path = null

	def timer = new Timer("Info update timer", true)
	def task

	Date lastUpdated

    String configString = "n/a"

	public YnfoManager() {
		this(null)
	}

	public YnfoManager(String pathToConfig) {
		path = pathToConfig
		initialize(path)
	}

	public refreshAndWait() {
		initialize(path)
	}

	public void refresh() {
		Thread t = new Thread() {
					public void run() {
						initialize(path)
					}
				}
		t.setName('YnfoUpdate')
		t.setDaemon(true)
		t.start()
	}

	public startPeriodicRefresh(int seconds) {

		if ( task ) {
			task.cancel()
		}

		task = timer.scheduleAtFixedRate(new UpdateInfoTask(), seconds*1000, seconds*1000)
	}

	protected synchronized void initialize(String pathToConfig) {

		Date now = new Date()

		if ( !lastUpdated ||  (now.getTime() - lastUpdated.getTime() > (threshold*1000) ) ) {

			Version.clearCache()
			Middleware.clearCache()
			Executable.clearCache()
			Application.clearCache()
			
			def gridtemp = new Grid()

			try {
				def config

				if ( ! pathToConfig ) {
					pathToConfig = CommonGridProperties.getDefault().getGridInfoConfig()
				}


				// check whether there is default "grid.groovy" file somewhwere
				if ( ! pathToConfig ) {
					File file = GridEnvironment.getGridInfoConfigFile()
					if ( file.exists() ) {
						pathToConfig = file.getAbsolutePath()
					}
				}

				if ( ! pathToConfig ) {
					pathToConfig = 'testbed'
				}

				log.debug('Updating info for path/alias: '+pathToConfig)

				if ( "testbed".equals(pathToConfig) ) {
					//					InputStream is = getClass().getResourceAsStream('/testbed.groovy')
					//					config = new ConfigSlurper().parse(is.getText())
					//					config = new ConfigSlurper().parse(new URL('https://raw.github.com/nesi/nesi-grid-info/develop/testbed.groovy'))
					pathToConfig = 'https://raw.github.com/nesi/nesi-grid-info/master/testbed_info.groovy'
				} else if ("nesi".equals(pathToConfig)) {
					//					InputStream is = getClass().getResourceAsStream('/nesi.groovy')
					//					config = new ConfigSlurper().parse(is.getText())
					//					config = new ConfigSlurper().parse(new URL('https://raw.github.com/nesi/nesi-grid-info/develop/nesi.groovy'))
					pathToConfig = 'https://raw.github.com/nesi/nesi-grid-info/master/nesi_info.groovy'

				} 
				
				CURRENT_CONFIG = pathToConfig
								
				if (pathToConfig.startsWith('git://') ) {
					log.debug 'Checking out/updating config from git: "'+pathToConfig+'"...'
					
					File gitRepoFile = GitRepoUpdater.ensureUpdated(pathToConfig)
                    configString = gitRepoFile.text
					pathToConfig = gitRepoFile.getAbsolutePath()
					CURRENT_LOCAL_CONFIG = gitRepoFile.getAbsolutePath()
					config = new ConfigSlurper().parse(new File(pathToConfig).toURL())
					
				} else if ( pathToConfig.startsWith('http') ) {
					log.debug 'Retrieving remote config from "'+pathToConfig+'"...'
					config = new ConfigSlurper().parse(new URL(pathToConfig))
                    configString = "n/a"
				} else {
					log.debug 'Using local config from "'+pathToConfig+'"...'
					File c = new File(pathToConfig)
                    configString = c.text
					CURRENT_LOCAL_CONFIG = c.getAbsolutePath()
					config = new ConfigSlurper().parse(c.toURL())
				}


				for (def e in config) {

					def name = e.key
					def object = e.value

					if ( object instanceof AbstractResource ) {

						if ( ! object.getAlias() ) {
							object.setAlias(name)
						}
					}

					switch(object.class) {

						case Directory.class:
							
							if ( object.isAvailable() && object.getFilesystem().isAvailable() ) {
								log.debug "Adding directory: "+object.toUrl()
								gridtemp.addDirectory(object)
							} else {
								log.debug "Directory not available: "+object.toUrl()
							}
							break
							
						case Queue.class:
							if ( object.getGateway().isAvailable() ) {
								Set<Directory> temp_dirs = object.getDirectories()
								
								if ( temp_dirs.any() { it ->
									it.isAvailable() && it.getFilesystem().isAvailable()
								}) {
									log.debug "Adding queue: "+object.toString()
									gridtemp.addQueue(object)
								} else {
									log.debug("Queue not available because no filesystem: "+object.toString())
								}
							} else {
								log.debug "Queue not available: "+object.toString()
							}

							break
					}
				}

				log.debug('Grid object created for path/alias: '+pathToConfig+'. Validating...')

				gridtemp.validate()
				log.debug('Grid object validated for path/alias: '+pathToConfig)
			} catch (all) {
				log.error("Can't build Grid object, probably info config broken: "+all.getLocalizedMessage(), all)
				return
			}
			this.grid = gridtemp
			lastUpdated = new Date()
		}
	}

	def download(String address)
	{
		tempfile = new FileOutputStream(File.createTempFile("temp", address.tokenize("/")[-1]))
		def out = new BufferedOutputStream(file)
		out << new URL(address).openStream()
		out.close()
		tempfile
	}

    public String getConfigString() {
        return configString
    }


	public Grid getGrid() {
		return grid;
	}
}
