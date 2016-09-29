"""
The defined actions that can be performed
"""

# Do not alert for short functions
# or docstring for this file
#pylint: disable=C0103,C0111

from fabric.state import env
from fabric.decorators import task
from fabfile.servers import _auto_host
from fabfile.actionhandlers import DEFAULT_WEB_APP_HANDLERS

__all__ = []
APP_NAME = 'mobeventprocessor'

@task
def deploy(safe=False):
    """Sets the desired action by the application tasks to be deploy"""
    env.action = 'deploy'
    if safe != 'True':
        go()
    else:
        go(safe=True)

@task
def rollback():
    """Sets the desired action by the application tasks to be a rollback"""
    env.action = 'rollback'
    go()

@_auto_host(APP_NAME)
def go(safe=False):
    status_url = '%s:9037/mobeventprocessor/status' % (env.server) if safe else None
    """Performs the action set in the given environment for the application"""
    DEFAULT_WEB_APP_HANDLERS[env.action](
        app_name=APP_NAME,
        war_name=APP_NAME + '.war',
        status_url=status_url
    )