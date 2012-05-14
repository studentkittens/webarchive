#!/usr/bin/env python

import subprocess

cmd_git_log   = ['git','--no-pager','log','--reverse','--pretty="%h"']
cmd_git_ckout = ['git','checkout','--force','--quiet']

try: 
    p = subprocess.Popen(cmd_git_log,stdout=subprocess.PIPE, shell = False) 
    hashes = []

    for line in p.stdout:
        hashes.append(line.decode('UTF-8')[1:-2])

    for chash in hashes:
        print(cmd_git_ckout + [chash])
        subprocess.call(cmd_git_ckout + [chash])

    subprocess.call(cmd_git_ckout + ['master'])
    
except OSError as e:
    print(e)
