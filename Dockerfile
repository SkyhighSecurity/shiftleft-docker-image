# Use an official Python runtime as a parent image
FROM python:3.9-slim
LABEL org.opencontainers.image.source="https://github.com/SkyhighSecurity/shiftleft-docker-image"

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app/

RUN pip install --trusted-host pypi.python.org -r requirements.txt
ENTRYPOINT ["python", "-u", "ShiftLeftInlineApplication.py"]

