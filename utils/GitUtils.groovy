#!groovy

def checkoutCommitHash() {
    checkout scm
    if(!env.BUILD_COMMIT_HASH) {
        env.BUILD_COMMIT_HASH = getCommitHash()
    }
    sh "git checkout $BUILD_COMMIT_HASH"
}

def getCommitHash() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    return gitCommit
}

def mergePrToMaster(prNum, prBranch) {
    sshagent([GIT_AUTH]) {
        sh """ git config --global user.email "sunlight.gen@cisco.com" """
        sh """ git config --global user.name "sunlight.gen" """
        sh "git fetch origin"
        sh "git checkout master"
        sh """ git merge --no-ff -m "Merge pull request #$prNum from $prBranch" origin/$prBranch """
        sh "git push origin master"
    }
}

return this;
