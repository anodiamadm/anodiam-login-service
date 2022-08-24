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
      inheritFrom 'maven-java-template'
      defaultContainer 'jnlp'
}
  }
  stages {
    stage('Build Artifact') {
      steps {
        container('maven') {
          sh "mvn clean package -DskipTests"
          sh "mkdir artifact"
          sh "cp **/*.jar artifact"
          sh "cp Dockerfile artifact"
        }
      }
    }
    stage('Push image with Container Builder') {
      steps {
        container('gcloud') {
          sh "PYTHONUNBUFFERED=1 gcloud builds submit -t ${IMAGE_TAG} ./artifact"
        }
      }
    }
    stage('Deploy Dev') {
      // Feature branch
      when { branch 'feature/**' }
      steps {
        container('kubectl') {
          sh "PYTHONUNBUFFERED=1 gcloud container clusters get-credentials ${CLUSTER_NAME} --zone=${CLUSTER_ZONE}"
          sh("sed -i.bak 's#APP_IMAGE#${IMAGE_TAG}#' ./k8s/*.yaml")
          sh 'kubectl apply -n dev-ns -f ./k8s'
        }
      }
    }
  }
}
