"""
Fabric module for all exposed tasks for deployment.

Usage example: fab -u myadminuser qa deploy
"""
import sys
try:
    from fabric.state import env
except ImportError:
    print("Cant find fabric.")
    print("Please install it either by pip install fabric, or some other means")
    sys.exit(1)

from fabfile.actions import deploy, rollback
from fabfile.environments import production, qa, future, delta02
from fabfile.servers import APP_ENV_HOST

env.action = None
env.environment = None

HELP_TEXT = """
Usage: fab -u <myadminuser> <env> [deploy | rollback]

Registered environment mapping:
"""

if hasattr(env, 'tasks') and len(env.tasks) < 2:
    import pprint
    pp = pprint.PrettyPrinter(indent=2, depth=3)
    print(HELP_TEXT)
    pp.pprint(APP_ENV_HOST)
    sys.exit(1)
