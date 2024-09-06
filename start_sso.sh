#!/bin/bash
docker-compose -f docker-compose.sso.yml -f docker-compose.override.yml up -d
docker-compose -f docker-compose.sso.yml -f docker-compose.override.yml logs -f