def call(Map args = [:]){
  if(isUnix()) {
    sh(args)
  } else {
    echo "Since I don't have any windows let's use sh instead"
    sh(args)
  }
}
