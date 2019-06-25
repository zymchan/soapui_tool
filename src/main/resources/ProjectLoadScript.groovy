//Clear all properties
String[] propToRemove = project.getPropertyNames();
for ( int i = 0 ; i < propToRemove.size(); i++ ){
    project.removeProperty( propToRemove[i] );
    log.info propToRemove[i]
}

// Load properties and loop to set as project PropertyValue
def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
def env = ""
Properties properties = new Properties()
File propertiesFile
if(env==""){
    propertiesFile = new File(groovyUtils.projectPath, 'config.properties')
}else{
    propertiesFile = new File(groovyUtils.projectPath, 'config' + '_'+env+'.properties')
}

propertiesFile.withInputStream {
    properties.load(it)
}

for (i in properties.propertyNames()){
    prop = properties[i]
    project.setPropertyValue( i,  prop)
    log.info ("Output ${i} - ${prop}")
}