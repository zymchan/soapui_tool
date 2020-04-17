# soapui_tool


<h2>本工具的作用</h2>
     <p>如下面描述，本工具有三个功能:</p>
     <p> 1. 支持将你已有的soapUI Project的用例快速导出到固定的目录。</p>
     <p> 2. 根据功能1导出的用例目录中的用例，生成一个excel文件，此文件能做后面执行用例的控制界面使用。不想要执行的case在对应的Disable标注"Y"即可。</p>
     <p> 3. 最后一个功能是关联前两个功能的目录以及excel文件，执行case。执行的过程会先生成一个soapUI的project文件，然后再自动执行这个project下的所有case。 同时这个project会附带我已经写好的extent report代码，能为你生成一个漂亮的report。同时你以后再在这个生成的project上写case，直接执行这个project也会有report生成。快来试试吧！</p>
    
    
     
     
<h2>配置前工作</h2>
    <p>确保电脑正确安装并配置了jdk</p>
    <p>确保电脑安装了maven</p>
    <p>获取项目，在项目的根目录执行mvn clean install，会在target目录下生成jar包，取有相关依赖那个，修改jar包名称为soap_tool.jar</p>
    <p>安装SoapUI, 记住SoapUI的安装路径，将bin目录加入电脑的path中，例如我的就是：C:\Program Files\SmartBear\SoapUI-5.5.0\bin</p> 
    <p>解压项目中的ext.zip文件，把里面jar包放入soapui的ext目录中，</p>
    如果中文出现乱码，记得将你本地电脑添加变量JAVA_TOOL_OPTIONS，值为-Dfile.encoding=UTF-8。最后记得重启你已经打开的应用。
 


<h2>用法</h2>
当前支持三个功能，-export 用例导出， -excel 生成excel的set文件用来控制用例执行， -execute 执行用例

<h5>用例导出</h5>
<p>CMD执行</p>
<pre>
   java -jar soap_tool -export "project.xml" "Q:\CTAutomation\JP_API\TestCase" 
</pre>
<p>"project.xml"为SoapUI项目的文件保存路径，"Q:\CTAutomation\JP_API\TestCase"为将用例导出后的路径。执行完这条命令，会将"project.xml"项目中的所有Enable的Case导出到"Q:\CTAutomation\JP_API\TestCase"，会根据原来的TestSuite名字自动生成对应的文件名，Case在对应的文件下。</p>
  
<h5>根据Case生成对应的执行excel管理文件</h5>
<p>CMD执行</p>
<pre>
   java -jar soap_tool -excel "Q:\CTAutomation\JP_API\TestCase"  "Q:\CTAutomation\JP_API\RunSet\SmokeTest"
</pre>
<p>"Q:\CTAutomation\JP_API\TestCase"为你存Case的路径，即export命令的第二个参数，"Q:\CTAutomation\JP_API\RunSet\SmokeTest"为生成的excel文件的对应目录，执行完这条命令后，将在"Q:\CTAutomation\JP_API\RunSet\SmokeTest"生成一个TestCaseSet.xlsx的excel文件</p>

<h5>用例执行</h5>
<p>CMD执行</p>
<pre>
   java -jar soap_tool -execute "Q:\CTAutomation\JP_API\RunSet\SmokeTest\TestCaseSet.xlsx" "Q:\CTAutomation\JP_API\TestCase" "projectName"
</pre>
<p>"Q:\CTAutomation\JP_API\RunSet\SmokeTest\TestCaseSet.xlsx"为管理用例执行的excel文件，你可以在这里管理你需要执行的用例，"Q:\CTAutomation\JP_API\TestCase"为将用例导出后的路径。"projectName"是可选的参数，用来指定你所执行的项目的名称。执行完这条命令，会根据TestCaseSet.xlsx中的用例管理情况生成一个projectName.xml的SoapUI项目，同时执行这个文件，得到结果，并生成报告</p>


<h2>最后的废话</h2>
在实际项目的时候，我是将测试用例的测试数据放在数据库的，所以在项目中能发现有Initial Step相关的groovy代码和功能，同时有一个config.properties的配置文件来放相关的公共参数。Initial step是作为测试用例的的第一个step，会第一个执行来将用例相关的数据从数据库取出。感兴趣可以自己看下代码。
