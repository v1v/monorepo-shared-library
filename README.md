## Jenkins Shared library

A shared library with some vitamins to help you to speed up your local development and test the monorepo approach.

## Context

This is an example of a shared library for the Jenkins pipelines based on:

- [JCasC](https://jenkins.io/projects/jcasc/) to configure a local jenkins instance.
- [JobDSL](https://github.com/jenkinsci/job-dsl-plugin/wiki) to configure the pipelines to test the steps.
- [JenkinsUnitPipeline](https://github.com/jenkinsci/JenkinsPipelineUnit) to test the shared library steps.
- [Spock](http://spockframework.org/spock/docs/1.0/introduction.html) to test the shared library steps with some specifications approach.
- [Gradle](https://docs.gradle.org/current/userguide/userguide.html) to orchestrate the build/tests of this library.
- [Vagrant](https://www.vagrantup.com/docs/index.html) and [VirtualBox](https://www.virtualbox.org/wiki/Documentation) to spin up jenkins agents using the Swarm connection.

## System Requirements

- Docker >= 19.x.x
- Docker Compose >= 1.25.0
- Vagrant >= 2.2.4
- VirtualBox >= 6
- Java >= 8

## Layout

```
(root)
+- src                             # Groovy source files
|   +- org
|       +- v1v
|           +- Bar.groovy          # for org.v1v.Bar class
|   +- test
|       +- groovy
|           +- FooStepTest.groovy  # Tests for the foo step
+- vars
|   +- foo.groovy                  # for global 'foo' variable
|   +- foo.txt                     # help for 'foo' variable
+- resources                       # resource files (external libraries only)
|   +- org
|       +- v1v
|           +- bar.json            # static helper data for org.foo.Bar
+- local                           # to enable a jenkins instance with this library
|   +- configs
|       +- jenkins.yaml
|   +- docker-compose.yml
|   +- Dockerfile
|
```

## How to test it

```bash
  ./gradlew clean test
  open build/reports/tests/test/index.html
```

### How to test it within the local Jenkins instance

1. Build docker image by running:

   ```
   make build
   ```

2. Start the local Jenkins master service by running:

   ```
   make start
   ```

3. Browse to <http://localhost:8080> in your web browser.

#### Enable the local agent

You can enable your own machine to become an agent, as simple as:

1. Browse to <http://localhost:8080/computer/local/> in your web browser.

2. Download <http://localhost:8080/jnlpJars/agent.jar>

3. Run in your terminal:

   ```
   java -jar agent.jar -jnlpUrl http://localhost:8080/computer/local/slave-agent.jnlp
   ```
   NOTE: Java is required.

#### Enable the linux vagrant worker

  ```bash
  cd local/workers/linux
  vagrant up --provision
  open http://localhost:8080
  ```

#### Customise what plugins are installed

You can configure this jenkins instance as you wish, if so please change:

* local/configs/jenkins.yaml using the [JCasC](https://jenkins.io/projects/jcasc/)
* local/configs/plugins.txt


## What's next?

- Be able to programmatically run functional tests.


## Further details

This is a subset of what it has been implemented in the https://github.com/elastic/apm-pipeline-library and https://github.com/v1v/jenkins-pipeline-library-skeleton