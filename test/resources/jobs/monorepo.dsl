NAME = 'it/monorepo'
multibranchPipelineJob(NAME) {
  branchSources {
    factory {
      workflowBranchProjectFactory {
        scriptPath('Jenkinsfile')
      }
    }
    branchSource {
      source {
        github {
          id('20201109') // IMPORTANT: use a constant and unique identifier
          credentialsId('UserAndToken')
          repoOwner('v1v')
          repository('jenkins-monorepo')
          repositoryUrl('https://github.com/v1v/jenkins-monorepo')
          configuredByUrl(false)
          traits {
            gitHubTagDiscovery()
            gitHubBranchDiscovery {
              strategyId(1)
            }
            gitHubPullRequestDiscovery {
              strategyId(1)
            }
          }
        }
      }
    }
  }
  orphanedItemStrategy {
    discardOldItems {
      numToKeep(20)
    }
  }
}
