pipeline {

  environment {
    PROJECT = "anodiamgcpproject"
    APP_NAME = "anodiam-login-service"
    TARGET_NAMESPACE = "dev-ns"
    CLUSTER_NAME = "cluster-anodiam-dev"
    CLUSTER_REGION = "us-central1"
    CLUSTER_ZONE = "us-central1-c"
    IMAGE_TAG = "${CLUSTER_REGION}-docker.pkg.dev/${PROJECT}/anodiam-repo/${APP_NAME}:v${env.BUILD_NUMBER}"
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
  component: cicd
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
    stage('Login to K8S cluster') {
      steps {
        container('gcloud') {
          sh "PYTHONUNBUFFERED=1 gcloud container clusters get-credentials ${CLUSTER_NAME} --zone=${CLUSTER_ZONE}"
        }
      }
    }
    stage('Deploy Dev') {
      // Feature branch
      when { branch 'feature/**' }
      steps {
        container('kubectl') {
          sh("sed -i.bak 's#APP_IMAGE#${IMAGE_TAG}#' ./k8s/*.yaml")
          sh 'kubectl apply -n dev-ns -f ./k8s'
        }
      }
    }
  }
}
