pipeline {

  environment {
    PROJECT = "anodiamgcpproject"
    APP_NAME = "anodiam-login-service"
    TARGET_NAMESPACE = "dev-ns"
    CLUSTER_ZONE = "us-central1"
    IMAGE_TAG = "${CLUSTER_ZONE}-docker.pkg.dev/${PROJECT}/anodiam-repo/${APP_NAME}:v${env.BUILD_NUMBER}"
    JENKINS_CRED = "${PROJECT}"
  }

  agent {
    kubernetes {
      label '${APP_NAME}'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
  component: ci
spec:
  serviceAccountName: jenkins-admin
  automountServiceAccountToken: false
  containers:
  - name: gcloud
    image: gcr.io/cloud-builders/gcloud
    command:
    - cat
    tty: true
  - name: kubectl
    image: gcr.io/cloud-builders/kubectl
    command:
    - cat
    tty: true
"""
}
  }
  stages {
    //stage('Build and push image with Container Builder') {
    //  steps {
    //    container('gcloud') {
    //      sh "PYTHONUNBUFFERED=1 gcloud builds submit -t ${IMAGE_TAG} ."
    //    }
    //  }
    //}
    stage('Deploy Dev') {
      // Feature branch
      when { branch 'feature/**' }
      steps {
        container('kubectl') {
          // Change deployed image to the one we just built
          //sh("sed -i.bak 's#APP_IMAGE#${IMAGE_TAG}#' ./k8s/*.yaml")
          sh "PYTHONUNBUFFERED=1 gcloud container clusters get-credentials gke_anodiamgcpproject_us-central1-c_cluster-anodiam-dev"
          sh 'kubectl apply -n dev-ns -f ./k8s'
          //withKubeConfig([namespace: 'dev-ns', credentialsId: 'anodian.system', serverUrl: 'https://34.133.91.63']) {
          //  sh 'kubectl apply -f ./k8s'
          //}
        }
      }
    }
  }
}
