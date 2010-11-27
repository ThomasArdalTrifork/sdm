import org.apache.tools.ant.filters.*

processResources.configure {
	// Replace any tokens with properties.
	from(sourceSets.main.resources.srcDirs) {
		filter(ReplaceTokens, tokens: settings.production)
		include '**/*.properties'
	}
	from(sourceSets.main.resources.srcDirs) {
		exclude '**/*.properties'
	}
}

processTestResources.doFirst {
	println settings.test
}

processTestResources.configure {
	// First copy any resources from the production
	// resources.
	from(sourceSets.main.resources.srcDirs) {
		filter(ReplaceTokens, tokens: settings.test)
		include '**/*.properties'
	}
	from(sourceSets.main.resources.srcDirs) {
		exclude '**/*.properties'
	}
	
	// Then overwrite them with any test resources.
	from(sourceSets.test.resources.srcDirs) {
		filter(ReplaceTokens, tokens: settings.test)
		include '**/*.properties'
		overwrite = true
	}
	from(sourceSets.test.resources.srcDirs) {
		exclude '**/*.properties'
		overwrite = true
	}
}
