pipeline {

  environment {
    PROJECT = "anodiamgcpproject"
    APP_NAME = "anodiam-login-service"
    FE_SVC_NAME = "${APP_NAME}-frontend"
    CLUSTER = "jenkins-cd"
    CLUSTER_ZONE = "us-central1"
    IMAGE_TAG = "${CLUSTER_ZONE}-docker.pkg.dev/${PROJECT}/anodiam-repo/${APP_NAME}:v${env.BUILD_NUMBER}"
    JENKINS_CRED = "${PROJECT}"
  }

  agent {
    kubernetes {
      label 'anodiam-login-service'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
  component: ci
spec:
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
    stage('Build and push image with Container Builder') {
      steps {
        container('gcloud') {
          sh "PYTHONUNBUFFERED=1 gcloud builds submit -t ${IMAGE_TAG} ."
        }
      }
    }
    stage('Deploy Dev') {
      // Feature branch
      when { branch 'feature/*' }
      steps {
        container('kubectl') {
          // Change deployed image in canary to the one we just built
          sh("sed -i.bak 's#APP_IMAGE#${IMAGE_TAG}#' ./k8s/*.yaml")
          withKubeConfig([namespace: 'dev-ns']) {
                sh 'kubectl apply -f ./k8s/deployment.yaml'
              }
        }
      }
    }
  }
}
