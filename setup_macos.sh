#!/usr/bin/env bash
###############################################################################
# setup_macos.sh : about me page builder for macOS
# version 0.0.1
#
# author    - Michael Treanor  <skeptycal@gmail.com>
# copyright - 2019 (c) Michael Treanor
# license   - MIT <https://opensource.org/licenses/MIT>
# github    - https://www.github.com/skeptycal

#? ######################## setup ANSI constants for common colors
    MAIN=$(echo -en '\001\033[38;5;229m') && export MAIN
    WARN=$(echo -en '\001\033[38;5;203m') && export WARN
    BLUE=$(echo -en '\001\033[38;5;38m') && export BLUE
    GO=$(echo -en '\001\033[38;5;28m') && export GO
    CANARY=$(echo -en '\001\033[38;5;226m') && export CANARY
    ATTN=$(echo -en '\001\033[38;5;178m') && export ATTN
    WHITE=$(echo -en '\001\033[37m') && export WHITE
    RESET=$(echo -en '\001\033[0m') && export RESET

#? ######################## setup functions for printing lines in common colors
    br() { printf "\n"; } # yes, this is a fake cli version of <br />
    ce() { printf "%b\n" "${@:-}${RESET:-}"; }
    me() { ce "${MAIN:-}${@:-}"; }
    warn() { ce "${WARN:-}${@:-}"; }
    blue() { ce "${COOL:-}${@:-}"; }
    green() { ce "${GO:-}${@:-}"; }
    canary() { ce "${CANARY:-}${@:-}"; }
    attn() { ce "${ATTN:-}${@:-}"; }
    white() { ce "${WHITE:-}${@:-}"; }

checkit() {
    # check for installed prerequisite; install if necessary
    prog_name=${1}
    inst_name=${2:-"$1"}
    eval_name=${3:-"brew install"}
    # warn "DEBUG: Installing $prog_name -> $eval_name $inst_name"
    if [[ $(command -v $prog_name) == "" ]]; then
        me "installing $prog_name"
        blue "eval $eval_name $inst_name"
    else
        attn "$prog_name already installed."
    fi
    }
###############################################################################
# install or update Homebrew
check_brew() {
    if [[ $(command -v brew) == "" ]]; then
        green "Installing Hombrew"
        /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    else
        green "Updating Homebrew"
        brew update
    fi
    }
###############################################################################
# install prerequisites
check_brew
checkit clojure
checkit lein leiningen
checkit npm
checkit git
checkit hub
checkit python
checkit pipenv

###############################################################################
# npm fix for single user computer
# run this command to make sure npm global repo permissions are sufficient:
if [ -O $(npm root -g) ]; then
    green "npm fix not needed..."
else
    attn "Installing npm path fix. (Password may be needed.)"
    sudo chown -R $(id -un):$(id -gn) $(npm root -g)
fi

brew doctor
###############################################################################
# collect repo name (default 'clojure_site')
echo -n "Choose a name for your repo (default: clojure_site): "
read site_name
[[ -z $site_name ]] && site_name='clojure_site'

# collect GitHub user name (default from git config)
git_name=$(git config user.name)
echo -n "Enter your GitHub username (default: $git_name): "
read user_name
[[ -z $user_name ]] && user_name=$git_name

repo_name="https://github.com/${user_name}/${site_name}"

###############################################################################
# Setup project git repo
# choose a repo name for your 'about me' page
git clone https://github.com/skeptycal/clojure_site $site_name
cd $site_name

# Rename remote repo. if you wish to remove the remote, use this:
#   git rm -rf .git && git init
git remote rename origin upstream
hub create
git add all
git commit -m 'initial commit'
git push --set-upstream origin $(git_current_branch)
attn 'Github remote repositories:'
git remote -v

make install
npm audit fix

. ./deploy_gh.sh
