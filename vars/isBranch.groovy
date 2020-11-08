def call(def args = [:]) {
  return (env.BRANCH_NAME?.trim() && !isPR())
}