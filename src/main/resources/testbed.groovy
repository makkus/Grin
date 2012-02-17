import grisu.jcommons.model.info.*




// sites

auckland = new Site(
	name = 'Testbed'
	)

// vos

nz = new VO(
	voName = 'nz',
	host = 'voms.test.bestgrid.org',
	port = 15000,
	hostDN = '/C=nz/O=org/O=nesi/OU=test/OU=local/CN=voms.test.bestgrid.org'
	)

// groups
nesi = new Group(
		vo = nz,
		fqan = "/nz/nesi"
		)

test = new Group(
		vo = nz,
		fqan = "/nz/nesi/test"
		)

// filesystems
akl_fs = new FileSystem(
		host:'globus.test.nesi.org.nz',
		protocol:'gsiftp',
		port:2811,
		site:auckland
		)


// directories
akl_home = new Directory(
		filesystem:akl_fs,
		groups:[nesi],
		path:"/~/",
		shared:false,
		volatileDirectory:false)

gram52 = Middleware.create("Globus", "5.2");

// gateways
gram52 = new Gateway(
		site:auckland,
		host:'globus.test.nesi.org.nz',
		middleware:gram53
		)
		
// applications
java = new Application(
		name:'Java'
		)

python = new Application(
		name:'Python'
		)

// executables
exe_java = Executable.create('java')
exe_javac = Executable.create('javac')
exe_python = Executable.create('python')

// packages
java15 = new Package(
		application:java,
		version: new Version('1.5.0'),
		executables: [exe_java, exe_javac]
		)


python26 = new Package(
		application:python,
		version:new Version('2.6'),
		executables: [exe_python]
		)
		
// queues
batch = new Queue(
		gateway:gram52,
		name:'batch',
		groups:[nesi],
		directories:[akl_home],
		packages:[java15,python26],
		noCpus:6,
		memory:1073741824
		)

