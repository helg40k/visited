"""
Tasks for specifying the environment that
should be used for the actions
"""
# Do not alert for short functions
# or docstring for this file
#pylint: disable=C0103,C0111

from fabric.state import env
from fabric.decorators import task

__all__ = []

@task
def production():
    "Sets the environment to work on to PRODUCTION"
    env.environment = 'production'

@task
def qa():
    "Sets the environment to work on to QA"
    env.environment = 'qa'

@task
def future():
    "Sets the environment to work on to FUTURE"
    env.environment = 'future'

@task
def delta02():
    "Sets the environment to work on to DELTA"
    env.environment = 'delta02'