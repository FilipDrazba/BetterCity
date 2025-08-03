
docker-compose \
--profile report \
--file .docker-composes/docker-compose-template.yml \
--env-file .envs/.env.template \
--profile backend \
up \
--build \
--force-recreate
