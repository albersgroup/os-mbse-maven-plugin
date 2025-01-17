This is a collection of maven projects related to open source mbse (osmbse) and the packaging and deployment of them. 

This project lays out a new custom packaging type in a pom file called mdzip and can be used in the pom model of a new project. In order for maven to successfully parse this new type, an inclusion in the plugin is necessary

```
<project>
...
<packaging>mdzip</packaging>
...

<build>
  <plugins>
    <plugin>
      <groupId>aero.albers.osmbse</groupId>
      <artifactId>mdzip-base-maven-plugin</artifactId>
      <version>0.0.1</version>
      <!-- declare that this plugin contributes the mdzip component packaging -->
      <extensions>true</extensions>
    </plugin>
    ...
  </plugins>
</project>

```

These are the currently tracked modules
1. mdzip-base-maven-plugin - The plugin to load the mdzip packaging type for the top level pom model
1. mdzip-validate-maven-plugin - the custom MOJO that processes the mdzip to ensure it's in sync with the pom file. 
1. mdzip-process-sources-maven-plugin - custom MOJO to process the source of the mdzip and copy it to the approrpiate directory