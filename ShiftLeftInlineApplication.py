import requests
import json
import time
import os
from typing import List, Dict, Any
import sys
from requests.exceptions import RequestException, HTTPError
import requests.sessions
from requests.auth import HTTPBasicAuth
import logging
import time

logging.basicConfig(
    format='%(asctime)s %(levelname)-8s %(message)s',
    level=logging.DEBUG,
    datefmt='%Y-%m-%d %H:%M:%S'
)

class ShiftLeftInlineException(Exception):
    pass

class ShiftLeftInlineData:
    def __init__(self):
        self.changes = []
        self.user_name = ""
        self.password = ""
        self.clone_dir = ""
        self.csp_name = ""
        self.environment = ""
        self.bps_tenant_id = ""
        self.access_token = ""
        self.iam_token = ""

def strip_leading_dot_slash(filename: str) -> str:
    if filename.startswith("./"):
        return filename[2:]
    return filename

def clean_filename(filename: str) -> str:
    fields = filename.split('/')
    # Check if the string was split into multiple parts
    if len(fields) == 1:
        # If not, return the original string
        return filename
    else:
        # Otherwise, return the last field
        return fields[-1]


def perform_shift_left(args: List[str]):
    shift_left_inline_data = ShiftLeftInlineData()
    shift_left_inline_data.changes = [s.strip() for s in args[0].split(',') if s not in ("buildspec.yml", "azure-mvc-shiftleft-pipeline.yml")]
    shift_left_inline_data.user_name = args[1]
    shift_left_inline_data.password = args[2]
    shift_left_inline_data.clone_dir = args[3]
    shift_left_inline_data.csp_name = args[4]
    shift_left_inline_data.environment = args[5]
    if len(args) == 7:
        shift_left_inline_data.bps_tenant_id = args[6]

    if not shift_left_inline_data.changes:
        logging.info("Exiting, since there are no changes available to perform evaluation")
        return

    update_iam_token(shift_left_inline_data)
    update_access_token(shift_left_inline_data)
    violated_files = []

    for file in shift_left_inline_data.changes:
        response = submit_file_for_scan(strip_leading_dot_slash(file), shift_left_inline_data)
        if response is None:
            continue

        submit_response = json.loads(response["text"])
        status = submit_response["status"]
        message = submit_response["message"]
        if status.lower() == "failure":
            raise ShiftLeftInlineException(message)

        while True:
            try:
                time.sleep(30)
                logging.info(f"Checking the status for the file: {file}")
                headers = {"x-access-token": shift_left_inline_data.access_token, "Content-Type": "application/json"}
                status_response = requests.get(message, headers=headers)
            except requests.exceptions.RequestException as e:
                if isinstance(e, requests.exceptions.HTTPError) and e.response.status_code == 401:
                    update_access_token(shift_left_inline_data)
                    continue
                logging.error(f"Error while getting the status of the scan for file: {file} for scan with exception: {e}")
                continue

            scan_eval_response = status_response.json()

            if scan_eval_response["status"] in ("Submitted", "In Progress"):
                logging.info(scan_eval_response["message"])
                continue

            if scan_eval_response["status"].lower() == "failure":
                raise ShiftLeftInlineException(scan_eval_response["message"])

            violation_details = scan_eval_response["message"] # json.loads(scan_eval_response["message"])
            violation_count = violation_details["violation_count"]
            policies_violated = violation_details["policies_violated"]
            file_name = violation_details["file_name"]

            if violation_count > 0:
                violated_files.append(file_name)
                logging.info(f"{violation_count} violations were found for the file: {file_name}. Violated the policies: {policies_violated}")
                break

            elif violation_count == 0:
                logging.info(f"No violations were found for the file: {file_name}")
                break

    if violated_files:
        raise ShiftLeftInlineException(f"Failing the build, since violations were found for the files: {', '.join(violated_files)}.")

