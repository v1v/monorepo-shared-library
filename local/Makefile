.DEFAULT_GOAL := start

.PHONY: build
build:
	@env docker-compose --file docker-compose.yml build

.PHONY: start
start:
	@env docker-compose --file docker-compose.yml up --detach --no-recreate

.PHONY: stop
stop:
	@env docker-compose --file docker-compose.yml stop

.PHONY: start-local-worker
start-local-worker:  ## Start the local worker
	[ ! -e agent.jar ] && wget --quiet -O agent.jar http://localhost:8080/jnlpJars/agent.jar || true
	{ nohup java -jar agent.jar -jnlpUrl http://localhost:8080/computer/local/slave-agent.jnlp > local.log 2>&1 & echo $$! > local.pid; }

.PHONY: stop-local-worker
stop-local-worker:  ## Stop the local worker
	[ -e local.pid ] && ( kill -9 `cat local.pid` ; rm local.pid ) || true

.PHONY: start-linux-worker
start-linux-worker:  ## Start the local linux worker
	VAGRANT_CWD=workers/linux/ vagrant up --provision

.PHONY: stop-linux-worker
stop-linux-worker:  ## Stop the local linux worker
	VAGRANT_CWD=workers/linux/ vagrant halt