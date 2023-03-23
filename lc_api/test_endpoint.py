#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jul  1 11:39:00 2022

@author: mavroudo
"""
import requests


#send thing
url="http://localhost:8080/lifechamps/ehr"
h2={'Authorization': "demo token"}
d2={"test":"Test"}
r2=requests.post(url,json=d2,headers=h2)

#test
url="http://localhost:8080/lifechamps/healthcheck"
requests.get(url)