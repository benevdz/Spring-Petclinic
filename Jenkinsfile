def COLOR_MAP = [
    'SUCCESS': 'green', 
    'FAILURE': 'red',
]

pipeline {
    agent any
     tools {
        maven 'maven 3.6'
        jdk 'Java 1.8'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile' 
            }
        }
        stage('Test') {
            steps {
                 sh 'mvn test'  
            }
        }
        stage('Package') {
            steps {
                 sh 'mvn package'  
            }
        }

        stage('Deploy') {
             /* only deploy when branch is master */
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy' 
            }
        }

    }

// Post-build actions
    post {
        
        success{
            //slackSend color: '#00ba09', message: 'test'
                slackSend color: '#00ba09',
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        }
        failure{
            //slackSend color: '#FF0000', message: 'test'
                slackSend color: '#ff0000',
               message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        }
    }
}


