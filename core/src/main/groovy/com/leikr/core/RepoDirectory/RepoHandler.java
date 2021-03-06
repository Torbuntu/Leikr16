/*
 * Copyright 2018 . torbuntu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leikr.core.RepoDirectory;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 *
 * @author tor
 */
public class RepoHandler {

    String userRepo = "";
    String repoType = "";

    public void setUserRepo(String repoUser) {
        this.userRepo = repoUser;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public void repoSettings(String repoUser, String repoType) {
        this.userRepo = repoUser;
        this.repoType = repoType;
    }

    public String lpmInstall(String repoName) {
        if (userRepo.length() < 2) {
            return "No user repository set. Set it with `setUserRepo [name]`";
        }
        if (repoType.length() < 2) {
            return "No repository type set. Set it with `setRepoType [type]`. Known options currently are: `github` , `gitlab` ";
        }
        try {
            Git.cloneRepository().setURI("https://" + repoType + ".com/" + userRepo + "/" + repoName + ".git").setDirectory(new File(Gdx.files.getExternalStoragePath() + "Leikr/Download/" + repoName)).call();
            return "Install success: `" +repoName + "`"; 
        } catch (GitAPIException ex) {
            return "Install failure:  " + ex.getMessage();
        }
    }

    public String lpmUpdate(String repoName) {

        //return "Update currently unsupported";
        if (userRepo.length() < 2) {
            return "No user repository set. Set it with `setUserRepo [name]`";
        }
        if (repoType.length() < 2) {
            return "No repository type set. Set it with `setRepoType [type]`. Known options currently are: `github` , `gitlab` ";
        }
        try {
            Git git = Git.open( new F‌ile( Gdx.files.getExternalStoragePath() + "Leikr/Download/" + repoName+"/.git") );
            PullCommand pc = git.pull();
            pc.call();

            return "Update success: `" +repoName + "`"; 
        } catch (IOException | GitAPIException ex) {
            return "Update failure:  " + ex.getMessage();
        }
    }
}
