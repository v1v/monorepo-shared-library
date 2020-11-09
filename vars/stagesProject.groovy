Map call(Map args = [:]){
  def project = args.containsKey('project') ? args.project : error('beatsStages: project param is required')
  def content = args.containsKey('content') ? args.content : error('beatsStages: content param is required')
  def function = args.containsKey('function') ? args.function : error('beatsStages: function param is required')
  def defaultNode = content.containsKey('platform') ? content.platform : error('beatsStages: platform entry in the content is required.')

  def mapOfStages = [:]

  content?.stages?.each { stageName, value ->
    def tempMapOfStages = [:]
    if (value.containsKey('when')) {
      if (when(project: project, content: value.when, description: stageName)) {
        tempMapOfStages = generateStages(content: value, project: project, stageName: stageName, defaultNode: defaultNode, function: function)
      }
    } else {
      tempMapOfStages = generateStages(content: value, project: project, stageName: stageName, defaultNode: defaultNode, function: function)
    }
    tempMapOfStages.each { k,v -> mapOfStages["${k}"] = v }
  }

  return mapOfStages
}

private generateStages(Map args = [:]) {
  def content = args.content
  def project = args.project
  def stageName = args.stageName
  def defaultNode = args.defaultNode
  def function = args.function

  def mapOfStages = [:]
  if (content.containsKey('platforms')) {
    content.platforms.each { platform ->
      def id = "${project}-${stageName}-${platform}"
      echo "stage: ${id}"
      mapOfStages[id] = generateStage(context: id, project: project, label: platform, content: content, function: function, id: id)
    }
  } else {
    def id = "${project}-${stageName}"
    echo "stage: ${id}"
    mapOfStages["${id}"] = generateStage(context: id, project: project, label: defaultNode, content: content, function: function, id: id)
  }
  return mapOfStages
}

private generateStage(Map args = [:]) {
  def function = args.function
  return {
    function.run(args)
  }
}