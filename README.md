[![build status](https://github.com/simonpoole/JosmTemplateParser/actions/workflows/javalib.yml/badge.svg)](https://github.com/simonpoole/JosmTemplateParser/actions) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=alert_status)](https://sonarcloud.io/dashboard?id=JosmTemplateParser) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=coverage)](https://sonarcloud.io/dashboard?id=JosmTemplateParser) [![sonarcloud bugs](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=bugs)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=bugs) [![sonarcould maintainability](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Maintainability) [![sonarcloud security](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=security_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Security) [![sonarcloud reliability](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Reliability)

# JosmTemplateParser


## Usage

        try {
            JosmTemplateParser parser = new JosmTemplateParser(new ByteArrayInputStream(filterString.getBytes()));
            List<Formatter> formatters = parser.formatters();
            .....
        } catch (ParseException pex) {
            ...
        } catch (Error err) {
            ...
        }
        
Your OSM elements object must either implement the Meta interface or be wrapped in an object that implements it. The object can then be passed to the _Formatter.format_ method, resp. _Util.listFormat_.

Currently there is no documentation of the grammar outside of [Name template details](https://josm.openstreetmap.de/wiki/TaggingPresets#name_templatedetails) which however has multiple errors and inconsistencies. Note that we don't support the _{special:....}_ templates yet. 

## Including in your project

You can either download the jar from github or add the following to your build.gradle

	...
	    repositories {
	        ...   
	        mavenCentral()
	        ...              
	    }
	...
	
	dependencies {
	    ...
	    compile 'ch.poole.osm:JosmTemplateParser:0.1.0'
	    ...
	}
