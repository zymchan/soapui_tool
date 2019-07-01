# soapui_tool

<h2>配置前工作</h2>
    <p>确保电脑正确安装并配置了jdk</p>
    <p>确保电脑安装了maven</p>
    <p>安装SoapUI, 记住SoapUI的安装路径，将bin目录加入电脑的path中，例如我的就是：C:\Program Files\SmartBear\SoapUI-5.5.0\bin</p>    
    <p>获取项目，在项目的根目录执行mvn clean install，会在target目录下生成jar包，取有相关依赖那个，修改jar包名称为soap_tool.jar</p>
    <p>解压项目中的ext.zip文件，把里面jar包放入soapui的ext目录中</p>
    

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
