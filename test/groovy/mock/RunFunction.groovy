package mock

/**
 * Mock class for the monorepo
 */
class RunFunction extends mono.Function {

    public RunFunction(Map args = [:]){
        super(args)
    }
    public run(Map args = [:]) {
        steps.echo "-------------${args.label} ---- ${args.context}"
    }
}