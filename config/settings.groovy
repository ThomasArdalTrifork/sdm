db {
	driver = "com.mysql.jdbc.Driver"
	url = "jdbc:mysql://localhost/"
	username = "root"
	password = ""
}	

environments {
	production {
		db.schema = "sdm"
	}
	test {
		db.schema = "sdm_test"
	}
}
