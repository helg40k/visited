"""
Utility functions for working with
status servlets.
"""

from time import sleep
import sys
from fabric.context_managers import settings

from fabric.state import env
from fabric.operations import run
from fabric.colors import green
from fabric.utils import abort

from fabfile.common import LOAD_BALANCER_TIMEOUT


def disable(url, wait_time=LOAD_BALANCER_TIMEOUT):
    """
    Triggers the disable endpoint and waits for the given time. The time to
    wait defaults to common.LOAD_BALANCER_TIMEOUT
    """
    with settings(warn_only=True):
        if run("curl -s %s/disable| grep Disabled" % url).succeeded:
            print green("Waiting for LB to remove %s" % env.server)
            sleep(wait_time)


def wait_for(service_name, url, max_count=124, sleep_interval=5):
    """
    Blocks execution untill the status servlet on the given url
    returns status 'Enabled'
    """
    count = 0
    sys.stdout.write("Waiting for %s to come back up ." % service_name)
    with settings(warn_only=True):
        while run("curl -s %s | grep Enabled" % url).failed:
            sys.stdout.write(".")
            sys.stdout.flush()
            if count == max_count:
                sys.stdout.write("\n")
                sys.stdout.flush()
                abort("Did not come up after %d s of waiting."\
                        % max_count * sleep_interval)
                break
            count += 1
            sleep(sleep_interval)
        sys.stdout.write("\n")