def update_iam_token(shift_left_inline_data):
    headers = {
        'Content-Type': 'application/json',
        'x-auth-username': shift_left_inline_data.user_name,
        'x-auth-password': shift_left_inline_data.password
    }

    auth = HTTPBasicAuth(shift_left_inline_data.user_name, shift_left_inline_data.password)

    if shift_left_inline_data.bps_tenant_id:
        headers['BPS-TENANT-ID'] = shift_left_inline_data.bps_tenant_id
    
    request = requests.post(shift_left_inline_data.environment + "/shnapi/rest/external/api/v1/token?grant_type=password&token_type=iam", headers=headers, auth=auth)

    if request.status_code != 200:
        error_msg = f"Unable to fetch IAM token for user: {shift_left_inline_data.user_name}"
        #logging.error(error_msg)
        raise ShiftLeftInlineException(error_msg)

    token_response = request.json()

    if token_response:
        shift_left_inline_data.iam_token = token_response.get('access_token')

        logging.info(f"Successfully received the IAM token for user: {shift_left_inline_data.user_name}")

def update_access_token(shift_left_inline_data):
    headers = {
        'Content-Type': 'application/json',
        'x-iam-token': shift_left_inline_data.iam_token
#        'x-auth-username': shift_left_inline_data.user_name,
#        'x-auth-password': shift_left_inline_data.password
    }
    if shift_left_inline_data.bps_tenant_id:
        headers['BPS-TENANT-ID'] = shift_left_inline_data.bps_tenant_id
    
    request = requests.post(shift_left_inline_data.environment + "/neo/neo-auth-service/oauth/token?grant_type=iam_token", headers=headers)
    
    if request.status_code != 200:
        error_msg = f"Unable to fetch access token from IAM token."
        #logging.error(error_msg)
        raise ShiftLeftInlineException(error_msg)

    token_response = request.json()

    if token_response:
        shift_left_inline_data.access_token = token_response.get('access_token')

        logging.info(f"Successfully received the access token from IAM token.")

def format_prepped_request(prepped, encoding=None):
    # prepped has .method, .path_url, .headers and .body attribute to view the request
    encoding = encoding or requests.utils.get_encoding_from_headers(prepped.headers)
    body = prepped.body.decode(encoding) if encoding else '<binary data>' 
    headers = '\n'.join(['{}: {}'.format(*hv) for hv in prepped.headers.items()])
    return f"""{prepped.method} {prepped.path_url} HTTP/1.1 {headers} {body}"""


def submit_file_for_scan(file_name: str, shift_left_inline_data: Dict[str, Any]):

    url = f'{shift_left_inline_data.environment}/neo/config-audit/devops/v1/scan?service={shift_left_inline_data.csp_name}'

    while True:
        try:
            payload = {'filename': clean_filename(file_name)}
            files = [
                ('templateFile',(file_name,open(os.path.join(shift_left_inline_data.clone_dir, file_name))))
            ]

            headers = {
                'x-access-token': shift_left_inline_data.access_token
            }

            session = requests.Session()
            req = requests.Request('POST', url, headers=headers, data=payload, files=files)
            prepped = session.prepare_request(req)
            response = session.send(prepped, verify=True)
            session.close

            files_size = 0
            for _, file_info in files:
                _, file_object = file_info
                file_object.seek(0, os.SEEK_END)
                files_size += file_object.tell()
                file_object.close()

        except RequestException as e:
            if isinstance(e, HTTPError) and e.response.status_code == 401:
                update_access_token(shift_left_inline_data)
                return submit_file_for_scan(file_name, shift_left_inline_data)
            logging.error(f"Error while submitting {file_name} for scan due to : {str(e)}")
            raise e
        else:
            if response.status_code == 429:
                logging.info(f"Server is busy (reponse code 429), trying {file_name} again in 60 seconds.")
                time.sleep(60)
            else:   
                logging.info(f"Successfully submitted {file_name} ({files_size} bytes) for scan")
                return {"status_code": response.status_code, "text": response.text}
    
if __name__ == "__main__":
    perform_shift_left(sys.argv[1:])
