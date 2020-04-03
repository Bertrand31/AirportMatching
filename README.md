# Airport Matching

## Statement of purpose

The goal of this project is to build a small service consuming rows representing users and their locationn, and matching each of them with the nearest airport.

For the purpose of this demonstration, we will be consuming rows from a CSV file, and printing them
to stdout. In a real-world setting, this service would likely consume a stream (Kafka, Kinesis, RabbitMQ, etc.) and store its results in a storage medium such as a database (for example Cassandra) or
a filesystem.

This exercise only focuses on the part "in-between", where we match every user with an airport.
However, the codebase and the abstractions were crafted with a real-world situation in mind ; thus
why the "Bridge" abstraction was created. [Read more about bridges here](src/main/scala/airportmatching/Bridges/README.md).

## Finding the nearest neighbour of a two-dimensional point

Our task can be summed up as such: we need to find the nearest neigbour of a two-dimensional point.

It is important to note that our dataset (the world's airports) is not only small, but likely to
grow only marginally in the forseable future. This means that we can confidently fit it in memory,
and trade memory use to get as much speed as we can for our nearest neighbour queries.

The solution that was picked is to use a KD-Tree, but simplified and now tightly coupled to our
use-case: it only supports the types we need and operates with two-dimensional points. Thus, we
get maximum performance and [a rather simple implementation](src/main/scala/airportmatching/Artemis.scala). It was named after [Artemis](https://upload.wikimedia.org/wikipedia/commons/2/2a/Diane_de_Versailles_Leochares_2.jpg), the Greek goddess of the hunt.

It provides `Θ(n log² n)` time complexity for build (in our case, only performed once upon boot), and nearest-neighbour search in `Θ(log n)`.

## Performance

On a warm JVM: 710ms per million records

## Licensing

* `user-geo-sample.csv.gz`

The longitude, latitude data in this sample was taken from a data-set provided by Maxmind inc.
This work is licensed under the [Creative CommonsAttribution-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-sa/4.0/).

This database incorporates [GeoNames](http://www.geonames.org) geographical data, which is made available under the
[Creative Commons Attribution 3.0 License](http://www.creativecommons.org/licenses/by/3.0/us/).

* `optd-airports-sample.csv.gz`

Licensed under Creative Commons. For more information check [here](https://github.com/opentraveldata/optd/blob/trunk/LICENSE).
