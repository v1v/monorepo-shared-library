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
