# AirportMatching

## Statement of purpose

The goal of this project is to build a small service consuming rows representing users and their locationn, and matching each of them with the nearest airport.

For the purpose of this demonstration, we will be consuming rows from a CSV file, and printing them
to stdout. In a real-world setting, this service would likely consume a stream (Kafka, Kinesis, RabbitMQ, etc.) and store its results in a storage medium such as a database (for example Cassandra) or
a filesystem.

This exercise only focuses on the part "in-between", where we match every user with an airport.
However, the codebase and the abstractions were crafted with a real-world situation in mind ; thus
why the "Bridge" abstraction was created. [Read more about bridges here](src/main/scala/airportmatching/Bridges/README.md).

## Performance

710ms per million record

## Licensing

* `user-geo-sample.csv.gz`

The longitude, latitude data in this sample was taken from a data-set provided by Maxmind inc.
This work is licensed under the [Creative CommonsAttribution-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-sa/4.0/).

This database incorporates [GeoNames](http://www.geonames.org) geographical data, which is made available under the
[Creative Commons Attribution 3.0 License](http://www.creativecommons.org/licenses/by/3.0/us/).

* `optd-airports-sample.csv.gz`

Licensed under Creative Commons. For more information check [here](https://github.com/opentraveldata/optd/blob/trunk/LICENSE).
