import base.SpockTestBase

class WhenSpecification extends SpockTestBase {

  def stepName = 'vars/when.groovy'

  def 'handling without any argument'() {
    given:
    def script = loadScript(stepName)

    when: 'when step is run'
    script.call()
    printCallStack()

    then: 'an error being thrown'
    assert(assertMethodCallContainsPattern('error', 'project param is required'))
  }
}