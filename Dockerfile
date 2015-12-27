FROM clojure
MAINTAINER Jirka Penzes <jirkapenzes@gmail.com>

WORKDIR /blog
COPY . /blog/

RUN lein clean
RUN lein deps
RUN lein uberjar

EXPOSE 8080

CMD ["java", "-jar", "/blog/target/blog-0.1.0-SNAPSHOT-standalone.jar"]
