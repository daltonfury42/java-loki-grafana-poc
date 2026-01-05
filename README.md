# JSON Structured Logging for Java/SLF4J using Grafana/Promtail/Loki

## Overview
A simple POC Java application that demonstrates structured logging in a Kubernetes environment using Grafana Loki and Promtail. The app logs an error in a loop, Slf4j is configured to output logs in JSON format, which is then parsed by Promtail, stored in Loki and displayed using Grafana.

## Features
- Java 11 application using Log4j2 for structured logging
- Containerized with Docker and deployed to Kubernetes via manifest
- Logs scraped and forwarded to Grafana Loki using Promtail
- Log visualization in Grafana

## Prerequisites
- Java 11+
- Maven
- Docker
- Minikube (or Kubernetes cluster)
- Helm

## Setup Instructions

### 1. Install Grafana and Loki-stack
```
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm upgrade --install loki grafana/loki-stack --namespace=loki --create-namespace
helm upgrade --install grafana grafana/grafana --namespace=grafana --create-namespace --set adminPassword='admin'
```

### 2. Port-forward Grafana UI
```
kubectl port-forward -n grafana svc/grafana 3000:80
```
Access Grafana at [http://localhost:3000](http://localhost:3000) (default user: admin, password: admin).

### 3. Build and Deploy the Java App
```
mvn clean package
docker build -t my-java-app:v1 .
minikube image load my-java-app:v1
```

Edit `java-app.yaml` to use the correct image tag (`v1`) if a new version was built, then deploy:
```
kubectl replace --force -f java-app.yaml
```

### 4. Configure Promtail for Log Scraping
Update Promtail config and apply as a Kubernetes secret:
```
kubectl create secret generic loki-promtail -n loki --from-file=promtail.yaml=my-config_provided.yaml --dry-run=client -o yaml | kubectl apply -f -
kubectl delete pod <promtail-pod-name> -n loki
```
Replace `<promtail-pod-name>` with the actual pod name.

### 5. Add Loki Data Source in Grafana
- In Grafana UI, add Loki as a data source (URL: `http://loki:3100`).
- Visualize logs from your Java app.

## Configuration Files
- `log4j2.properties` and `JsonLogLayout.json`: Log4j2 configs for JSON console output
- `java-app.yaml`: Kubernetes deployment manifest
- `my-config_provided.yaml`: Promtail config for scraping logs from the app pod

## Troubleshooting
- Ensure image tags match between Docker build and Kubernetes manifest
- Verify Promtail config matches pod labels (`app: my-java-app`)
- Check Grafana and Loki pods are running and accessible

## License
MIT

