# BITSBids backend

**NOTE:** Before contributing changes, we recommend you read the [Contributing Guide](../CONTRIBUTING.md)

## Steps for setup:

1. Install Docker
2. Run docker compose according to your needs

There's already a [.env](./.env) supplied for your convenience. You can change the values in the file if you want to.

## Information about this project's Docker system

This project's docker build system relies on something Docker Compose calls "profiles"

This project has 2 profiles as of now:

- `migration`
- `backend`

## Steps to run any given profile:

1. Run `docker compose --profile PROFILE down` to stop, and delete any containers in the profile named PROFILE.

   **NOTE:** Adding a `-v` flag at the end of this command WILL NOT delete any data in the database. You have to
   manually delete the `data` folder created by the `postgres` container.

2. Run `docker compose --profile PROFILE up --build` to build and run all containers in the profile named PROFILE.

   **NOTE:** Adding a `-d` flag runs the containers in the background, so if you want to look at the logs, omit this
   flag.

### Migration

Running the project with the `migration` profile runs the migrations
in the [./src/main/resources/db.migration.postgresql](./src/main/resources/db.migration.postgresql) folder.

#### Containers in the `migration` profile

- postgres
- flyway

**NOTE:** This same `postgres` container is used in `backend`.

We are using [Flyway](https://flywaydb.org/) for our migrations.

### Backend

Running the project with the `backend` profile copies the code, and runs the code like you would in a **dev environment**.
You should test any PR with this profile before
merging it.

#### Containers in the `backend` profile

- postgres
- app

**NOTE:** This same `postgres` container is used in `migration`.
