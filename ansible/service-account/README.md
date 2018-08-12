# See https://confluence.devfactory.com/pages/viewpage.action?spaceKey=DE&title=Programmatic+access+to+Kubernetes+API
# Create service account
# kubectl create serviceaccount <serviceaccount_username>
kubectl create serviceaccount service-account

# Copy rolebinding template into rolebinding.yml
# Replace namespace and serviceaccount_name placeholders into the rolebinding.yml file
# create the rolebinding object
kubectl create -f rolebinding.yml 

# Describe the service account and take note of the token name
kubectl describe serviceaccount service-account

# kubectl get secret <token_name> -o yaml
# copy the 'token' value into the global_k8s.yml file
