# TODO This entire code should be either some functions or a class ... this is a mess!

# TODO make an alias for:
#   'pipenv run python '  prp??

# returns:
# {
#     "message": "Must specify two-factor authentication OTP code.",
#     "documentation_url": "https://developer.github.com/v3/auth#working-with-two-factor-authentication",
# }
# if you have 2FA activated


class GitHub:
    try:
        import ujson as json
    except:
        import json
    import requests
    import numpy as np
    import pandas as pd

    from requests.auth import HTTPBasicAuth

    def __init__(self, filename: str = "credentials_private.json"):
        self.credentials: Any = json.loads(open("credentials_private.json").read())
        self.username: str = self.credentials["username"]
        self.password: str = self.credentials["password"]
        self.authentication: HTTPBasicAuth = HTTPBasicAuth(self.username, self.password)
        self.data = requests.get(self.login_url(), auth=self.authentication)
        self.data = self.data.json()
        print(self.data)
        super().__init__()

    def login_url(self) -> str:
        return "https://api.github.com/users/" + self.username

    def print_info(self):
        print(self.data)
        print("Information about user {}:\n".format(self.username))
        print("Name: {}".format(self.data["name"]))
        print("Email: {}".format(self.data["email"]))
        print("Location: {}".format(self.data["location"]))
        print("Public repos: {}".format(self.data["public_repos"]))
        print("Public gists: {}".format(self.data["public_gists"]))
        print("About: {}\n".format(self.data["bio"]))

    def get_repos(self):
        print("Collecting repositories information")
        url: str = self.data["repos_url"]
        page_no: int = 1
        repos_fetched: int = 0
        repos_data: List(Any) = []
        while True:
            response = requests.get(url, auth=self.authentication)
            response = response.json()
            repos_data = repos_data + response
            repos_fetched = len(response)
            print("Total repositories fetched: {}".format(repos_fetched))
            if repos_fetched == 30:
                page_no = page_no + 1
                url = self.data["repos_url"] + "?page=" + str(page_no)
            else:
                break

        self.repos_information = []
        for i, repo in enumerate(repos_data):
            self.repo_data = []
            self.repo_data.append(repo["id"])
            self.repo_data.append(repo["name"])
            self.repo_data.append(repo["description"])
            self.repo_data.append(repo["created_at"])
            self.repo_data.append(repo["updated_at"])
            self.repo_data.append(repo["owner"]["login"])
            self.repo_data.append(
                repo["license"]["name"] if repo["license"] != None else None
            )
            self.repo_data.append(repo["has_wiki"])
            self.repo_data.append(repo["forks_count"])
            self.repo_data.append(repo["open_issues_count"])
            self.repo_data.append(repo["stargazers_count"])
            self.repo_data.append(repo["watchers_count"])
            self.repo_data.append(repo["url"])
            self.repo_data.append(repo["commits_url"].split("{")[0])
            self.repo_data.append(repo["url"] + "/languages")
            self.repos_information.append(self.data)

        repos_df = pd.DataFrame(
            self.repos_information,
            columns=[
                "Id",
                "Name",
                "Description",
                "Created on",
                "Updated on",
                "Owner",
                "License",
                "Includes wiki",
                "Forks count",
                "Issues count",
                "Stars count",
                "Watchers count",
                "Repo URL",
                "Commits URL",
                "Languages URL",
            ],
        )

    def print_language_data(self):
        print("Collecting language data")
        for i in range(self.repos_df.shape[0]):
            response = requests.get(
                self.repos_df.loc[i, "Languages URL"], auth=self.authentication
            )
            response = response.json()
            if response != {}:
                languages = []
                for key, value in response.items():
                    languages.append(key)
                languages = ", ".join(languages)
                repos_df.loc[i, "Languages"] = languages
            else:
                repos_df.loc[i, "Languages"] = ""
        print("Language data collection complete")
        self.repos_df.to_csv("repos_info.csv", index=False)
        print("Saved repositories information to repo_info.csv\n")

    def print_commits_data(self):
        print("Collecting commits information")
        commits_information = []
        for i in range(self.repos_df.shape[0]):
            url = repos_df.loc[i, "Commits URL"]
            page_no = 1
            while True:
                response = requests.get(url, auth=self.authentication)
                response = response.json()
                print("URL: {}, commits: {}".format(url, len(response)))
                for commit in response:
                    commit_data = []
                    commit_data.append(self.repos_df.loc[i, "Id"])
                    commit_data.append(commit["sha"])
                    commit_data.append(commit["commit"]["committer"]["date"])
                    commit_data.append(commit["commit"]["message"])
                    commits_information.append(commit_data)
                if len(response) == 30:
                    page_no = page_no + 1
                    url = repos_df.loc[i, "Commits URL"] + "?page=" + str(page_no)
                else:
                    break

        commits_df = pd.DataFrame(
            commits_information, columns=["Repo Id", "Commit Id", "Date", "Message"]
        )
        commits_df.to_csv("commits_info.csv", index=False)
        print("Saved commits information to commits_info.csv")


g = GitHub()
g.get_repos()
g.print_info()

print(HEADER)
print("<table>")
print("")


# ? ################## References:

# according to: https://artem.krylysov.com/blog/2015/09/29/benchmark-python-json-libraries/
# and other sources, ujson is up to 90% faster than json ... works for me
# TODO add a variable or flag for this or ... something?
# print("using ujson") #
