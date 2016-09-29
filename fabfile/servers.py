"""
Mapping from application to servers
for each supported environment
"""

from functools import wraps

from fabric.context_managers import settings
from fabric.utils import abort
from fabric.state import env

APP_ENV_HOST = {
    'mobeventprocessor': {
        'delta02': ['vm-sb-delta02.getaccess.no'],
        'future': [
            'vm-tt-wuapp61.getaccess.no'],
        'qa': [
            'vm-qa-minside01.getaccess.no',
            'vm-qa-minside02.getaccess.no'],
        'production': [
            'vm-pr-minside01.getaccess.no',
            'vm-pr-minside02.getaccess.no'],
        },
}


def _auto_host(app_name):
    """Decorator for running a task on all defined servers for the application
    in the given environment"""
    def dec(task_fn):
        "Outer wrapper for accepting app_name argument to decorator"
        @wraps(task_fn)
        def wrapper(*args, **kwargs):
            "Runs the task against all registered servers"
            if not env.environment:
                abort("No environment set for deploy")
            if not env.action:
                abort("No action set")
            hosts = APP_ENV_HOST[app_name][env.environment]
            for server in hosts:
                with settings(host_string=server, server=server):
                    task_fn(*args, **kwargs)
        return wrapper
    return dec

