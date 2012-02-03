import grisu.grin.model.resources.*




// sites
site1 = new Site(
	name:'Site1'
	)

site2 = new Site(
	name:'Site2'
	)


// vos
vo1 = new VO(
	voName='vo1',
	host='voms1host.org',
	port=15000,
	hostDN='/C=NZ/O=The University of Auckland/OU=BeSTGRID/CN=voms.bestgrid.org'
)


vo2 = new VO(
	voName='vo2',
	host = 'voms2host.org',
	port = 15000,
	hostDN = '/C=NZ/O=The University of Auckland/OU=BeSTGRID/CN=voms.bestgrid.org'
)


// groups
vo1g1 = new Group(
		vo = vo1,
		fqan = "/vo1/g1"
		)

vo1g2 = new Group(
		vo = vo1,
		fqan = "vo1/g2"
		)

vo2g1 = new Group(
	vo = vo1,
	fqan = "/vo2/g1"
	)

vo2g2 = new Group(
	vo = vo1,
	fqan = "vo2/g2"
	)

// filesystems
s1_fs1 = new FileSystem(
	host:'fs1.site1.org',
	protocol:'gsiftp',
	port:2811,
	site:site1
	)

s1_fs2 = new FileSystem(
	host:'fs2.site1.org',
	site:site1
)


s2_fs1 = new FileSystem(
	host:'fs1.site2.org',
	protocol:'gsiftp',
	port:2811,
	site:site2
)

s2_fs2 = new FileSystem(
	host:'fs2.site2.org',
	site:site2
)

// directories
s1_d1 = new Directory(
		filesystem:s1_fs1,
		groups:[vo1g1],
		path:"/~/"
)

s1_d2 = new Directory(
		filesystem:s1_fs2,
		groups:[vo1g2],
		path:"/tmp"
)


s2_d1 = new Directory(
		filesystem:s2_fs1,
		groups:[vo2g1],
		path:"/~/"
)

s1_d2 = new Directory(
		filesystem:s2_fs2,
		groups:[vo2g2],
		path:"/tmp"
)

// applications
app1 = new Application(
		name:'App1'
)

app2 = new Application(
		name:'App2'
)

// packages
// groups is optional, if not set, groups of all queues will be used
app1_v1 = new Package(
		application:app1,
		version: new Version('1')
)

app1_v2 = new Package(
		application:app2,
		version: new Version('2')
)

// gateways
gw1 = new Gateway(
		site:site1,
		host:"gateway1.site1.org"
)

// gateways
gw2 = new Gateway(
		site:site2,
		host:"gateway1.site2.org"
)

// queues
queue1 = new Queue(
	gateway:gw1,
	name:'queue1',
	groups:[vo1g1],
	directories:[s1_d1],
	packages:[app1_v1]
)

queue2 = new Queue(
	gateway:gw2,
	name:'queue2',
	groups:[vo2g1],
	directories:[s1_d2],
	packages:[app1_v2]
)

