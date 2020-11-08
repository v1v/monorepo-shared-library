import org.junit.Before
import org.junit.After
import org.junit.Test
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import mock.RunFunction

class StageStepTests extends PipelineTestHelper {
  def script

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()
    script = loadScript('vars/stage.groovy')
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
  void test_with_no_platform() throws Exception {
    try {
      script.call(project: 'foo', content: [:], function: new RunFunction(steps: this))
    } catch (e) {
      // NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'platform entry in the content is required'))
    assertJobStatusFailure()
  }

  @Test
  void test_with_no_function() throws Exception {
    try {
      script.call(project: 'foo', content: [:])
    } catch (e) {
      // NOOP
    }
    printCallStack()
    assertTrue(assertMethodCallContainsPattern('error', 'function param is required'))
    assertJobStatusFailure()
  }

  @Test
  void test_simple() throws Exception {
    def ret = script.call(project: 'foo', content: [
      "platform" : [ "label-1 && label-2" ],
      "stages": [
        "simple" : [
          "build" : [ "foo" ]
        ]
      ]
    ], function: new RunFunction(steps: this))
    printCallStack()
    assertTrue(ret.size() == 1)
    assertTrue(assertMethodCallContainsPattern('echo', 'stage: foo-simple'))
    assertJobStatusSuccess()
  }

  @Test
  void test_multiple() throws Exception {
    def ret = script.call(project: 'foo', content: [
      "platform" : [ "label-1 && label-2" ],
      "stages": [
        "simple" : [
          "build" : [ "foo" ]
        ],
        "multi" : [
          "build" : [ "bar" ],
          "platforms" : [ 'windows-2019', 'windows-2016' ]
        ]
      ]
    ], function: new RunFunction(steps: this))
    printCallStack()
    assertTrue(ret.size() == 3)
    assertTrue(assertMethodCallContainsPattern('echo', 'stage: foo-simple'))
    assertTrue(assertMethodCallContainsPattern('echo', 'stage: foo-multi-windows-2019'))
    assertTrue(assertMethodCallContainsPattern('echo', 'stage: foo-multi-windows-2016'))
    assertJobStatusSuccess()
  }

  @Test
  void test_multiple_when_without_match() throws Exception {
    helper.registerAllowedMethod('when', [Map.class], {return false})
    def ret = script.call(project: 'foo', content: [
      "platform" : [ "label-1 && label-2" ],
      "stages": [
        "simple" : [
          "build" : [ "foo" ]
        ],
        "multi" : [
          "build" : [ "bar" ],
          "platforms" : [ 'windows-2019' ]
        ],
        "multi-when" : [
          "build" : [ "zet" ],
          "platforms" : [ 'windows-2016' ],
          "when" : [ 
            "comments" : [ "/test for windows-2016" ]
          ]
        ]
      ]
    ], function: new RunFunction(steps: this))
    printCallStack()
    assertTrue(ret.size() == 2)
    assertFalse(assertMethodCallContainsPattern('echo', 'stage: foo-multi-when'))
    assertJobStatusSuccess()
  }

  @Test
  void test_multiple_when_with_match() throws Exception {
    helper.registerAllowedMethod('when', [Map.class], { return true })
    def ret = script.call(project: 'foo', content: [
      "platform" : [ "label-1 && label-2" ],
      "stages": [
        "simple" : [
          "build" : [ "foo" ]
        ],
        "multi" : [
          "build" : [ "bar" ],
          "platforms" : [ 'windows-2019' ]
        ],
        "multi-when" : [
          "build" : [ "zet" ],
          "platforms" : [ 'windows-2016' ],
          "when" : [ 
            "comments" : [ "/test for windows-2016" ]
          ]
        ]
      ]
    ], function: new RunFunction(steps: this))
    printCallStack()
    assertTrue(ret.size() == 3)
    assertTrue(assertMethodCallContainsPattern('echo', 'stage: foo-multi-when'))
    assertJobStatusSuccess()
  }
}