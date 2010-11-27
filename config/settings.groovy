// Default settings

db {
	driver = "com.mysql.jdbc.Driver"
	url = "jdbc:mysql://localhost/"
	username = "root"
	password = "root"
}	


// Environment specific settings.
// These will override the defaults.

environments {
	production {
		db.schema = "sdm"
		db.housekeeping.schema = "sdm_housekeeping"
	}
	test {
		db.schema = "sdm_test"
		db.housekeeping.schema = "sdm_housekeeping_test"
	}
}
