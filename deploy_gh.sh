#!/usr/bin/env bash
###############################################################################
# setup_macos.sh : webpage builder for macOS
# version 0.0.1
#
# author    - Michael Treanor  <skeptycal@gmail.com>
# copyright - 2019 (c) Michael Treanor
# license   - MIT <https://opensource.org/licenses/MIT>
# github    - https://www.github.com/skeptycal

# make build
mkdir -p docs
cp -r output/ docs/

###### minor tweaks may be necessary; make changes to files in docs/ if needed

[[ -s docs/_config.yml ]] || echo "theme: jekyll-theme-slate" >docs/_config.yml
git add --all
git commit -m 'deploy_gh.sh - build and deploy'
git push
