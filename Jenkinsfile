def label = "gcloud-command-${UUID.randomUUID().toString()}"

podTemplate(label: label, yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gcloud
    image: gcr.io/cloud-builders/gcloud
    command:
    - cat
    tty: true
"""
  ) {

  node(label) {
    stage('Test -  Execution of gcloud command') {
      container('gcloud') {
        sh "gcloud compute zones --help"
      }
    }

  }
}