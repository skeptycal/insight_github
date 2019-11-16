# Insights using GitHub API

> The project involves collecting data about a user's profile and drawing insights from it. It was made using [Webpack](https://webpack.js.org/) and Clojure. [Why Clojure?](https://clojure.org/about/rationale)

[![netlify badge](https://api.netlify.com/api/v1/badges/416b8ca3-82db-470f-9adf-a6d06264ca75/deploy-status)](https://app.netlify.com/sites/mystifying-keller-ab5658/deploys) [![Build Status](https://travis-ci.com/skeptycal/clojure_site.svg?branch=master)](https://travis-ci.com/skeptycal/clojure_site) ![https://pypi.python.org/pypi/autosys](https://img.shields.io/badge/test_coverage-100%25-6600CC.svg)

[![code style: prettier](https://img.shields.io/badge/code_style-prettier-ff69b4.svg?style=flat-square)](https://github.com/prettier/prettier) [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v1.4%20adopted-ff69b4.svg)](code-of-conduct.md)

![Twitter Follow](https://img.shields.io/twitter/follow/skeptycal.svg?label=%40skeptycal&style=social) ![GitHub followers](https://img.shields.io/github/followers/skeptycal.svg?style=social) ![Keybase PGP](https://img.shields.io/keybase/pgp/skeptycal?label=Keybase%20PGP&style=social)

---

## Prerequisites

Make sure you have these prerequisites installed. Any similar version will likely work fine ...

| Tool                                                  | Version I used   |
| ----------------------------------------------------- | ---------------- |
| [Python](https://www.python.org/)                     | 3.8              |
| [Clojure](https://clojure.org/guides/getting_started) | 1.10.1.469       |
| [Lein Build Tools](https://leiningen.org/)            | 2.9.1 on Java 13 |
| [node.js / npm](https://nodejs.org/en/download/)      | 12.9.1 / 6.13    |
| [Git](https://git-scm.com/downloads)                  | 2.23.0           |
| [hub](https://hub.github.com/)                        | 2.12.8           |

If you run into permission problems with npm (not just with this project ... this is a problem with many non-rvm installs), run this terminal command to make sure npm global repo permissions are sufficient:

`sudo chown -R $(id -un):$(id -gn) $(npm root -g)`

## Quickstart (macOS)

 Run the `setup_macos.sh` if you use macOS ...

This will do everything!

or take the long way around ...

---

## Setup for linux and others ... ymmv

- Setup local project and git repo

```bash
# choose a repo name for your 'about me' page
site_name='clojure_site'
# set your GitHub username from Git ... or just type it in...
user_name=$(git config user.name)

repo_name="https://github.com/${user_name}/${site_name}"

git clone https://github.com/skeptycal/clojure_site $site_name
cd $site_name
```

- Setup Github remote repository:

```bash
# Rename remote repo. if you wish to remove the remote, use this:
#   git rm -rf .git && git init
git remote rename origin upstream

# create remote repo
hub create

# initial git commit and github push
git add all
git commit -m 'initial commit'
git push --set-upstream origin $(git_current_branch)
echo 'Github remote repositories:'
git remote -v
```

### Install dependencies:

```bash
make install
```

### Development:

```bash
make watch
make serve
```

### Build:

```bash
make build
```

### Deploy to GitHub Pages:

```bash
make github

```

### Deploy to Google Cloud (account required):

```bash
make deploy
```

---

### Contributions

[Code of Conduct](CODE_OF_CONDUCT.md)

[Bug Reports](.github/ISSUE_TEMPLATE/bug_report.md)

[Feature Requests](.github/ISSUE_TEMPLATE/feature_request.md)
