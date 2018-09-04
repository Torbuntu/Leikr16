/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leikr.core.RepoDirectory;

import com.badlogic.gdx.Gdx;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 *
 * @author tor
 */
public class RepoHandler {

    String userRepo = "";

    public void setUserRepo(String repoUser) {
        this.userRepo = repoUser;
    }

    public String lpmInstall(String repoName) {
        if (userRepo.length() < 2) {
            return "No user repository set. Set it with `setUserRepo [name]`";
        }
        try {
            Git.cloneRepository().setURI("https://github.com/" + userRepo + "/" + repoName + ".git").setDirectory(new File(Gdx.files.getExternalStoragePath() + "Leikr/Download/" + repoName)).call();
            return repoName + " installed";
        } catch (GitAPIException ex) {
            return ex.getMessage();
        }
    }

    public String lpmInstall(String repoName, String localDir) {
        if (userRepo.length() < 2) {
            return "No user repository set. Set it with `setUserRepo [name]`";
        }
        try {
            Git.cloneRepository().setURI("https://github.com/" + userRepo + "/" + repoName + ".git").setDirectory(new File(Gdx.files.getExternalStoragePath() + "Leikr/Download/" + localDir)).call();
            return repoName + " installed to " + localDir;
        } catch (GitAPIException ex) {
            return ex.getMessage();
        }
    }

}
