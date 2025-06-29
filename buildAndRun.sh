
docker-compose \
--profile report \
--env-file .env.template \
up \
--build \
--force-recreate
