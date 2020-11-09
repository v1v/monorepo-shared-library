package base

import spock.lang.Specification

class SpockTestBase extends Specification {

  @Delegate PipelineTestHelper testBase

  def setup() {
    testBase = new PipelineTestHelper()
    testBase.setUp()
  }
}