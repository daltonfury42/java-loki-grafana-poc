# JSON Structured Logging for Java/Log4j2 using Grafana/Promtail/Loki

## Overview
A simple POC Java application that demonstrates structured logging in a Kubernetes environment using Grafana Loki and [Promtail](https://grafana.com/docs/loki/latest/send-data/promtail/configuration/). The app logs an error in a loop, Log4j2 is configured with [JsonTemplateLayout](https://logging.apache.org/log4j/2.x/manual/json-template-layout.html) to output logs in JSON format, which is then parsed by Promtail, stored in Loki and displayed using Grafana.

<img width="1512" height="725" alt="image" src="https://github.com/user-attachments/assets/8e8294ea-5287-4a5d-9676-a399a8674d2f" />


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
helm upgrade --install loki grafana/loki-stack --namespace=loki --create-namespace --set loki.image.tag=2.9.15
helm upgrade --install grafana grafana/grafana --namespace=grafana --create-namespace --set adminPassword='admin' --set image.tag=10.2.6
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
kubectl delete pod -n loki -l app.kubernetes.io/instance=loki
```

### 5. Add Loki Data Source in Grafana
- In Grafana UI, add Loki as a data source (URL: `http://loki:3100` or `http://loki.loki.svc.cluster.local:3100` if grafana is in different namespace).
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

