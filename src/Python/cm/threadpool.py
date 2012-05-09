#!/usr/bin/env python
# encoding: utf-8

from time import sleep
from multiprocessing.pool import ThreadPool
#import multiprocessing

def calculate(func, args):
    result = func(*args)
    return '%s says that %s%s = %s' % (
        multiprocessing.current_process().name,
        func.__name__, args, result
        )

def mul(a,b):
    sleep(3)
    print('break')
    return a * b

if __name__ == '__main__':
    p = ThreadPool(2)

    #TASKS = [(mul, (i, 7)) for i in range(3000)]
    #results = [p.apply_async(calculate,t) for t in TASKS]

    ARGS = [(i,7) for i in range(3000)]
    results = [p.apply_async(mul,a) for a in ARGS]

    p.close()
    p.join()

