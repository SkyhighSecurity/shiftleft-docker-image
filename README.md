# Introduction
This project provides an interface for submitting infrastructure as code (IaC) templates to Skyhigh Security CSPM for configuration audit. Simply map the location of your CloudFormation, Azure Resource Manager (ARM) to the container and pass the list of files you wish to be inspected.

# Preparation
In your Skyhigh SSE console, create a user with at least the following permissions:
  Policy Management/Configuration Audit
  Policy Management/On-Demand Scan
  Incident Management
  
If you are using a SSO solution with Skyhigh, configure an exclusion for this user Under User Management / SAML configuration.

# Usage

First, generate a list of files to be submitted to Skyhigh for inspection relative to path that you will map to the container's /data path. Next, place your Skyhigh username and password, the name of the target IaaS provider, and Skyhigh environment URL into environment variables as follows:

SKYHIGH_USERNAME: Your Skyhigh username / email address
SKYHIGH_PASSWORD: Your Skyhigh password
IAAS_PROVIDER: The name of the IAAS provider which tells Skyhigh which active policies to evaluate against. Valid options are: aws, gcp, azure.
SKYHIGH_ENV: The region-specific base-url for the Skyhigh API. Valid options include: https://www.myshn.net , https://www.myshn.eu , and https://www.myshn.ca. Generally this URL will be displayed when you are logged into your Skyhigh dashboard.

Finally, execute the docker run command mapping the /data volume and passing the environment variables as parameters in this specific order:


<docker run command> <list of files> $SKYHIGH_USERNAME $SKYHIGH_PASSWORD "/data" $IAAS_PROVIDER $SKYHIGH_ENV

The example script below, when executed within that path, will find all .yaml, .yml, .json, and .tf files and write the list to iac-inspection.txt. It then sanitized the file list of extra characters, sets the environment variables, and executes the docker command.

``` bash
#!/bin/bash
find ./ -type f \( -iname "*.yaml" -o -iname "*.yml" -o -iname "*.tf" -o -iname "*.json" \) > iac-inspection.txt
if test "$( wc -l < iac-inspection.txt )" -gt 0; then
data=$(cat iac-inspection.txt | while read line; do echo $line; done)
data=$(echo $data  | tr ' ' ',')
SKYHIGH_USERNAME=user@domain.com
SKYHIGH_PASSWORD=your_password
IAAS_PROVIDER="aws" # Tells Skyhigh which set of active CSPM policies to execute against.  Valid options are aws, gcp, or azure.
SKYHIGH_ENV="https://www.myshn.net"
docker run -v /path/to/iac/files:/data ghcr.io/skyhighsecurity/shiftleft-docker-image:latest $data $SKYHIGH_USERNAME $SKYHIGH_PASSWORD "/data" $IAAS_PROVIDER $SKYHIGH_ENV
fi
```

# FAQ
### Can I specify a policy group to evaluate against
This is presently not supported.

### Can I make these API calls myself instead of using this container?
Absolutely. See the project's source code for examples of how to do this using Python.
