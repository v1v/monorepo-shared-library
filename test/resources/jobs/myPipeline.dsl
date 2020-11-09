NAME = 'it/myPipeline'
DSL = '''myPipeline()'''

pipelineJob(NAME) {
  definition {
    cps {
      script(DSL.stripIndent())
    }
  }
}

// If required to be triggered automatically
queue(NAME)
