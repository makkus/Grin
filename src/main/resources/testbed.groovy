import grisu.jcommons.model.info.*


// sites

testbed = new Site(
	name = 'Testbed'
	)

// vos
test = new VO(
	voName = 'test',
	host = 'voms.test.bestgrid.org',
	port = 15000,
	hostDN = '/C=nz/O=org/O=nesi/OU=test/OU=local/CN=voms.test.bestgrid.org'
	)

// groups
nesi = new Group(
		vo = test,
		fqan = "/test/nesi"
		)

test = new Group(
		vo = test,
		fqan = "/test/nesi/test"
		)

// filesystems
akl_fs = new FileSystem(
		host:'globus.test.nesi.org.nz',
		protocol:'gsiftp',
		port:2811,
		site:testbed
		)


// directories
akl_home = new Directory(
		filesystem:akl_fs,
		groups:[nesi],
		path:"/~/",
		shared:false,
		volatileDirectory:false)

gram52 = Middleware.get("Globus", "5.2");

// gateways
testbed_gram52 = new Gateway(
		site:testbed,
		host:'globus.test.nesi.org.nz',
		middleware:gram52
		)
		
// applications
java = new Application(
		name:'Java'
		)

python = new Application(
		name:'Python'
		)

// executables
exe_java = Executable.get('java')
exe_javac = Executable.get('javac')
exe_python = Executable.get('python')

// packages
java15 = new Package(
		application:java,
		version: Version.get('1.5.0'),
		executables: [exe_java, exe_javac]
		)


python26 = new Package(
		application:python,
		version:Version.get('2.6'),
		executables: [exe_python]
		)
		
// queues
batch = new Queue(
		gateway:testbed_gram52,
		name:'batch',
		groups:[nesi],
		directories:[akl_home],
		packages:[java15,python26]
		)

