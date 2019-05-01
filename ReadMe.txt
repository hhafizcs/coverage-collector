Steps to run the tool:
1-Select a test maven project.
2-Open the test project's pom.xml file and add the following plugin:
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<configuration>
		<properties>
			<property>
				<name>listener</name>
				<value>com.utd.cs6367.CommonListener</value>
			</property>
		</properties>
		<argLine>-javaagent:"${basedir}\target\coverage-collector-1.jar"</argLine>
	</configuration>
</plugin> 
If the pom.xml already has a surefire plugin entry, comment it out.
3-Open a command prompt, navigate to the base diretory of the tool project, and then run the following command:
mvn package
4-If the command from step 3 completes successfully, you will find a jar file in the target directory of the tool project named "coverage-collector-1.jar". Copy this jar file and paste it in the target directory of the test project.
5-Open a command prompt, navigate to the base diretory of the test project, and then run the following command:
mvn test
6-If the command from step 5 completes successfully, you will find 3 txt files in the base directory of the test project. The txt file names are "stmt-cov.txt", "var-cov.txt", and "invariants.txt".