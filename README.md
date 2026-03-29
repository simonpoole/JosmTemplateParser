[![build status](https://github.com/simonpoole/JosmTemplateParser/actions/workflows/javalib.yml/badge.svg)](https://github.com/simonpoole/JosmTemplateParser/actions) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=alert_status)](https://sonarcloud.io/dashboard?id=JosmTemplateParser) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=coverage)](https://sonarcloud.io/dashboard?id=JosmTemplateParser) [![sonarcloud bugs](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=bugs)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=bugs) [![sonarcloud maintainability](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Maintainability) [![sonarcloud security](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=security_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Security) [![sonarcloud reliability](https://sonarcloud.io/api/project_badges/measure?project=JosmTemplateParser&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=JosmTemplateParser&metric=Reliability)

# JosmTemplateParser

## Grammar

`{key}` - insert the _value_ of the tag that corresponds to _key_ .

`{%key}` - insert the _display value_ of the tag corresponding to _key_ if one is available, otherwise just insert the _value_ _(this is an extension)_ .

`?{condition1 'value1' | condition2 'value2' | 'value3'}` - use value1 if condition1 is satisfied, else use value2 if condition2 is satisfied, finally use value3 if no condition is satisfied. Condition can be either explicit - in JOSM search syntax - or implicit: The value is used when all tags referenced inside exist.

`!{search_expression 'template'}` - search_expression is evaluated and first matching primitive is used as context for template. Useful for example to get tags of parent relation.

`\` - use a backslash to escape special characters '{', '}', '?', '!'. E.g. What is this\? It is a `{type}\!`.

`{special:everything}` - prints all available values, output is implementation dependent.

`{special:id}` - prints the ID of the OSM element.

`{special:localName}` - prints the localized name, that is the value of _name:lang_ for your language if it is available, or the value of _name_ if it is not. 


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

Currently there is no documentation of the grammar outside of [Name template details](https://josm.openstreetmap.de/wiki/TaggingPresets#name_templatedetails) which however has multiple errors and inconsistencies. 

Note that  the _{special:localName}_ template simply returns the value of the name tag. 

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
	    compile 'ch.poole.osm:JosmTemplateParser:0.3.0'
	    ...
	}
