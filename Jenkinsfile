pipeline {
   agent any

stages {
    stage('Run gcloud') {

        steps {
            withEnv(['GCLOUD_PATH=/var/jenkins_home/google-cloud-sdk/bin']) {
                sh '$GCLOUD_PATH/gcloud --version'
            }


         }
      }
   }
}