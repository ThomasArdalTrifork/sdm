// This file loads the settings for the two environments (production and test),
// and stores them in the project variable settings.

def productionSettings = new ConfigSlurper('production').parse(new File('config/settings.groovy').toURL())
def testSettings = new ConfigSlurper('test').parse(new File('config/settings.groovy').toURL())

def settings = [
	test:testSettings.toProperties(),
	production:productionSettings.toProperties()
]

project.setProperty("settings", settings)
