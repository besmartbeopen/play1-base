PLAY1-BASE
==========

**Play 1 project** used as **skeleton** by Be Smart Be Open during any new Play 1 project startup.

It contains features (forms, controllers, helpers, etc) for:
 - user management (user creation, password recovery, role assignment, etc)
 - user notification
 - task management
 - security enforcement using JBoss Drools 
 - comment on issue
 - italian geo data management (municipals, provinces, regions)
 - some useful play binders (Currency, LocalDate, LocalTime, Optional, DateRange, etc)

It also contains the integration with common library and tools, such as:
 - [google guice](https://github.com/google/guice)
 - [google guava](https://github.com/google/guava) (Collections, EventBus, etc)
 - integration with [gitlab](https://gitlab.com/) through a [Feign](https://github.com/OpenFeign/feign) based http library
 - [webpack](https://webpack.js.org/)
 - [prometheus](https://prometheus.io/) using [Micrometer](https://micrometer.io/)
 - [jboss drools](https://www.drools.org/)
 - [querydsl](http://www.querydsl.com/)
 - [hibernate envers](http://hibernate.org/orm/envers/)
 - [lombok](https://projectlombok.org/)
 - [postgresql](https://www.postgresql.org)
 - ...

First setup
-----------
After you've cloned the project you have at least to:
 - create a new database for the project
 - change database name, username and password (in conf/application.conf, the db param)

You can find the application default user and password in the conf/defaults.yml file, 
you can change them if you want.

Then you can run:

```sh
play deps
play evolutions:apply
```

If you want to import your project in eclipse you can execute:

```sh
play eclipsify
```

Eclipse run
------------
In order to run the project in Eclipse you should use this vmargs param:

```
 -javaagent:"${project_loc:play1-base}/lib/lombok-1.16.18.jar"
```

Don't forget to install [Lombok](https://projectlombok.org/) in your eclipse installation.

Versions
--------

Play version is **1.5.x**.

See package.json to check the javascript library versions.
Javascript versions could be interactively changed using:

```sh
yarn upgrade-interactive
```

We've used this jquery.history library:

jquery.history.js (v1.8a4) https://github.com/andreasbernhard/history.js
