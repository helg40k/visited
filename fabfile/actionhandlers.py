from fabric.colors import green
from fabric.context_managers import cd, lcd
from fabric.contrib.files import exists
from fabric.operations import run, put, sudo
from fabric.utils import abort
from fabric.state import env

from fabfile.common import BASE_DIR, LOAD_BALANCER_TIMEOUT
from fabfile.status import disable, wait_for


def web_app_deploy_handler(
        app_name,
        war_name=None,
        status_url=None,
        wait_time=LOAD_BALANCER_TIMEOUT,
):
    war_name = war_name or (app_name + ".war")

    # Create rollback version
    with cd('/opt/get/%s/webapps' % app_name):
        if exists(war_name):
            print(green("Creating rollback version of %s" % app_name))
            sudo('cp %s %s-previous' % (war_name, war_name))

    if status_url is not None:
        # Disable in F5
        disable(status_url, wait_time)

    # Deploy new version
    with lcd(BASE_DIR):
        put("build/%s" % (war_name),
            "/opt/get/%s/webapps/%s" % (app_name, war_name),
            use_sudo=True)
        sudo('service %s restart' % app_name)

    if status_url is not None:
        # Wait for service to come back up
        wait_for(app_name, status_url)

    print(green("%s deployed and restarted." % app_name.capitalize()))
    if env.environment == 'production':
        notify_slack(app_name, env.environment, env.server)

def notify_slack(
    app_name,
    environment,
    server
):
    run("curl -X POST --data-urlencode 'payload={\"text\": \"A new version of *%s* was deployed to the %s environment on node %s\", \"channel\": \"#devops\", \"username\": \"deploy-bot\", \"icon_emoji\": \":monkey_face:\"}' https://hooks.slack.com/services/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" % (app_name, environment, server))

def web_app_rollback_handler(
        app_name,
        war_name=None,
        status_url=None,
        wait_time=LOAD_BALANCER_TIMEOUT,
):
    war_name = war_name or (app_name + ".war")

    print(green("Rolling back %s" % app_name))
    with cd('/opt/get/%s/webapps' % app_name):
        if not exists('%s-previous' % war_name):
            abort('No version available for rollback of %s' % app_name)
            return
        if status_url is not None:
            # Disable in F5
            disable(status_url, wait_time)

        sudo('cp %s-previous %s' % (war_name, war_name))
        sudo('service %s restart' % app_name)

    if status_url is not None:
        # Wait for service to come back up
        wait_for(app_name, status_url)

    print(green("%s deployed and restarted." % app_name.capitalize()))


DEFAULT_WEB_APP_HANDLERS = {
    'deploy': web_app_deploy_handler,
    'rollback': web_app_rollback_handler
}