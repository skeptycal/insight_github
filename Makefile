.PHONY: help install-backend install-frontend install build-frontend build-backend build watch serve deploy github test

.DEFAULT_GOAL := help

help: # http://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort |\
	awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

install-backend: ## Install the Clojure and Python backend dependencies.
	lein deps && pipenv install --python3.8

install-frontend: ## Install the JavaScript frontend dependencies.
	npm install

install: install-backend install-frontend ## Install all dependencies.

build-backend: ## Export the static website files with stasis.
	lein build

build-frontend: ## Build the frontend assets with webpack.
	npx webpack --display errors-only

build-python: ## Collect data and build database.
	pipenv run python3 ./get_github_data.py

build: build-frontend build-backend ## Export the static website files with stasis.

watch: ## Build frontend assets and watch for changes.
	npx webpack --watch --debug --progress --colors

serve: ## Serve as an application for local development.
	lein serve

deploy: ## Deploy static website assets to a Google Cloud Storage bucket.
	gcloud components update --quiet
	gsutil rsync -d -r output gs://xxxxxxxx.xxx

github: ## Deploy static site to /docs folder of github repo
	deploy_gh.sh

ci-deploy: ##
	@if [ "${CIRCLE_BRANCH}" == "master" ]; then make deploy; else echo "No deploy"; fi

test: ## Run the tests.
	lein test
