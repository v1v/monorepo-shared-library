
/**
  Base class to implement specific functions for the monorepo pipeline.
*/Function
class MyFunction {

  /** object to access to pipeline steps */
  public steps

  public Function(Map args){
    this.steps = args.steps
  }
  
  /**
    This method should be overwritten by the target pipeline.
  */
  protected run(Map args){ }
}