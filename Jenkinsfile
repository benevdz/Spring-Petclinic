pipeline {
    agent {label 'JDK_17'}
    options {
        retry(3)
        timeout(time: 30, unit: 'MINUTES')
    }
    triggers {
        pollSCM('* * * * *')
    }
    tools {
        jdk 'JDK_17'
        maven 'maven 3.9.4'
    }
    stages {
        stage('code') {
            steps {
                git url: 'https://github.com/KVKR31/dummy.git' ,
                branch : 'main'
            }
        }
        stage('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('build') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic-*.jar'
                junit testResults : '**/target/surefire-reports/TEST-*.xml'
            }
        }
    }
    post {
        success {
            mail subject: 'your project is effective',
                 body: 'your project is effective',
                 to: 'vinod@gmail.com'
        }
        failure {
            mail subject: 'your project is failure',
                 body: 'your project is defective',
                 to: 'vinod@gmail.com'
        }
    }
}
