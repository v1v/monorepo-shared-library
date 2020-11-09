import org.junit.Before
import org.junit.After
import org.junit.Test
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class WhenStepTests extends base.PipelineTestHelper {
  def script

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()
    script = loadScript('vars/when.groovy')
  }

  @Test
  void test_with_no_data() throws Exception {
    try {
      script.call()
    } catch (e) {
      // NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'project param is required'))
    assertJobStatusFailure()
  }

  @Test
  void test_with_no_project() throws Exception {
    try {
      script.call(project: 'foo')
    } catch (e) {
      // NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'content param is required'))
    assertJobStatusFailure()
  }

  @Test
  void test_with_description() throws Exception {
    def ret = script.call(project: 'foo', description: 'bar', content: [:])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenBranches_and_no_environment_variable() throws Exception {
    def ret = script.whenBranches()
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenBranches_and_environment_variable_but_no_data() throws Exception {
    addEnvVar('BRANCH_NAME', 'branch')
    def ret = script.whenBranches(content: [:])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenBranches_and_environment_variable_with_data() throws Exception {
    addEnvVar('BRANCH_NAME', 'branch')
    def ret = script.whenBranches(content: [branches: true])
    printCallStack()
    assertTrue(ret)
  }

  @Test
  void test_whenBranches_and_environment_variable_with_data_and_prs() throws Exception {
    addEnvVar('BRANCH_NAME', 'branch')
    addEnvVar('CHANGE_ID', 'PR-1')
    def ret = script.whenBranches(content: [branches: true])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenComments_and_no_environment_variable() throws Exception {
    def ret = script.whenComments()
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenComments_and_environment_variable_but_no_data() throws Exception {
    addEnvVar('GITHUB_COMMENT', 'foo')
    def ret = script.whenComments(content: [:])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenComments_and_environment_variable_with_match() throws Exception {
    addEnvVar('GITHUB_COMMENT', '/test foo')
    def ret = script.whenComments(content: [comments: ['/test foo']])
    printCallStack()
    assertTrue(ret)
  }

  @Test
  void test_whenComments_and_environment_variable_without_match() throws Exception {
    addEnvVar('GITHUB_COMMENT', '/test foo')
    def ret = script.whenComments(content: [comments: ['/run bla', '/test bar']])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenEnabled_without_data() throws Exception {
    def ret = script.whenEnabled()
    printCallStack()
    assertTrue(ret)
  }

  @Test
  void test_whenEnabled_with_data() throws Exception {
    def ret = script.whenEnabled(content: [:])
    printCallStack()
    assertTrue(ret)
  }

  @Test
  void test_whenEnabled_with_disabled() throws Exception {
    def ret = script.whenEnabled(content: [ disabled: true])
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenEnabled_with_no_disabled() throws Exception {
    def ret = script.whenEnabled(content: [ disabled: false])
    printCallStack()
    assertTrue(ret)
  }

  @Test
  void test_whenParameters_and_no_params() throws Exception {
    def ret = script.whenParameters()
    printCallStack()
    assertFalse(ret)
  }

  @Test
  void test_whenParameters_and_params_without_match() throws Exception {
    def ret = script.whenParameters(content: [parameters : ['foo', 'bar']])
    printCallStack()
    assertFalse(ret)
  }

  void test_whenParameters_and_params_with_match() throws Exception {
    binding.setVariable('bar', 'true')
    def ret = script.whenParameters(content: [parameters : ['foo', 'bar']])
    printCallStack()
    assertTrue(ret)
  }

  void test_whenParameters_and_params_with_match_but_disabled() throws Exception {
    binding.setVariable('bar', 'false')
    def ret = script.whenParameters(content: [parameters : ['foo', 'bar']])
    printCallStack()
    assertFalse(ret)
  }
}