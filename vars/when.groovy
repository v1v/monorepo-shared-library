import groovy.transform.Field

/**
* Given the YAML definition then it verifies if the project or stage should be enabled.
*/
Boolean call(Map args = [:]){
  def project = args.containsKey('project') ? args.project : error('when: project param is required')
  def content = args.containsKey('content') ? args.content : error('when: content param is required')
  def description = args.get('description', '')
  def ret = false

  if (whenEnabled(args)) {
    if (whenBranches(args)) { ret = true }
    if (whenComments(args)) { ret = true }
    if (whenParameters(args)) { ret = true }
  }
  return ret
}

private Boolean whenBranches(Map args = [:]) {
  return (args.content?.get('branches', false) && isBranch())
}

private Boolean whenComments(Map args = [:]) {
  if (args.content?.get('comments', false) && env.GITHUB_COMMENT?.trim()) {
    return args.content.get('comments').find { env.GITHUB_COMMENT?.toLowerCase()?.contains(it?.toLowerCase()) } ? true : false
  }
  return false
}

private boolean whenEnabled(Map args = [:]) {
  return !args.content?.get('disabled', false)
}

private Boolean whenParameters(Map args = [:]) {
  if (args.content?.get('parameters')) {
    return args.content.get('parameters').find { params[it] } ? true : false
  }
  return false
}
