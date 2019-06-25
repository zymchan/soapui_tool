//Get data from automation database
import groovy.sql.Sql
import groovy.json.JsonSlurper

//Get Case Name
myTestCase = testRunner.testCase
caseName = testRunner.testCase.getName()

String[] propToRemove = myTestCase.getPropertyNames()
for (int i = 0; i < propToRemove.size(); i++) {
    myTestCase.removeProperty(propToRemove[i])
    log.info propToRemove[i]
}

//define a function to execute the sql
def getPropertiesByExecuteSql(strSql) {

    //New a sql instance
    dbDriver = context.expand('${#Project#automation_DbDriver}')
    com.eviware.soapui.support.GroovyUtils.registerJdbcDriver(dbDriver)
    encoded = context.expand('${#Project#automation_Pwd}')
    byte[] decoded = encoded.decodeBase64()
    Pwd = new String(decoded)
    user = context.expand('${#Project#automation_UserName}')
    connection = context.expand('${#Project#automation_connection}')

    sqlInstance = Sql.newInstance("${connection}", "${user}", "${Pwd}", dbDriver)

    //execute sql
    def row = sqlInstance.firstRow(strSql)
    if (!row) {
        assert false: "No result found in db, SQL: ${strSql}"
    } else {
        columns = row.keySet()
        for (i = 0; i < columns.size(); i++) {
            name = columns[i]
            value = row[i].toString().trim()
            myTestCase.setPropertyValue(name, value)
            log.info "Output TestCase Property: ${name}  - ${value}"
        }
    }
    sqlInstance.close()
}


//Find the data in DataSheet table
strSql = "Select * From auto.SoapUI_JP_DataSheet Where TestName = '${caseName}'"
log.info strSql
getPropertiesByExecuteSql(strSql)


//Get testData by execute InputDataSQL
inputDataSQL = myTestCase.getPropertyValue("InputDataSQL")

if (inputDataSQL && inputDataSQL.trim() != "") {
    String[] sqls = inputDataSQL.split('\\|\\|')
    for (String sql : sqls) {
        try {
            getPropertiesByExecuteSql(sql)
        } catch (Exception e) {
            assert false: "SQL:" + sql + "<br>" + e
        }
    }
}

//get testData by InputParameter
inputParameter = myTestCase.getPropertyValue("InputParameter")
if (inputParameter && inputParameter.trim() != "") {
    try {
        def jsonSlurper = new JsonSlurper()
        def object = jsonSlurper.parseText(inputParameter)
        keys = object.keySet()
        for (String key : keys) {
            value = object.get(key)
            myTestCase.setPropertyValue(key, value)
            log.info "Output TestCase Property: ${key}  - ${value}"
        }
    } catch (Exception e) {
        assert false: "InputParameter:" + "<br>" + e
    }
}


//initial Current_i and Total_i
int Current_i = 0
int Total_i = 1

loopParameter = myTestCase.getPropertyValue("LoopParameter")
if (loopParameter && loopParameter.trim() != "") {
    try {
        def jsonSlurper = new JsonSlurper()
        def objects = jsonSlurper.parseText(loopParameter)
        Total_i = objects.size();
    } catch (Exception e) {
        assert false: "LoopParameter:" + "<br>" + e
    }
}
myTestCase.setPropertyValue("Current_i", Current_i + "")
myTestCase.setPropertyValue("Total_i", Total_i + "")